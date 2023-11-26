package com.github.aks8m.crawl;

import com.github.aks8m.TestHTTPServer;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BreadthFirstCrawlerTest {

    private final TestHTTPServer testHTTPServer = new TestHTTPServer();

    /**
     *                 I
     *             A       B
     *         C -   - D -   - E
     *    B <-                   >> https://blank.page
     */

    @BeforeAll
    public void setupSuite(){
        testHTTPServer.start();
    }

    @AfterAll
    public void teardownSuite(){
        testHTTPServer.stop();
    }


    @Test
    public void breadthFirstCrawlerNoExternal(){
        //Given a breadth first crawler and a test site
        Crawler crawler = Crawler.make(CrawlerType.BREADTH_FIRST);

        //When the crawler performs its crawl with no external links
        crawler.performCrawl(testHTTPServer.url(), false);

        //Then the crawler should crawl the sample site in the following page order: I, A, B, C, D, E
        assertEquals(6, crawler.history().size(), "history contains 6 pages");
        assertEquals("http://localhost:8080", crawler.history().get(0), "first page order incorrect");
        assertEquals("http://localhost:8080/a.html", crawler.history().get(1), "second page order incorrect");
        assertEquals("http://localhost:8080/b.html", crawler.history().get(2), "third page order incorrect");
        assertEquals("http://localhost:8080/c.html", crawler.history().get(3), "fourth page order incorrect");
        assertEquals("http://localhost:8080/d.html", crawler.history().get(4), "fifth page order incorrect");
        assertEquals("http://localhost:8080/e.html", crawler.history().get(5), "sixth page order incorrect");
    }

    @Test
    public void breadFirstCrawlerWithExternal(){
        //Given a breadth first crawler and a test site
        Crawler crawler = Crawler.make(CrawlerType.BREADTH_FIRST);

        //When the crawler performs its crawl with external links allowed
        crawler.performCrawl(testHTTPServer.url(), true);

        //Then the crawler should crawl the sample site in the following page order: I, A, B, C, D, E, https://blank.page
        assertEquals(7, crawler.history().size(), "history contains 7 pages");
        assertEquals("http://localhost:8080", crawler.history().get(0), "first page order incorrect");
        assertEquals("http://localhost:8080/a.html", crawler.history().get(1), "second page order incorrect");
        assertEquals("http://localhost:8080/b.html", crawler.history().get(2), "third page order incorrect");
        assertEquals("http://localhost:8080/c.html", crawler.history().get(3), "fourth page order incorrect");
        assertEquals("http://localhost:8080/d.html", crawler.history().get(4), "fifth page order incorrect");
        assertEquals("http://localhost:8080/e.html", crawler.history().get(5), "sixth page order incorrect");
        assertEquals("https://blank.page", crawler.history().get(6), "seventh page order incorrect");
    }

    @Test
    @Disabled
    public void breadthFirstCrawlerWithDepthOne(){
        //Given a breadth first crawler and a test site
        Crawler crawler = Crawler.make(CrawlerType.BREADTH_FIRST);

        //When the crawler performs its crawl with external links not allowed and a depths of 1
        crawler.performCrawl(testHTTPServer.url(), false, 1);

        //Then the crawler should crawl the sample site in the following page order: I, A, B
        assertEquals(3, crawler.history().size(), "history contains 3 pages");
        assertEquals("http://localhost:8080", crawler.history().get(0), "first page order incorrect");
        assertEquals("http://localhost:8080/a.html", crawler.history().get(1), "second page order incorrect");
        assertEquals("http://localhost:8080/b.html", crawler.history().get(2), "third page order incorrect");
    }
}
