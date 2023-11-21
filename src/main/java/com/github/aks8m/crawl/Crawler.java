package com.github.aks8m.crawl;

import java.io.IOException;
import java.util.List;

public interface Crawler {

    void crawl() throws IOException;
    List<String> history();
}
