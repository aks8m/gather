package com.github.aks8m.crawl;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class Crawler {

    private final static Logger LOG = LoggerFactory.getLogger(Crawler.class.getSimpleName());

    private final SubmissionPublisher<Document> publisher;
    private final Filter filter;
    private final Crawl crawl;
    private final URI rootURL;

    public Crawler(URI root, Filter filter, Crawl crawl){
        this.publisher = new SubmissionPublisher<>();
        this.filter = filter;
        this.rootURL = root;
        this.crawl = crawl;
    }

    public void registerScraper(Flow.Subscriber<Document> subscriber){
        publisher.subscribe(subscriber);
    }

    public void go(){
        try {
            crawl.execute(rootURL, filter, publisher);
        } catch (IOException ioException){
            LOG.error(ioException.getMessage());
        }
    }
}
