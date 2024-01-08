package com.github.aks8m.common;

public class Helper {

    public static void throttle(){
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
