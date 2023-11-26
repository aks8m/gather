package com.github.aks8m.crawl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Breadth First Crawler based on <a href="https://en.wikipedia.org/wiki/Breadth-first_search">Wikipedia Link</a>
 */
public class BreadthFirstCrawler extends Crawler{

    private final static Logger LOG = LoggerFactory.getLogger(BreadthFirstCrawler.class.getSimpleName());

    private final Queue<String> pageQueue;

    public BreadthFirstCrawler(){
        this.pageQueue = new PriorityQueue<>();
    }

    @Override
    protected void crawl(String rootURL) {
        //Queue rootURL to visit
        pageQueue.add(rootURL);
        while(!pageQueue.isEmpty()){
            //Visit page
            visit(pageQueue.poll());
            //Look at all links on page and queue to visit (if not queued or visited prior)
            peek(linkURL -> {
                if (!pageQueue.contains(linkURL)){
                    pageQueue.add(linkURL);
                    incrementDepth();
                }
            });
            //Increment current depth of crawl
            incrementDepth();
        }
    }
}
