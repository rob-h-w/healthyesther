/**
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.test;

import android.view.View;

import com.robwilliamson.healthyesther.R;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class MedicationEventActivityAccessor {

    public static Matcher<View> medTitle() {
        return withId(R.id.medication_name_title);
    }

    public static Matcher<View> medName() {
        return withId(R.id.medication_name);
    }
}
