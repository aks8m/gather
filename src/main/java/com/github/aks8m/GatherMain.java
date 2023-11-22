package com.github.aks8m;

public class GatherMain {
    public String getMessage() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(new GatherMain().getMessage());
    }
}