package org.monkey.common.transport

import org.apache.http.HttpStatus
import org.apache.http.StatusLine
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static org.mockito.Mockito.when

@RunWith(MockitoJUnitRunner.class)
class PooledHttpTransportTest {

    PooledHttpTransport transport

    @Mock StatusLine sc_processing, sc_ok, sc_temporary_redirect, sc_bad_request

    @Before
    public void before() {
        transport = new PooledHttpTransport()
    }

    @Test
    public void isNotSuccessReturnsTrueWhenStatusCodeIsEitherOf2xxOr3xx() {
        assert !transport.isSuccess(null)

        when(sc_processing.statusCode).thenReturn(HttpStatus.SC_PROCESSING)
        when(sc_ok.statusCode).thenReturn(HttpStatus.SC_OK)
        when(sc_temporary_redirect.statusCode).thenReturn(HttpStatus.SC_TEMPORARY_REDIRECT)
        when(sc_bad_request.statusCode).thenReturn(HttpStatus.SC_BAD_REQUEST)

        assert !transport.isSuccess(sc_processing)
        assert transport.isSuccess(sc_ok)
        assert transport.isSuccess(sc_temporary_redirect)
        assert !transport.isSuccess(sc_bad_request)
    }
}
