package com.robwilliamson.healthyesther.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.runner.AndroidJUnitRunner;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitor;
import android.support.test.runner.lifecycle.Stage;
import android.test.InstrumentationTestCase;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Espresso {
    static class Both<T> extends BaseMatcher<T> {
        private final Matcher<T> mLeft;
        private final Matcher<T> mRight;

        public Both(Matcher<T> left, Matcher<T> right) {
            mLeft = left;
            mRight = right;
        }

        @Override
        public boolean matches(Object o) {
            try {
                return mLeft.matches(o) && mRight.matches(o);
            } catch (ClassCastException e) {
                return false;
            }
        }

        @Override
        public void describeTo(Description description) {
            description.appendDescriptionOf(mLeft);
            description.appendDescriptionOf(mRight);
        }
    }

    public static <T> Matcher<T> both(Matcher<T> left, Matcher<T> right) {
        return new Both<T>(left, right);
    }

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
        final ActivityLifecycleMonitor monitor =  monitor((AndroidJUnitRunner) instrumentation);
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

    private static ActivityLifecycleMonitor monitor(AndroidJUnitRunner instrumentation) {
        Field lifecycleMonitor;
        try {
            lifecycleMonitor = android.support.test.runner.MonitoringInstrumentation.class.getDeclaredField("mLifecycleMonitor");
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
