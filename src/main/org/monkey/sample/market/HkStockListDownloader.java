package org.monkey.sample.market;

import org.monkey.common.transport.HttpTransport;
import org.monkey.common.utils.BatchProcessor;
import org.monkey.common.utils.BatchingProcessor;
import org.monkey.sample.model.Security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

@Component
public class HkStockListDownloader implements SecuritiesDownloader {

    private static final Logger log = LoggerFactory.getLogger(HkStockListDownloader.class);
    public static final String HK_STOCK_SYMBOL_LIST_URL = "http://www.hkex.com.hk/eng/market/sec_tradinfo/stockcode/eisdeqty.htm";
    private HttpTransport transport;

    @Autowired
    public HkStockListDownloader(HttpTransport transport) {
        this.transport = transport;
    }

    @Override
    public List<Security> download() throws IOException {
        String response = transport.doGet(HK_STOCK_SYMBOL_LIST_URL);
        Vector<Vector> contents = parseHtmlTable(response);
        return mapToSecurities(contents);
    }

    private List<Security> mapToSecurities(Vector<Vector> contents) {
        // column structure
        // Stock Code || Name || Lot Size || # || H || O || F

        // # Admitted to Central Clearing and Settlement System
        // H Designated Securities eligible for shortselling
        // O Admitted to Stock Options
        // F Admitted to Stock Futures

        final List<Security> securitiesList = new ArrayList<Security>(contents.capacity());

        BatchingProcessor.process(contents, 200, new BatchProcessor<Vector>() {
            @Override
            public void doInBatch(Collection<Vector> batch) {
                for (Vector content : batch) {
                    if (content.size() == 7) {
                        final String hkExchangeSpecificId = (String) content.get(0);
                        String ric = hkExchangeSpecificId.substring(1) + ".HK";
                        String name = (String) content.get(1);
                        Integer lotSize = Integer.valueOf(((String) content.get(2)).replace(",", ""));
                        securitiesList.add(new Security(ric, name, lotSize));
                    }
                }
            }

            @Override
            public void reportProcessed(int batchSize, int totalJobSize, int remaining) {
                log.info(String.format("Creating securities objects. Processed %s items, %s remaining", (totalJobSize - remaining), remaining));
            }
        });
        return securitiesList;
    }

    private Vector<Vector> parseHtmlTable(String response) {
        final HtmlTableParser htmlTableParser = new HtmlTableParser();
        htmlTableParser.parse(response);
        return htmlTableParser.getContents();
    }
}
