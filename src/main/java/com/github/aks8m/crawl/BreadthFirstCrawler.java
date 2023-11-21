package com.github.aks8m.crawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Breadth First Crawler based on <a href="https://en.wikipedia.org/wiki/Breadth-first_search">...</a>
 */
public class BreadthFirstCrawler implements Crawler{

    private final static Logger LOG = LoggerFactory.getLogger(BreadthFirstCrawler.class.getSimpleName());

    private final Queue<String> pageQueue;
    private final List<String> history;
    private final String rootURL;

    public BreadthFirstCrawler(String rootURL){
        this.rootURL = rootURL;
        this.pageQueue = new PriorityQueue<>();
        this.history = new ArrayList<>();
    }

    @Override
    public void crawl() throws IOException {
        //Queue rootURL to visit
        pageQueue.add(rootURL);
        while(!pageQueue.isEmpty()){
            //Visit page
            Document page = Jsoup.connect(pageQueue.poll()).get();
            history.add(page.location());
            LOG.info("Visited " + page.location());

            //Stop Point here

            //Look at all links on page and queue to visit (if not queued or visited prior)
            for(Element link : page.select("a[href]")){
                String linkURL = link.attr("href");
                if (!history.contains(linkURL) && !pageQueue.contains(linkURL)){
                    pageQueue.add(linkURL);
                    LOG.info("Identified " + linkURL);
                }
            }
        }
    }

    @Override
    public List<String> history() {
        return history;
    }
}
