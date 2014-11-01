package com.robwilliamson.healthyesther.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.InstrumentationTestCase;

import com.google.android.apps.common.testing.testrunner.ActivityLifecycleMonitor;
import com.google.android.apps.common.testing.testrunner.GoogleInstrumentation;
import com.google.android.apps.common.testing.testrunner.GoogleInstrumentationTestRunner;
import com.google.android.apps.common.testing.testrunner.Stage;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Espresso {
    public static Activity waitForActivityToResume(InstrumentationTestCase testCase) {
        Collection<Activity> resumed = getResumed(testCase);
        while(resumed.isEmpty()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            resumed = getResumed(testCase);
        }

        return (resumed.iterator().next());
    }

    private static Collection<Activity> getResumed(InstrumentationTestCase testCase) {
        final Instrumentation instrumentation = testCase.getInstrumentation();
        final ActivityLifecycleMonitor monitor =  monitor((GoogleInstrumentationTestRunner) instrumentation);
        final CountDownLatch latch = new CountDownLatch(1);
        final Object[] resumed = { null };

        try {
            testCase.runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    resumed[0] = monitor.getActivitiesInStage(Stage.RESUMED);
                    latch.countDown();
                }
            });
        } catch (Throwable throwable) {
            return null;
        }

        try {
            latch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return (Collection<Activity>) resumed[0];
    }

    private static ActivityLifecycleMonitor monitor(GoogleInstrumentationTestRunner instrumentation) {
        Field lifecycleMonitor;
        try {
            lifecycleMonitor = GoogleInstrumentation.class.getDeclaredField("lifecycleMonitor");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        lifecycleMonitor.setAccessible(true);
        try {
            return (ActivityLifecycleMonitor)lifecycleMonitor.get(instrumentation);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
