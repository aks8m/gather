package com.github.aks8m.crawl;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.SubmissionPublisher;

@FunctionalInterface
public interface Crawl {
    void execute(URI root, Filter filter, SubmissionPublisher<Document> publisher) throws IOException;
}
