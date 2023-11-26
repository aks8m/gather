package com.github.aks8m.crawl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Stack;

/**
 * Depth First Crawler based on <a href="https://en.wikipedia.org/wiki/Depth-first_search">Wikipedia Link</a>
 */

public class DepthFirstCrawler extends Crawler {

    private final static Logger LOG = LoggerFactory.getLogger(DepthFirstCrawler.class.getSimpleName());

    private final Stack<String> pageStack;

    public DepthFirstCrawler() {
        this.pageStack = new Stack<>();
    }

    @Override
    protected void crawl(String rootURL) {
        //Push rootURL to visit
        pageStack.push(rootURL);
        while (!pageStack.isEmpty()){
            //Visit page
            visit(pageStack.pop());
            //Look at all links on page and queue to visit (if not queued or visited prior)
            peek(linkURL -> {
                if(!pageStack.contains(linkURL)){
                    pageStack.push(linkURL);
                }
            });
        }
    }
}
