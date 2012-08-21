package org.monkey.common.transport

import org.apache.http.HttpStatus
import org.apache.http.StatusLine
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static org.mockito.Mockito.when
import static org.mockito.Mockito.mock

@RunWith(MockitoJUnitRunner.class)
class PooledHttpTransportTest {

    PooledHttpTransport transport

    @Mock StatusLine statusLine

    @Before
    public void before() {
        transport = new PooledHttpTransport()
    }

    @Test
    public void isNotSuccessReturnsTrueWhenStatusCodeIsEitherOf2xxOr3xx() {
        assert !transport.isSuccess(null)

        def sc_processing = mock(StatusLine.class)
        def sc_ok = mock(StatusLine.class)
        def sc_temporary_redirect = mock(StatusLine.class)
        def sc_bad_request = mock(StatusLine.class)

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
