package com.github.aks8m.scrape;

import org.jsoup.nodes.Document;

@FunctionalInterface
public interface Scrape {
    Result execute(Document document);
}
