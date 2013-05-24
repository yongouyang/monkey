package org.monkey.sample.market;

import org.monkey.sample.model.Security;

import java.io.IOException;
import java.util.List;

public interface SecuritiesDownloader {

    List<Security> download() throws IOException;
}
