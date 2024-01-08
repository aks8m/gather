package com.github.aks8m.crawl;

import com.github.aks8m.Gather;
import com.github.aks8m.scrape.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.nio.file.Path;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class GatherTest {

    @Test
    public void crawlSomeAnime(){
        Gather gather = Gather.builder()
                .rootURL("https://www.wco.tv/anime/one-piece-english-subbed")
                .filter(".*one-piece-episode-\\d+-english-subbed(?!#|-\\d$)")
                .simpleCrawl()
                .addScrape(document -> {

                    if(document.baseUri().toString().equals("https://www.wco.tv/anime/one-piece-english-subbed")){


                    } else { //TODO - make more than one scrape!!! this will prevent cyclomatic complexity here

                    }

                    return new Result(document.baseUri(), "", Path.of("/help"), List.of(""));


                })
                .addScrape(document -> {
                    return new Result("", "", Path.of("/"), List.of(""));
                })
                .build();

        gather.go();

        System.out.println(gather.history());

    }
}
