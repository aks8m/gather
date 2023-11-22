package com.github.aks8m.gather;

import com.github.aks8m.crawl.Crawler;
import com.github.aks8m.crawl.CrawlerFactory;
import com.github.aks8m.crawl.CrawlerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class Gather {

    private static final Logger LOG = LoggerFactory.getLogger(Gather.class.getSimpleName());
    private final Crawler crawler;

    public Gather(CrawlerType crawlerType, String rootURL){
        this.crawler = CrawlerFactory.make(crawlerType, rootURL);
    }

    public void go(){
        try {
            crawler.crawl();
        } catch (IOException e){
            LOG.error("Crawl Failed", e);
        }
    }

    public List<String> results(){
        return crawler.history();
    }
}
