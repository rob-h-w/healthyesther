package com.robwilliamson.healthyesther.unit.com.robwilliamson.healthyesther;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

import com.robwilliamson.healthyesther.BaseFragmentActivity;
import com.robwilliamson.healthyesther.EventActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseFragmentActivityTest extends ActivityUnitTestCase {

    public BaseFragmentActivityTest() {
        super(EventActivity.class);
    }

    public void testNotCreatedState() throws Exception {
        final EventActivity[] activity = new EventActivity[1];

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                activity[0] = new EventActivity();
            }
        });

        assertFalse(isActive(activity[0]));
    }

    public void testLaunchedState() throws Exception {
        final EventActivity activity = (EventActivity) getActivity();

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                activity.onResume();

                Throwable t = null;
                try {
                    assertTrue(isActive(activity));

                    onPause(activity);

                    assertFalse(isActive(activity));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    t = e;
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    t = e;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    t = e;
                }

                if (t != null) {
                    fail("This was thrown: " + t);
                }
            }
        });
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Intent.ACTION_MAIN), null, null);
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
