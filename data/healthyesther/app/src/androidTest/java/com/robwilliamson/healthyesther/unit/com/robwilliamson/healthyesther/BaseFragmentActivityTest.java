/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.unit.com.robwilliamson.healthyesther;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.robwilliamson.healthyesther.BaseFragmentActivity;
import com.robwilliamson.healthyesther.EventActivity;
import com.robwilliamson.healthyesther.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class BaseFragmentActivityTest {
    @Rule
    public ActivityTestRule<EventActivity> mActivityRule = new ActivityTestRule<>(
            EventActivity.class);

    @Test
    public void testNotCreatedState() throws Exception {
        final EventActivity[] activity = new EventActivity[1];

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                activity[0] = mActivityRule.getActivity();
            }
        });

        assertFalse(isActive(activity[0]));
    }

    @Test
    public void testLaunchedState() throws Exception {
        final EventActivity activity = mActivityRule.getActivity();

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                activity.onResume();

                Throwable t = null;
                try {
                    assertTrue(isActive(activity));

                    onPause(activity);

                    assertFalse(isActive(activity));
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                    t = e;
                }

                if (t != null) {
                    fail("This was thrown: " + t);
                }
            }
        });
    }

    @Before
    public void setUp() throws Exception {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
                context.setTheme(R.style.HealthyEstherTheme);
                mActivityRule.getActivity().startActivity(new Intent(Intent.ACTION_MAIN), null);
            }
        });
    }

    private boolean isActive(EventActivity activity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = BaseFragmentActivity.class.getDeclaredMethod("isActive");
        method.setAccessible(true);
        return (boolean) method.invoke(activity);
    }

    private void onPause(EventActivity activity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = BaseFragmentActivity.class.getDeclaredMethod("onPause");
        method.setAccessible(true);
        method.invoke(activity);
    }
}
