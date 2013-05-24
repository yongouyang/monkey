package org.monkey.common.utils;

import java.util.Collection;

public interface BatchProcessor<T> {

    void doInBatch(Collection<T> batch);

    void reportProcessed(int batchSize, int totalJobSize, int remaining);
}
