package com.robwilliamson.healthyesther.test;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import javax.annotation.Nonnull;

public class Orientation {
    public static void check(Subject subject) {
        setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, subject);
        subject.checkContent();
        setOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, subject);
        subject.checkContent();
    }

    private static void setOrientation(int orientation, Subject subject) {
        final Activity initial = Espresso.waitForActivityToResume(
                InstrumentationRegistry.getInstrumentation(),
                subject.getActivityTestRule());
        final int initialOrientation = initial.getRequestedOrientation();

        // Only set the orientation if necessary.
        if (initialOrientation != orientation) {
            initial.setRequestedOrientation(orientation);
        }
    }

    public interface Subject {
        void checkContent();

        @Nonnull
        ActivityTestRule getActivityTestRule();
    }
}
