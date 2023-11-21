package com.github.aks8m;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.SimpleFileServer;
import org.apache.log4j.BasicConfigurator;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

public class AppTest {

    private static final Logger LOG = LoggerFactory.getLogger(AppTest.class.getSimpleName());
    private HttpServer httpServer;

    public static void main(String[] args){
        // Set up a simple configuration that logs on the console.
        AppTest appTest = new AppTest();
        appTest.beforeTests();
        appTest.runTests();
        appTest.afterTests();
    }

    /**
     * Configure and run the in-memory HTTP server with test website
     */
    void beforeTests(){
        InetSocketAddress address = new InetSocketAddress("0.0.0.0", 8080);

        try (FileSystem fileSystem = Jimfs.newFileSystem(Configuration.unix())) {
            FileSystem fs = Jimfs.newFileSystem(Configuration.unix());
            Path html = fs.getPath("/html");
            Files.createDirectory(html);
            createHTMLSite(html);
            httpServer = SimpleFileServer.createFileServer(address, html, SimpleFileServer.OutputLevel.NONE);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

        httpServer.start();
    }

    /**
     * Stop the in-memory HTTP server with test website
     */
    void afterTests(){
        httpServer.stop(0);
    }

    /**
     * Discover, Execute, and report all JUnit5 annotated tests with the following packages:
     *    1) com.github.aks8m.test - for unit tests
     *    2) com.github.aks8m.it - for integration tests
     */
    void runTests(){
        //Configure Test Discovery and Execute Tests
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(
                        selectPackage("com.github.aks8m.test"),
                        selectPackage("com.github.aks8m.it"))
                .build();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        try (LauncherSession session = LauncherFactory.openSession()) {
            Launcher launcher = session.getLauncher();
            launcher.registerTestExecutionListeners(listener);
            TestPlan testPlan = launcher.discover(request);
            launcher.execute(testPlan);
        }

        //Summarize JUnit5 Test Execution
        TestExecutionSummary summary = listener.getSummary();
        LOG.info(summary.getTestsStartedCount() + " of " + summary.getTestsFoundCount() + " Test Ran");
        LOG.info("Passed: " + summary.getTestsSucceededCount());
        LOG.info("Failed: " + summary.getTestsFailedCount());
        summary.getFailures().stream()
                .map(failure -> failure.getTestIdentifier().getDisplayName() + ": " + failure.getException().getMessage())
                .forEach(LOG::info);
        LOG.info("Aborted: " + summary.getTestsAbortedCount());
        LOG.info("Skipped: " + summary.getTestsSkippedCount());
        LOG.info("Total Time: " + Duration.between(Instant.ofEpochMilli(summary.getTimeStarted()), Instant.ofEpochMilli(summary.getTimeFinished())));
    }

    /**
     * Create the following HTML pages (as strings) using JIMFS library
     *
     *                 I
     *             A       B
     *    B <- C -   - D -   - E
     * @return - Index or Root Path object
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
                "</body>\n",
                "</html>"), StandardCharsets.UTF_8);
    }
}




