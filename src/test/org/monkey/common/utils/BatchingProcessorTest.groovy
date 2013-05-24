package org.monkey.common.utils

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner

import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.verifyZeroInteractions

@RunWith(MockitoJUnitRunner.class)
class BatchingProcessorTest {

    @Mock BatchProcessor<Integer> batchProcessor

    @Test
    public void willDoNothingWhenAskedToProcessAnEmptyJob() {
        BatchingProcessor.process([], 100, batchProcessor)

        verifyZeroInteractions(batchProcessor)
    }

    @Test
    public void willProcessALargeJobInBatches() {
        def jobs = 1..35
        def batchSize = 10

        BatchingProcessor.process(jobs, batchSize, batchProcessor)

        verify(batchProcessor, times(4)).doInBatch(Mockito.anyListOf(Integer))

        verify(batchProcessor).reportProcessed(10, 35, 25)
        verify(batchProcessor).reportProcessed(10, 35, 15)
        verify(batchProcessor).reportProcessed(10, 35, 5)
        verify(batchProcessor).reportProcessed(5, 35, 0)
    }
}
