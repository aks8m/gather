package com.github.aks8m;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.SimpleFileServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TestHTTPServer {

    private static final Logger LOG = LoggerFactory.getLogger(TestHTTPServer.class.getSimpleName());

    private HttpServer httpServer;
    private final String url = "http://localhost:8080";

    public static void main(String[] args){
        TestHTTPServer testHTTPServer = new TestHTTPServer();
        testHTTPServer.start();
    }

    /**
     * URL the HTTP server is configured to use
     * @return String URL
     */
    public String url(){
        return url;
    }

    /**
     * Start the HTTP Server
     */
    public void start(){
        InetSocketAddress address = new InetSocketAddress("0.0.0.0", 8080);

        try {
            FileSystem fileSystem = Jimfs.newFileSystem(Configuration.unix());
            Path html = fileSystem.getPath("/html");
            Files.createDirectory(html);
            createHTMLSite(html);
            httpServer = SimpleFileServer.createFileServer(address, html, SimpleFileServer.OutputLevel.NONE);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

        httpServer.start();
    }

    public void stop(){
        httpServer.stop(0);
    }

    /**
     * Create the following HTML pages (as strings) using JIMFS library
     *
     *                  I
     *              A -   - B
     *          C -   - D -   - E
     *      F -   - G     - H -    - I
     *        <- B            -> *    - J
     *                                    - K
     *  *https://blank.page
     * @return Index or Root Path object
     */
    private void createHTMLSite(Path html) throws IOException {
        Path indexHTML = html.resolve("index.html");
        Files.createFile(indexHTML);
        Files.write(indexHTML, List.of(
                "<!DOCTYPE html>\n",
                "<html lang=\"en\">\n",
                "<head>\n",
                "    <meta charset=\"UTF-8\">\n",
                "    <title>Index</title>\n",
                "</head>\n",
                "<body>\n",
                "Page: Index\n",
                "<br>\n",
                "<a href=\"http://localhost:8080/a.html\">A</a>\n",
                "<br>\n",
                "<a href=\"http://localhost:8080/b.html\">B</a>\n",
                "</body>\n",
                "</html>"), StandardCharsets.UTF_8);

        Path aHTML = html.resolve("a.html");
        Files.createFile(aHTML);
        Files.write(aHTML, List.of(
                "<!DOCTYPE html>\n",
                "<html lang=\"en\">\n",
                "<head>\n",
                "    <meta charset=\"UTF-8\">\n",
                "    <title>A</title>\n",
                "</head>\n",
                "<body>\n",
                "Page: A\n",
                "<br>\n",
                "<a href=\"http://localhost:8080/c.html\">C</a>\n",
                "<br>\n",
                "<a href=\"http://localhost:8080/d.html\">D</a>\n",
                "</body>\n",
                "</html>"), StandardCharsets.UTF_8);

        Path bHTML = html.resolve("b.html");
        Files.createFile(bHTML);
        Files.write(bHTML, List.of(
                "<!DOCTYPE html>\n",
                "<html lang=\"en\">\n",
                "<head>\n",
                "    <meta charset=\"UTF-8\">\n",
                "    <title>B</title>\n",
                "</head>\n",
                "<body>\n",
                "Page: B\n",
                "<br>\n",
                "<a href=\"http://localhost:8080/d.html\">D</a>\n",
                "<br>\n",
                "<a href=\"http://localhost:8080/e.html\">E</a>\n",
                "</body>\n",
                "</html>"), StandardCharsets.UTF_8);

        Path cHTML = html.resolve("c.html");
        Files.createFile(cHTML);
        Files.write(cHTML, List.of(
                "<!DOCTYPE html>\n",
                "<html lang=\"en\">\n",
                "<head>\n",
                "    <meta charset=\"UTF-8\">\n",
                "    <title>C</title>\n",
                "</head>\n",
                "<body>\n",
                "Page: C\n",
                "<br>\n",
                "<a href=\"http://localhost:8080/f.html\">F</a>\n",
                "<br>\n",
                "<a href=\"http://localhost:8080/g.html\">G</a>\n",
                "</body>\n",
                "</html>"), StandardCharsets.UTF_8);

        Path dHTML = html.resolve("d.html");
        Files.createFile(dHTML);
        Files.write(dHTML, List.of(
                "<!DOCTYPE html>\n",
                "<html lang=\"en\">\n",
                "<head>\n",
                "    <meta charset=\"UTF-8\">\n",
                "    <title>D</title>\n",
                "</head>\n",
                "<body>\n",
                "Page: D\n",
                "<br>\n",
                "<a href=\"http://localhost:8080/h.html\">H</a>\n",
                "</body>\n",
                "</html>"), StandardCharsets.UTF_8);

        Path eHTML = html.resolve("e.html");
        Files.createFile(eHTML);
        Files.write(eHTML, List.of(
                "<!DOCTYPE html>\n",
                "<html lang=\"en\">\n",
                "<head>\n",
                "    <meta charset=\"UTF-8\">\n",
                "    <title>E</title>\n",
                "</head>\n",
                "<body>\n",
                "Page: E\n",
                "<br>\n",
                "<a href=\"http://localhost:8080/h.html\">H</a>\n",
                "<br>\n",
                "<a href=\"http://localhost:8080/i.html\">I</a>\n",
                "</body>\n",
                "</html>"), StandardCharsets.UTF_8);

        Path fHTML = html.resolve("f.html");
        Files.createFile(fHTML);
        Files.write(fHTML, List.of(
                "<!DOCTYPE html>\n",
                "<html lang=\"en\">\n",
                "<head>\n",
                "    <meta charset=\"UTF-8\">\n",
                "    <title>F</title>\n",
                "</head>\n",
                "<body>\n",
                "Page: F\n",
                "<br>\n",
                "<a href=\"http://localhost:8080/b.html\">B</a>\n",
                "</body>\n",
                "</html>"), StandardCharsets.UTF_8);

        Path gHTML = html.resolve("g.html");
        Files.createFile(gHTML);
        Files.write(gHTML, List.of(
                "<!DOCTYPE html>\n",
                "<html lang=\"en\">\n",
                "<head>\n",
                "    <meta charset=\"UTF-8\">\n",
                "    <title>G</title>\n",
                "</head>\n",
                "<body>\n",
                "Page: G\n",
                "</body>\n",
                "</html>"), StandardCharsets.UTF_8);

        Path hHTML = html.resolve("h.html");
        Files.createFile(hHTML);
        Files.write(hHTML, List.of(
                "<!DOCTYPE html>\n",
                "<html lang=\"en\">\n",
                "<head>\n",
                "    <meta charset=\"UTF-8\">\n",
                "    <title>H</title>\n",
                "</head>\n",
                "<body>\n",
                "Page: H\n",
                "<br>\n",
                "<a href=\"https://blank.page\">Blank Page</a>\n",
                "</body>\n",
                "</html>"), StandardCharsets.UTF_8);

        Path iHTML = html.resolve("i.html");
        Files.createFile(iHTML);
        Files.write(iHTML, List.of(
                "<!DOCTYPE html>\n",
                "<html lang=\"en\">\n",
                "<head>\n",
                "    <meta charset=\"UTF-8\">\n",
                "    <title>I</title>\n",
                "</head>\n",
                "<body>\n",
                "Page: I\n",
                "<br>\n",
                "<a href=\"http://localhost:8080/j.html\">J</a>\n",
                "</body>\n",
                "</html>"), StandardCharsets.UTF_8);

        Path jHTML = html.resolve("j.html");
        Files.createFile(jHTML);
        Files.write(jHTML, List.of(
                "<!DOCTYPE html>\n",
                "<html lang=\"en\">\n",
                "<head>\n",
                "    <meta charset=\"UTF-8\">\n",
                "    <title>J</title>\n",
                "</head>\n",
                "<body>\n",
                "Page: J\n",
                "<br>\n",
                "<a href=\"http://localhost:8080/k.html\">K</a>\n",
                "</body>\n",
                "</html>"), StandardCharsets.UTF_8);

        Path kHTML = html.resolve("k.html");
        Files.createFile(kHTML);
        Files.write(kHTML, List.of(
                "<!DOCTYPE html>\n",
                "<html lang=\"en\">\n",
                "<head>\n",
                "    <meta charset=\"UTF-8\">\n",
                "    <title>K</title>\n",
                "</head>\n",
                "<body>\n",
                "Page: K\n",
                "</body>\n",
                "</html>"), StandardCharsets.UTF_8);
    }

}
