package org.monkey.common.concurrent;

import org.monkey.common.servicewrapper.Service;

import java.util.concurrent.*;

public class ExecutorsWrapper {

    /**
     * @param namePrefix the prefix to give all thread names
     * @return the newly created thread pool
     */
    public static ExecutorService newCachedThreadPool(String namePrefix) {
        return Executors.newCachedThreadPool(namedThreadFactory(namePrefix));
    }

    /**
     * @param nThreads   the number of threads in the pool
     * @param namePrefix the prefix to give all thread names
     * @return the newly created thread pool
     * @throws IllegalArgumentException if <tt>nThreads &lt;= 0</tt>
     */
    public static ExecutorService newFixedThreadPool(int nThreads, String namePrefix) {
        return Executors.newFixedThreadPool(nThreads, namedThreadFactory(namePrefix));
    }

    /**
     * @param nThreads   the number of threads in the pool
     * @param namePrefix the prefix to give all thread names
     * @param handler    the handler to use when execution is blocked
     *                   because the thread bounds and queue capacities are reached.
     * @param queue      the queue to use for holding tasks before they
     *                   are executed. This queue will hold only the <tt>Runnable</tt>
     *                   tasks submitted by the <tt>execute</tt> method.
     * @return the newly created <tt>ThreadPoolExecutor</tt>
     * @throws IllegalArgumentException if <tt>nThread &lt;= 0</tt>
     */
    public static ThreadPoolExecutor newBoundedQueueFixedThreadPool(int nThreads, String namePrefix, RejectedExecutionHandler handler, BlockingQueue<Runnable> queue) {
        return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, queue, namedThreadFactory(namePrefix), handler);
    }

    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize, String namePrefix) {
        return Executors.newScheduledThreadPool(corePoolSize, namedThreadFactory(namePrefix));
    }

    public static ScheduledExecutorService newSingleThreadScheduledExecutor(String namePrefix) {
        return Executors.newSingleThreadScheduledExecutor(namedThreadFactory(namePrefix));
    }

    public static ExecutorService newSingleThreadExecutor(String namePrefix) {
        return Executors.newSingleThreadExecutor(namedThreadFactory(namePrefix));
    }

    public static void shutdownNowExecutor(ExecutorService executorService) {
        executorService.shutdownNow();
    }

    public static Service asService(final ExecutorService executorService) {
        return new Service() {
            @Override
            public void start() throws Exception {
                //do nothing
            }

            @Override
            public void stop() throws Exception {
                ExecutorsWrapper.shutdownNowExecutor(executorService);
            }

            @Override
            public String name() {
                return executorService.getClass().getSimpleName();
            }
        };
    }


    private static NamedThreadFactory namedThreadFactory(String namePrefix) {
        return new NamedThreadFactory("monkey-" + namePrefix);
    }
}
