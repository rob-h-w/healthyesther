package com.robwilliamson.healthyesther;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.HashMap;
import java.util.Set;

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

    @SuppressWarnings("unchecked")
    public static <T> T uncheckedCast(Object obj) {
        return (T) obj;
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

        public static <T extends android.view.View> T getTypeSafeView(android.view.View parent, int id) {
            if (parent == null) {
                return null;
            }

            return uncheckedCast(parent.findViewById(id));
        }

        public static <T extends Fragment> T getTypeSafeFragment(FragmentManager manager, String tag) {
            if (manager == null) {
                return null;
            }

            return uncheckedCast(manager.findFragmentByTag(tag));
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

    public static final class Bundles {
        public interface HashPutter {
            public void put(Bundle bundle, String bundleKey, String key);
        }

        public interface HashGetter <T> {
            public T get(Bundle bundle, String bundleKey);
        }

        public static <V> void put(Bundle bundle, String bundleKey, HashMap<String, V> map, HashPutter putter) {
            String [] keys = new String [map.keySet().size()];
            map.keySet().toArray(keys);

            bundle.putStringArray(keysName(bundleKey), keys);

            for (String k : keys) {
                putter.put(bundle, valueName(bundleKey, k), k);
            }
        }

        public static <V> HashMap<String, V> get(Bundle bundle, String bundleKey, HashGetter<V> getter) {
            Set<String> keySet = bundle.keySet();
            if (!keySet.contains(keysName(bundleKey))) {
                return null;
            }

            String [] keys = bundle.getStringArray(keysName(bundleKey));
            HashMap<String, V> map = new HashMap<String, V>(keys.length);

            for (String k : keys) {
                map.put(k, getter.get(bundle, valueName(bundleKey, k)));
            }

            return map;
        }

        private static String keysName(String key) {
            return key + "Keys";
        }

        private static String valueName(String bundleKey, String key) {
            return bundleKey + "Value_" + key;
        }
    }
}
