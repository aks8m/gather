package com.github.aks8m;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.SimpleFileServer;
import org.checkerframework.checker.units.qual.A;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

public class AppTest {

    private static final Logger LOG = Logger.getLogger(AppTest.class.getSimpleName());
    private HttpServer httpServer;

    public static void main(String[] args){
        AppTest appTest = new AppTest();

        appTest.beforeTests();
        appTest.runTests();
        appTest.afterTests();
    }

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

    void afterTests(){
        httpServer.stop(0);
    }

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
        StringBuilder junitSummary = new StringBuilder();
        junitSummary.append(summary.getTestsStartedCount() + " of " + summary.getTestsFoundCount() + " Test Ran\n");
        junitSummary.append("Passed: " + summary.getTestsSucceededCount() + "\n");
        junitSummary.append("Failed: " + summary.getTestsFailedCount() + "\n");
        summary.getFailures().stream()
                .map(failure ->  failure.getTestIdentifier().getSource().get().toString())
                .forEach(failureString -> junitSummary.append("\t" + failureString + "\n"));
        junitSummary.append("Aborted: " + summary.getTestsAbortedCount() + "\n");
        junitSummary.append("Skipped: " + summary.getTestsSkippedCount() + "\n");
        junitSummary.append("Total Time: " +
                Duration.between(Instant.ofEpochMilli(summary.getTimeStarted()), Instant.ofEpochMilli(summary.getTimeFinished())));
        LOG.info(junitSummary.toString());
    }

    /**
     *                 I
     *             A       B
     *    B <- C -   - D -   - E -> I
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
                "<a href=\"a.html\">A</a>\n",
                "<br>\n",
                "<a href=\"b.html\">B</a>\n",
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
                "<a href=\"c.html\">C</a>\n",
                "<br>\n",
                "<a href=\"d.html\">D</a>\n",
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
                "<a href=\"d.html\">D</a>\n",
                "<br>\n",
                "<a href=\"e.html\">E</a>\n",
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
                "<a href=\"b.html\">B</a>\n",
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
                "<a href=\"index.html\">Index</a>\n",
                "</body>\n",
                "</html>"), StandardCharsets.UTF_8);
    }
}




