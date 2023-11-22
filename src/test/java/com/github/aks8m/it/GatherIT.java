package com.github.aks8m.it;

import com.github.aks8m.crawl.CrawlerType;
import com.github.aks8m.gather.Gather;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GatherIT {

    @Test
    public void testCrawlOnLiveWebsite(){
        //Given an instance of Gather object
        Gather gather = new Gather(CrawlerType.BREADTH_FIRST,
                "https://www.wcostream.tv/anime/one-piece-english-subbed");

        //When Gather is configured with live URL
        gather.go();

        //Then results from a crawl should properly reflect the URL
        List<String> results = gather.results();

        assertTrue(!results.isEmpty());
    }

}
