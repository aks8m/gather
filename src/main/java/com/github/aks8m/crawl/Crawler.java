package com.github.aks8m.crawl;

import java.io.IOException;
import java.util.List;

public interface Crawler {

    /**
     * Performs the Crawl based on the type of Crawler initialized
     * @throws IOException - Handle any exception thrown from cral
     */
    void crawl() throws IOException;

    /**
     * Linear history of crawl
     * @return
     */
    List<String> history();
}
