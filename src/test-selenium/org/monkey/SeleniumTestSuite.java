package org.monkey;

import org.junit.runner.RunWith;
import org.monkey.common.junit.DirectoryScanningSpec;
import org.monkey.common.junit.DirectoryScanningSuite;

@RunWith(DirectoryScanningSuite.class)
@DirectoryScanningSpec(testSourceRoot = "src/test-selenium", filesToInclude = {"**/*Test.java", "**/*Test.groovy"})
public class SeleniumTestSuite {
}
