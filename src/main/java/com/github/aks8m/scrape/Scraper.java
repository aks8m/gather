package com.github.aks8m.scrape;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

public class Scraper implements Flow.Subscriber<Document> {

    private final static Logger LOG = LoggerFactory.getLogger(Scraper.class.getSimpleName());
    private Flow.Subscription subscription;
    private final List<Scrape> scrapes;
    private final List<Result> results;

    public Scraper(List<Scrape> scrapes) {
        this.scrapes = scrapes;
        this.results = new ArrayList<>();
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(1);
        LOG.info("Scrapper subscribed to Crawler " + subscription);
    }

    @Override
    public void onNext(Document item) {
        this.subscription.request(1);
        scrapes.forEach(scrape -> results.add(scrape.execute(item)));
        LOG.info("Scraping - " + item.location() + " with " + scrapes.size() + " scrapes");
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }

    public List<Result> results(){
        return  results;
    }
}
