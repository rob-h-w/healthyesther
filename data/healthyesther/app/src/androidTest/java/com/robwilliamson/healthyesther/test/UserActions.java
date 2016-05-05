/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;

public class UserActions {
    public static void openAddHealthScore() {
        HomeActivityAccessor.AddMode.start();
        onView(HomeActivityAccessor.AddMode.healthScoreButton()).perform(click());
    }

    public static void openAddMeal() {
        HomeActivityAccessor.AddMode.start();
        onView(HomeActivityAccessor.AddMode.mealScoreButton()).perform(click());
    }
}
