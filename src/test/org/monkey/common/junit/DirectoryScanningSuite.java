package org.monkey.common.junit;

import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

public class DirectoryScanningSuite extends Suite {

    public DirectoryScanningSuite(Class<?> testClass) throws ClassNotFoundException, InitializationError {
        super(testClass, getTestClasses(getScanningSpec(testClass)));
    }

    private static DirectoryScanningSpec getScanningSpec(Class<?> testClass) throws InitializationError {
        final DirectoryScanningSpec scanningSpec = testClass.getAnnotation(DirectoryScanningSpec.class);
        if (scanningSpec == null) {
            throw new InitializationError(String.format("@%s is required for class '%s' to run with %s",
                    DirectoryScanningSpec.class.getSimpleName(),
                    testClass.getName(),
                    DirectoryScanningSuite.class.getSimpleName()));
        }
        return scanningSpec;
    }

    public static Class[] getTestClasses(DirectoryScanningSpec scanningSpec) throws ClassNotFoundException {
        return DirectoryScanningUtils.getClasses(scanningSpec.testSourceRoot(), scanningSpec.filesToInclude(), scanningSpec.filesToExclude());
    }
}
