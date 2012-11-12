package org.monkey.common.exception;

import java.util.List;

public interface HasMultipleCauses {
    List<Throwable> getCauses();
}
