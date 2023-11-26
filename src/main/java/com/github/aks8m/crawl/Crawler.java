package com.github.aks8m.crawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class Crawler {

    private final Logger LOG = LoggerFactory.getLogger(Crawler.class.getSimpleName());

    private final List<Document> history = new ArrayList<>();
    private final Map<String, Integer> depthHistory = new HashMap<>();
    private String rootURL;
    private boolean allowExternalPeek;
    private int maximumCrawlDepth;
    private int currentDepth = 0;

    /**
     * Create an instance of a crawler based on the Crawl Type
     * @param crawlerType Type of Crawl based on the crawl algorithm
     * @return Crawler instance
     */
    public static Crawler make(CrawlerType crawlerType){
        return switch (crawlerType){
            case BREADTH_FIRST -> new BreadthFirstCrawler();
            case DEPTH_FIRST -> new DepthFirstCrawler();
        };
    }

    /**
     * Performs the Crawl based on the type of Crawler initialized
     */
    protected abstract void crawl(String rootURL);

    /**
     * Invoke the created crawler
     * @param rootURL Website root URL to crawl
     * @param allowExternalLink Allow crawler to crawl external links
     */
    public void performCrawl(String rootURL, boolean allowExternalLink){
        this.rootURL = rootURL;
        this.allowExternalPeek = allowExternalLink;
        this.maximumCrawlDepth = 6;
        crawl(rootURL);
    }

    /**
     * Invoke the created crawler
     * @param rootURL Website root URL to crawl
     * @param allowExternalLink Allow crawler to crawl external links
     */
    public void performCrawl(String rootURL, boolean allowExternalLink, int maximumCrawlDepth){
        this.rootURL = rootURL;
        this.allowExternalPeek = allowExternalLink;
        this.maximumCrawlDepth = maximumCrawlDepth;
        crawl(rootURL);
    }

    /**
     * Peek at all links that are present on current page and apply passed in logic to them
     * @param peekLogic Consumer containing what to do with peeked link URLs
     */
    protected void peek(Consumer<String> peekLogic){
        this.history.get(history.size() - 1).select("a[href]").stream()
                .map(link -> link.attr("abs:href"))
                .forEach(linkURL -> {
                    if (!history().contains(linkURL) && peekable(linkURL)){
                        peekLogic.accept(linkURL);
                    }
                });
    }

    /**
     * Visit a website by getting its page and adding it to the crawler history
     * @param pageURL URL of page to visit
     */
    protected void visit(String pageURL){
        try {
            history.add(Jsoup.connect(pageURL).userAgent("chrome").get());
        } catch (IOException ioException){
            LOG.error("Error performing page visit", ioException);
        }
    }

    protected void incrementDepth(){

        //Will need to tag pages that we know will visit based on peek
        currentDepth++;
    }

    /**
     * History of crawl
     * @return List of strings of crawled URLs
     */
    public List<String> history(){
        return history.stream().map(Document::location).toList();
    }

    /**
     * See if the passed in link is peekable (should be visited by crawler)
     * @param linkURL Link to potential page to visit
     * @return True if page should be visited, False if it shouldn't be visited
     */
    private boolean peekable(String linkURL){
        boolean isPeekable = false;
        boolean isExternalLink = !linkURL.startsWith(this.rootURL);
        if (allowExternalPeek){
            isPeekable = true;
        } else {
            if (!isExternalLink){
                isPeekable = true;
            }
        }
        return isPeekable;
    }
}
