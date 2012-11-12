package org.monkey.common.servicewrapper;

import com.google.common.base.Function;
import org.apache.commons.lang.StringUtils;
import org.monkey.common.concurrent.ExecutorsWrapper;
import org.monkey.common.exception.Defect;
import org.monkey.common.exception.ServiceException;
import org.monkey.common.utils.CollectionUtils;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.reverse;
import static org.monkey.common.utils.CollectionUtils.reverseNow;

public class ServicesAggregator implements Service {

    public static final int SHUTDOWN_TIMEOUT_SECOND = 60;

    protected final List<Service> servicesToStart;
    protected final List<Service> servicesToStop = new CopyOnWriteArrayList<Service>();
    private final RuntimeException creationStackTrace;

    protected ServicesAggregator(Iterable<? extends Service> servicesToStart) {
        creationStackTrace = new RuntimeException("AggregateService was not shutdown. See this stacktrace for where this aggregate service was created");
        this.servicesToStart = newArrayList();
        for (Service service : servicesToStart) {
            addServiceToStart(service);
        }
    }

    public ServicesAggregator(Service... servicesToStart) {
        this(Arrays.asList(servicesToStart));
    }

    public static ServicesAggregator aggregate(Service... services) {
        return new ServicesAggregator(services);
    }


    @Override
    public void start() throws Exception {
        Iterator<Service> iterator = servicesToStart.iterator();
        while (iterator.hasNext()) {
            Service service = iterator.next();
            iterator.remove();
            start(service);
        }
    }

