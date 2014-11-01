package com.robwilliamson.healthyesther.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.pm.ActivityInfo;
import android.test.InstrumentationTestCase;
import android.view.View;

import com.google.android.apps.common.testing.testrunner.ActivityLifecycleMonitor;
import com.google.android.apps.common.testing.testrunner.GoogleInstrumentationTestRunner;
import com.google.android.apps.common.testing.testrunner.Stage;

import java.util.Collection;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDisplayed;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;

public class Orientation {
    public static interface Subject {
        InstrumentationTestCase getTestCase();
        void checkContent();
    }

    public static void check(Subject subject) {
        setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, subject);
        subject.checkContent();
        setOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, subject);
        subject.checkContent();
    }

    private static void setOrientation(int orientation, Subject subject) {
        final Activity initial = Espresso.waitForActivityToResume(subject.getTestCase());
        final int initialOrientation = initial.getRequestedOrientation();

        // Only set the orientation if necessary.
        if (initialOrientation != orientation) {
            initial.setRequestedOrientation(orientation);
        }
    }
}
