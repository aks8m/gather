package com.github.aks8m;

public class GatherTest {
    void verifyHello() {
        if (!"Hello World!".equals(new GatherMain().getMessage())) {
            throw new AssertionError();
        } else {
            System.out.println("Succeeded");
        }
    }

    public static void main(String[] args) {
        new GatherTest().verifyHello();
    }
}