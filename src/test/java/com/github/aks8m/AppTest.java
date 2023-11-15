package com.github.aks8m;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.SimpleFileServer;
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
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

public class AppTest {

    private static final Logger LOG = Logger.getLogger(AppTest.class.getSimpleName());

    private HttpServer server;

    public static void main(String[] args){
        AppTest appTest = new AppTest();
        appTest.startTestDependencies();
        TestExecutionSummary summary = appTest.discoverAndRunTests();
        appTest.logTestResults(summary);
//        appTest.stopTestDependencies();
    }

    public void startTestDependencies(){
        final InetSocketAddress address = new InetSocketAddress("0.0.0.0", 8080);
        server = SimpleFileServer.createFileServer(address, initializeHTML(), SimpleFileServer.OutputLevel.VERBOSE);
        server.start();
    }

    private Path initializeHTML(){
        Path root;
        try {
            FileSystem fileSystem = Jimfs.newFileSystem(Configuration.unix());
            root = fileSystem.getPath("/");
            Files.createDirectories(root.resolve("test/test2"));
            Path foo = fileSystem.getPath("/foo");
            Files.createDirectory(foo);

            Path hello = foo.resolve("hello.txt");
            Files.write(hello, List.of("hello", "world"), StandardCharsets.UTF_8);

        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        return root;
    }

    public void stopTestDependencies(){
        server.stop(0);
    }

    public TestExecutionSummary discoverAndRunTests(){
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(
                        selectPackage("com.github.aks8m.test"),
                        selectPackage("com.github.aks8m.it")
                )
                .build();

        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        try (LauncherSession session = LauncherFactory.openSession()) {
            Launcher launcher = session.getLauncher();
            launcher.registerTestExecutionListeners(listener);
            TestPlan testPlan = launcher.discover(request);
            launcher.execute(testPlan);
        }

        return listener.getSummary();
    }

    public void logTestResults(TestExecutionSummary summary){
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

}




