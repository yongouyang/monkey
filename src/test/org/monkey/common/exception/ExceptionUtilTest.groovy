package org.monkey.common.exception

import org.junit.Test


class ExceptionUtilTest {

    @Test
    public void returnsTheCauseOfAnException() {
        def child = new Defect("child")
        def parent = new Defect("parent", child)
        assert ExceptionUtil.getOriginalCause(parent) == child
    }

    @Test
    public void returnsTheExceptionAsCauseOfAnExceptionInNoCause() {
        def parent = new Defect("parent")
        assert ExceptionUtil.getOriginalCause(parent) == parent
    }
}
