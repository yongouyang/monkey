package org.monkey.web.object

import org.monkey.common.marshall.json.JacksonJsonMarshaller
import org.monkey.common.marshall.json.JsonMarshaller
import org.monkey.common.transport.HttpTransport
import org.monkey.common.transport.PooledHttpTransport


class ApiSupport {

    protected HttpTransport httpTransport = new PooledHttpTransport()
    protected JsonMarshaller marshaller = new JacksonJsonMarshaller()



}
