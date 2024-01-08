package com.github.aks8m.crawl;

import java.net.URI;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Filter {

    private final String regex;

    public Filter(String regex) {
        this.regex = regex;
    }

    public boolean filter(URI pagURL){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pagURL.getPath());
        return matcher.find();
    }

    //TODO - prefix filter
    //TODO - suffix filter
    //TODO - regex is really contains
}
