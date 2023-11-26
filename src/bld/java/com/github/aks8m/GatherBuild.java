package com.github.aks8m;

import rife.bld.Project;

import java.util.List;

import static rife.bld.dependencies.Repository.*;
import static rife.bld.dependencies.Scope.*;

public class GatherBuild extends Project {
    public GatherBuild() {
        pkg = "com.github.aks8m";
        name = "gather";
        mainClass = "com.github.aks8m.GatherMain";
        version = version(0,1,0);

        downloadSources = true;
        repositories = List.of(MAVEN_CENTRAL, RIFE2_RELEASES);
        
        scope(compile)
                .include(dependency("org.jsoup", "jsoup", version(1, 16, 2)))
                .include(dependency("org.slf4j", "slf4j-api", version(2, 0, 9)))
                .include(dependency("org.slf4j", "slf4j-reload4j", version(2,0,9)))
                .include(dependency("ch.qos.reload4j", "reload4j", version(1, 2, 25)));

        scope(test)
                .include(dependency("org.junit.jupiter", "junit-jupiter", version(5,10,0)))
                .include(dependency("org.junit.platform", "junit-platform-console-standalone", version(1,10,0)))
                .include(dependency("com.google.jimfs", "jimfs", version(1, 3, 0)))
                .include(dependency("org.slf4j", "slf4j-api", version(2, 0, 9)))
                .include(dependency("org.slf4j", "slf4j-reload4j", version(2,0,9)))
                .include(dependency("ch.qos.reload4j", "reload4j", version(1, 2, 25)));
    }

    public static void main(String[] args) {
        new GatherBuild().start(args);
    }
}