    @Override
    public void stop() throws Exception {
        List<Exception> caughtExceptions = newArrayList();

        if (!servicesToStart.isEmpty()) {
            System.out.println(String.format("These services [%s] are supposed to have been started before stop() is invoked!", serviceNames(servicesToStart)));
        }

        for (Service stoppable : reverseNow(servicesToStop)) {
            try {
                timedStop(stoppable);

                if (!servicesToStop.remove(stoppable)) {
                    throw new Defect(String.format("Failed to remove service [%s] from the stopping list", stoppable.name()));
                }
            } catch (Exception e) {
                caughtExceptions.add(new Defect(String.format("Error when trying to stop [%s]", stoppable.name()), e));
            }
        }

        if (!caughtExceptions.isEmpty()) {
            throw new ServiceException("Could not stop services: " + reverse(servicesToStop), caughtExceptions);
        }

    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public String name() {
        StringBuilder builder = new StringBuilder();

        for (Service service : servicesToStart) {
            if (builder.length() > 0) {
                builder.append(",");
            }
            builder.append(service.name());
        }

        for (Service service : servicesToStop) {
            if (builder.length() > 0) {
                builder.append(",");
            }
            String name = service.name();
            builder.append((name.trim().length() == 0) ? name.getClass().getName() : name);
        }

        return getClass().getSimpleName() + " (" + servicesToStop.size() + ") [" + builder.toString() + "]";
    }

    private void timedStop(final Service stoppable) {
        ExecutorService executorService = ExecutorsWrapper.newSingleThreadExecutor("timed-stopped-for-" + stoppable.name());
        Future<?> future = executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    long start = System.currentTimeMillis();

                    try {
                        stoppable.stop();
                    } finally {
                        long end = System.currentTimeMillis();
                        long timeTaken = end - start;
                        if (timeTaken > 10000) {
                            System.out.println(String.format("Took %sms to shut down %s", timeTaken, stoppable.name()));
                        }
                    }
                } catch (Exception e) {
                    throw new Defect("Problem stopping " + stoppable.name(), e);
                }
            }
        });

        try {
            future.get(SHUTDOWN_TIMEOUT_SECOND, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return;
        } catch (ExecutionException e) {
            throw new Defect("Problem stopping " + stoppable.name(), e);
        } catch (TimeoutException e) {
            programmaticThreadDump();
            throw new Defect(String.format("Took more than %ss to shut down %s", SHUTDOWN_TIMEOUT_SECOND, stoppable.name()));
        } finally {
            executorService.shutdownNow();
        }

    }

    private static void programmaticThreadDump() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(true, true);
        for (ThreadInfo threadInfo : threadInfos) {
            System.err.println(threadInfo);
        }
    }

    public Connection start(Connection connection) throws Exception {
        start(jmsConnectionService(connection));
        return connection;

    }

    public <T extends Service> T start(T service) throws Exception {
        try {
            service.start();
            return service;
        } finally {
            addServiceToStop(service);
        }
    }

    public void start(Service... services) throws Exception {
        for (Service service : services) {
            service.start();
            addServiceToStop(service);
        }
    }

    public <T extends Service> T addServiceToStart(T service) {
        if (service == null) throw new Defect("You can't have a null service");
        if (service == this) throw new Defect("You can't add a service to itself!");
        if (servicesToStart.contains(service))
            throw new Defect(String.format("Trying to add a service [%s] more than once.", service.name()));

        servicesToStart.add(service);
        return service;
    }

    public <T extends Service> void remove(T service) {
        servicesToStart.remove(service);
    }

    public <T extends Service> T addServiceToStop(T service) {
        servicesToStop.add(service);
        return service;
    }

    public void addServiceToStop(Service... services) {
        for (Service service : services) {
            addServiceToStop(service);
        }
    }

    public ExecutorService addServiceToStop(final ExecutorService service) {
        servicesToStop.add(new Service() {
            @Override
            public void start() throws Exception {
            }

            @Override
            public void stop() throws Exception {
                service.shutdownNow();
            }

            @Override
            public String name() {
                return ExecutorService.class.getName();
            }
        });
        return service;
    }

    public Session addServiceToStop(final Session session) {
        addServiceToStop(new Service() {
            @Override
            public void start() throws Exception {
            }

            @Override
            public void stop() throws Exception {
                session.close();
            }

            @Override
            public String name() {
                return "jmsSession";
            }
        });
        return session;
    }

    public MessageConsumer addServiceToStop(final MessageConsumer consumer) {
        addServiceToStop(new Service() {
            @Override
            public void start() throws Exception {
            }

            @Override
            public void stop() throws Exception {
                consumer.close();
            }

            @Override
            public String name() {
                return "jmsMessageConsumer";
            }
        });
        return consumer;
    }

    public MessageProducer addServiceToStop(final MessageProducer producer) {
        addServiceToStop(new Service() {
            @Override
            public void start() throws Exception {
            }

            @Override
            public void stop() throws Exception {
                producer.close();
            }

            @Override
            public String name() {
                return "jmsMessageProducer";
            }
        });
        return producer;
    }

    public <T extends Service> T stop(T service) throws Exception {
        servicesToStop.remove(service);
        service.stop();
        return service;
    }

    public void addServicesToStart(Iterable<Service> services) {
        for (Service service : services) {
            addServiceToStart(service);
        }
    }

    public void addServicesToStart(Service... services) {
        for (Service service : services) {
            addServiceToStart(service);
        }
    }

    public <T extends Service> T add(T service) {
        addServiceToStart(service);
        return service;
    }

    public static String prettyPrint(String original) {
        String done = original
                .replaceAll("\\[", "\\[\n")
                .replaceAll("\\]", "\n]")
                .replaceAll(",", ",\n");

        StringBuilder result = new StringBuilder();
        int tabCount = 0;
        for (String line : done.split("\n")) {
            char[] tabs = tabs(tabCount);
            result.append(new String(tabs)).append(line).append("\n");
            if (line.endsWith("[")) {
                tabCount++;
            } else if (line.endsWith("]")) {
                tabCount = Math.max(0, tabCount - 1);
            } else if (!line.endsWith(",")) {
                tabCount = Math.max(0, tabCount - 1);
            }
        }
        return result.toString();
    }

    private static char[] tabs(int tabCount) {
        char[] tabs = new char[tabCount];
        Arrays.fill(tabs, '\t');
        return tabs;
    }

    @Override
    protected void finalize() throws Throwable {
        if (!servicesToStart.isEmpty()) {
            System.out.println("There were services that were not started!");
            System.out.println("Services to start: " + serviceNames(servicesToStart));
            creationStackTrace.printStackTrace(System.out);
        }

        if (!servicesToStop.isEmpty()) {
            System.out.println("There were services that were not stopped!");
            System.out.println("Services to stop: " + serviceNames(servicesToStop));
            creationStackTrace.printStackTrace(System.out);
        }
        super.finalize();
    }

    private Service jmsConnectionService(final Connection connection) {
        return new Service() {
            @Override
            public void start() throws Exception {
                connection.start();
            }

            @Override
            public void stop() throws Exception {
                try {
                    connection.stop();
                } finally {
                    connection.close();
                }
            }

            @Override
            public String name() {
                return "jmsConnection";
            }
        };
    }

    private String serviceNames(List<Service> services) {
        return StringUtils.join(CollectionUtils.transformNow(services, new Function<Service, String>() {
            @Override
            public String apply(Service service) {
                return service.name();
            }
        }), ", ");
    }
}
