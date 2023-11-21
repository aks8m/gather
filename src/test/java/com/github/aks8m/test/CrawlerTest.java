package com.github.aks8m.test;

import com.github.aks8m.crawl.BreadthFirstCrawler;
import com.github.aks8m.crawl.Crawler;
import com.github.aks8m.crawl.DepthFirstCrawler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CrawlerTest {

    /**
     *                 I
     *             A       B
     *    B <- C -   - D -   - E
     */

    /**
     * Breadth First Crawler Test
     */
    @Test
    public void breadthFirstCrawl(){
        //Given a mocked website, see above site layout
        final String rootURL = "http://localhost:8080";

        //When a breadth first crawl is ran on the root page
        Crawler crawler = new BreadthFirstCrawler(rootURL);
        try {
            crawler.crawl();
        } catch (IOException e){
            e.printStackTrace();
        }
        //Then the crawler should crawl the pages and only visit 6 pages (to account/ignore for cycles in site)
        assertEquals(6, crawler.history().size(), "History count not matching");

        //Then the crawler should crawl the pages in the following order -> I, A, B, C, D, E
        assertEquals("http://localhost:8080", crawler.history().get(0), "first page order incorrect");
        assertEquals("http://localhost:8080/a.html", crawler.history().get(1), "second page order incorrect");
        assertEquals("http://localhost:8080/b.html", crawler.history().get(2), "third page order incorrect");
        assertEquals("http://localhost:8080/c.html", crawler.history().get(3), "fourth page order incorrect");
        assertEquals("http://localhost:8080/d.html", crawler.history().get(4), "fifth page order incorrect");
        assertEquals("http://localhost:8080/e.html", crawler.history().get(5), "sixth page order incorrect");
    }

    /**
     * Depth First Crawler Test
     */
    @Test
    public void depthFirstCrawl(){
        //Given a mocked website, see above site layout
        final String rootURL = "http://localhost:8080";

        //When a depth first crawl is ran on root page
        Crawler crawler = new DepthFirstCrawler(rootURL);
        try {
            crawler.crawl();
        } catch (IOException e){
            e.printStackTrace();
        }

        //Then the crawler should crawl the pages and only visit 6 pages (to account/ignore for cycles in site)
        assertEquals(6, crawler.history().size(), "History count not matching");

        //Then the crawler should crawl the pages in the following order -> I, A, C, B, D, E
        assertEquals("http://localhost:8080", crawler.history().get(0), "first page order incorrect");
        assertEquals("http://localhost:8080/b.html", crawler.history().get(1), "second page order incorrect");
        assertEquals("http://localhost:8080/e.html", crawler.history().get(2), "third page order incorrect");
        assertEquals("http://localhost:8080/d.html", crawler.history().get(3), "fourth page order incorrect");
        assertEquals("http://localhost:8080/a.html", crawler.history().get(4), "fifth page order incorrect");
        assertEquals("http://localhost:8080/c.html", crawler.history().get(5), "sixth page order incorrect");
    }
}