package com.github.aks8m.scrape;

import java.nio.file.Path;
import java.util.List;

public record Result(String url, String description, Path media, List<String> tags) {

    @Override
    public String toString() {
        StringBuilder tagBuilder = new StringBuilder();
        tags.forEach(tag -> tagBuilder.append(tag + ", "));

        return url +
                ": " +
                description +
                " saved at " +
                media.toString() +
                " with tags(" +
                tagBuilder.substring(0, tagBuilder.toString().length() - 2) +
                ")";
    }
}
