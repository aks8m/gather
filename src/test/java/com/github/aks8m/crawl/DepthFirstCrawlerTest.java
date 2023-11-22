package com.github.aks8m.crawl;

import com.github.aks8m.TestHTTPServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DepthFirstCrawlerTest {

    /**
     *                 I
     *             A       B
     *    B <- C -   - D -   - E
     */

    /**
     * Depth First Crawler Test
     */

    private final TestHTTPServer testHTTPServer = new TestHTTPServer();

    @BeforeAll
    public void setupSuite(){
        testHTTPServer.start();
    }

    @AfterAll
    public void teardownSuite(){
        testHTTPServer.stop();
    }

    @Test
    public void depthFirstCrawl(){
        //Given a mocked website, see above site layout
        //When a depth first crawl is ran on root page
        Crawler crawler = new DepthFirstCrawler(testHTTPServer.url());
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