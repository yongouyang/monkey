package org.monkey.common.concurrent

import org.junit.Before
import org.junit.Test

import java.util.concurrent.ThreadFactory

class NamedThreadFactoryTest {

    String prefix
    ThreadFactory threadFactory

    @Before
    public void before() {
        prefix = "some-prefix"
        threadFactory = new NamedThreadFactory(prefix)
    }

    @Test
    public void willCreateAThreadNamedWithGivenPrefix() {
        def prefix = "some-prefix"
        def threadFactory = new NamedThreadFactory(prefix)
        def thread = threadFactory.newThread(someRunnable())
        assert thread.name.startsWith(prefix)
    }

    @Test
    public void willNumberEachThreadCreated() {
        assert threadFactory.newThread(someRunnable()).name.endsWith("thread-1")
        assert threadFactory.newThread(someRunnable()).name.endsWith("thread-2")
    }

    @Test
    public void willHaveANameFormatAs() {
        assert threadFactory.newThread(someRunnable()).name == "some-prefix-thread-1"
        assert threadFactory.newThread(someRunnable()).name == "some-prefix-thread-2"
    }

    private Runnable someRunnable() {
        return new Runnable() {
            @Override
            void run() {
                //do nothing
            }
        }
    }
}
