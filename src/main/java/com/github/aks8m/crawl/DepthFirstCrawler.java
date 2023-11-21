package com.github.aks8m.crawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Depth First Crawler based on <a href="https://en.wikipedia.org/wiki/Depth-first_search">...</a>
 */

public class DepthFirstCrawler implements Crawler {

    private final List<String> history;
    private final String rootURL;
    private final Stack<String> pageStage;

    public DepthFirstCrawler(String rootURL) {
        this.rootURL = rootURL;
        this.history = new ArrayList<>();
        this.pageStage = new Stack<>();
    }

    @Override
    public void crawl() throws IOException {
        //Push rootURL to visit
        pageStage.push(rootURL);
        while (!pageStage.isEmpty()){
            //Visit page
            Document page = Jsoup.connect(pageStage.pop()).get();
            history.add(page.location());
            //Look at all links on page and queue to visit (if not queued or visited prior)
            for(Element link : page.select("a[href]")){
                String linkURL = link.attr("href");
                if (!history.contains(linkURL) && !pageStage.contains(linkURL)){
                    pageStage.push(linkURL);
                }
            }
        }
    }

    @Override
    public List<String> history() {
        return history;
    }
}
