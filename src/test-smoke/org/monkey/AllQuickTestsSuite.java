package org.monkey;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        UnitTestSuite.class,
        IntegrationTestSuite.class,
        SmokeTestSuite.class
})
public class AllQuickTestsSuite {

}
