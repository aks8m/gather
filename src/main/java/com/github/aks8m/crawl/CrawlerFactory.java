package com.github.aks8m.crawl;

/**
 * Factory Pattern to help instantiate the correct Crawler based on Crawler Type
 */
public class CrawlerFactory {

    public static Crawler make(CrawlerType crawlerType, String rootURL){
        return switch (crawlerType){
            case DEPTH_FIRST -> new DepthFirstCrawler(rootURL);
            case BREADTH_FIRST -> new BreadthFirstCrawler(rootURL);
        };
    }
}
