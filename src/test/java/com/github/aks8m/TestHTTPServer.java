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
     *             A       B
     *         C -   - D -   - E
     *    B <-                   >> https://blank.page
     *
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
                "<a href=\"http://localhost:8080/b.html\">B</a>\n",
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
                "<a href=\"https://blank.page\">Google</a>\n",
                "</body>\n",
                "</html>"), StandardCharsets.UTF_8);
    }

}
