package org.monkey.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BatchingProcessor {

    public static <T> void process(Collection<T> totalJobs, int batchSize, BatchProcessor<T> batchProcessor) {
        List<T> remaining = new ArrayList<T>(totalJobs);
        while (!remaining.isEmpty()) {
            List<T> batch = remaining.subList(0, Math.min(batchSize, remaining.size()));
            batchProcessor.doInBatch(batch);
            int actualBatchSize = batch.size();
            batch.clear();
            batchProcessor.reportProcessed(actualBatchSize, totalJobs.size(), remaining.size());
        }

    }
}
