package org.monkey;

import org.junit.runner.RunWith;
import org.monkey.common.junit.DirectoryScanningSpec;
import org.monkey.common.junit.DirectoryScanningSuite;

@RunWith(DirectoryScanningSuite.class)
@DirectoryScanningSpec(testSourceRoot = "src/test-smoke", filesToInclude = {"**/*Test.java", "**/*Test.groovy"})
public class SmokeTestSuite {
}
