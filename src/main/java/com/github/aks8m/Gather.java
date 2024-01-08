package com.github.aks8m;

import com.github.aks8m.common.Helper;
import com.github.aks8m.crawl.Crawl;
import com.github.aks8m.crawl.Crawler;
import com.github.aks8m.crawl.Filter;
import com.github.aks8m.scrape.Result;
import com.github.aks8m.scrape.Scrape;
import com.github.aks8m.scrape.Scraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


public class Gather {

    public static class Builder{

        private String rootURLString;
        private Crawl crawl;
        private String filterRegex;
        private List<Scrape> scrapes;

        public Builder() {
            this.scrapes = new ArrayList<>();
        }

        public Builder rootURL(String rootURLString){
            this.rootURLString = rootURLString;
            return this;
        }

        public Builder crawl(Crawl crawl){
            this.crawl = crawl;
            return this;
        }

        public Builder filter(String filterRegex){
            this.filterRegex = filterRegex;
            return this;
        }

        public Builder addScrape(Scrape scrape){
            this.scrapes.add(scrape);
            return this;
        }

        public Builder simpleCrawl(){
            this.crawl = (root, filter, publisher) -> {
                Document rootDocument = Jsoup.connect(root.toString()).userAgent("chrome").get();
                publisher.submit(rootDocument);

                rootDocument.select("a[href]")
                        .stream()
                        .map(link -> link.attr("abs:href"))
                        .map(URI::create)
                        .filter(linkURL -> linkURL.getHost().equals(root.getHost()))
                        .filter(filter::filter)
                        .distinct()
                        .map(uri -> {
                            try {
                                return Jsoup.connect(uri.toString()).userAgent("chrome").get();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .forEach(document -> {
                            Helper.throttle();
                            publisher.submit(document);
                        });
            };
            return this;
        }

        public Gather build(){
            URI root = URI.create(this.rootURLString);
            Filter filter = new Filter(filterRegex);
            Scraper scraper = new Scraper(scrapes);
            Crawler crawler = new Crawler(root, filter, crawl);
            crawler.registerScraper(scraper);
            return new Gather(crawler, scraper);
        }
    }

    private final Crawler crawler;
    private final Scraper scraper;

    private Gather(Crawler crawler, Scraper scraper) {
        this.crawler = crawler;
        this.scraper = scraper;
    }

    public static Builder builder(){
        return new Builder();
    }

    public void go(){
        crawler.go();
    }

    public List<String> history(){
        return scraper.results().stream().map(Result::toString).toList();
    }
}
