package org.monkey.common.servicewrapper

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import org.monkey.common.exception.ServiceException

import static org.mockito.Mockito.*
import static org.monkey.common.servicewrapper.ServicesAggregator.aggregate

@RunWith(MockitoJUnitRunner.class)
class ServicesAggregatorTest {

    @Mock Service service1, service2
    def shouldFail = new GroovyTestCase().&shouldFail

    ServicesAggregator aggregator

    @Before
    public void before() {
        when(service1.name()).thenReturn("service_1")
        when(service2.name()).thenReturn("service_2")

        aggregator = ServicesAggregator.aggregate(service1, service2)
    }

    @Test
    public void startServicesInTheOrderTheyWereAdded() {
        def inOrder = inOrder(service1, service2)

        aggregator.start()

        inOrder.verify(service1).start()
        inOrder.verify(service2).start()
    }

    @Test
    public void stopServicesInReverseOrder() {
        def inOrder = inOrder(service2, service1)

        aggregator.start()
        aggregator.stop()

        inOrder.verify(service2).stop()
        inOrder.verify(service1).stop()
    }

    @Test
    public void stopServicesEvenIfAnExceptionIsThrown() {
        def errorMessage = "some error message"
        when(service1.toString()).thenReturn("service_1")
        doThrow(new RuntimeException(errorMessage)).when(service1).stop()

        aggregator.start()
        assert shouldFail(ServiceException) { aggregator.stop() } == "Could not stop services: [service_1]. Failure messages [Error when trying to stop [service_1]]"
    }

    @Test
    public void displayNameInAPrettyFormat() {
        def expectedName =
            "ServicesAggregator (0) [\n" +
            "\tServicesAggregator (0) [\n" +
            "\t\tgrandChildService1,\n" +
            "\t\tServicesAggregator (0) [\n" +
            "\t\t\tgreatGrandChildService1,\n" +
            "\t\t\tgreatGrandChildService2\n" +
            "\t\t]\n" +
            "\t],\n" +
            "\tServicesAggregator (0) [\n" +
            "\t\tgrandChildService2,\n" +
            "\t\tgrandChildService3\n" +
            "\t]\n" +
            "]\n";

        def topLevelService = aggregate(
            aggregate(newService("grandChildService1"), aggregate(newService("greatGrandChildService1"), newService("greatGrandChildService2"))),
            aggregate(newService("grandChildService2"), newService("grandChildService3"))
        )

        assert ServicesAggregator.prettyPrint(topLevelService.name()) == expectedName
    }

    private Service newService(String name) {
        def service = mock(Service.class, name)
        when(service.name()).thenReturn(name)
        return service
    }
}
