package com.github.aks8m;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    public String getMessage() {
        return "Hello World!";
    }

    private static final Logger LOG = LoggerFactory.getLogger(App.class.getSimpleName());


    public static void main(String[] args) {
        System.out.println(new App().getMessage());
        LOG.info("It works!!");
    }
}