package com.robwilliamson.healthyesther.test;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.test.InstrumentationTestCase;

public class Orientation {
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

    public static interface Subject {
        InstrumentationTestCase getTestCase();

        void checkContent();
    }
}
