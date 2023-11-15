package com.github.aks8m;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

public class AppTest {

    private static final Logger LOG = Logger.getLogger(AppTest.class.getSimpleName());

    public static void main(String[] args){
        AppTest appTest = new AppTest();
        TestExecutionSummary summary = appTest.discoverAndRunTests();
        appTest.logTestResults(summary);
    }

    public void startTestDependencies(){

    }

    public void stopTestDependencies(){}

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




