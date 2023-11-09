package com.github.aks8m;

import rife.bld.BaseProject;

import java.util.List;

import static rife.bld.dependencies.Repository.MAVEN_CENTRAL;

public class GatherBuild extends BaseProject {
    public GatherBuild() {
        pkg = "com.github.aks8m";
        name = "Gather";
        mainClass = "com.github.aks8m.GatherMain";
        version = version(0,1,0);

        downloadSources = true;
        repositories = List.of(MAVEN_CENTRAL);

        testOperation().mainClass("com.github.aks8m.GatherTest");
    }

    public static void main(String[] args) {
        new GatherBuild().start(args);
    }
}
