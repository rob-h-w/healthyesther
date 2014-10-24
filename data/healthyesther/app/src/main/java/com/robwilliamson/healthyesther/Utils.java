package com.robwilliamson.healthyesther;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public final class Utils {
    public static String format(Throwable e) {
        StringBuilder str = new StringBuilder();
        final String nl = "/n";
        str.append(e.getMessage()).append(nl);
        str.append(e.getClass()).append(nl);
        str.append(e.getStackTrace()).append(nl);

        if (e.getCause() != null) {
            str.append("Caused by:").append(nl);
            str.append(format(e.getCause()));
        }

        return str.toString();
    }

    public static final class View {

        public static class NonUiThreadException extends RuntimeException {}

        public interface RadioButtonHandler {
            /**
             * @param button
             * @return true to loop through the other buttons, false to exit with this one.
             */
            public boolean handleRadioButton(RadioButton button);
        }

        public static void forEachRadioButton(RadioGroup radioGroup, RadioButtonHandler handler) {
            final int count = radioGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                android.view.View view = radioGroup.getChildAt(i);
                if (!(view instanceof RadioButton)) {
                    continue;
                }

                if (!handler.handleRadioButton((RadioButton)view)) {
                    break;
                }
            }
        }

        @SuppressWarnings("unchecked")
        public static <T extends android.view.View> T getTypeSafeView(android.view.View parent, int id) {
            if (parent == null) {
                return null;
            }

            return (T)parent.findViewById(id);
        }

        @SuppressWarnings("unchecked")
        public static <T extends Fragment> T getTypeSafeFragment(FragmentManager manager, String tag) {
            if (manager == null) {
                return null;
            }

            return (T) manager.findFragmentByTag(tag);
        }

        public static void assertIsOnUiThread() {
            long appUiThread = App.getUiThreadId();
            Thread currentThread = Thread.currentThread();

            if (appUiThread == 0 && currentThread.getName().equals("main")) {
                App.setsUiThreadId(currentThread.getId());
            }

            if (App.getUiThreadId() != currentThread.getId()) {
                throw new NonUiThreadException();
            }
        }
    }
}
