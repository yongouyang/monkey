package org.monkey.web.object

import org.monkey.common.marshall.json.JacksonJsonMarshaller
import org.monkey.common.transport.PooledHttpTransport


class ApiSupport {

    def httpTransport = new PooledHttpTransport()
    def marshaller = new JacksonJsonMarshaller()



}
