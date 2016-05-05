/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.HashMap;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Utils {
    public static String format(@Nonnull Throwable e) {
        StringBuilder str = new StringBuilder();
        final String nl = "/n";
        str.append(e.getMessage()).append(nl);
        str.append(e.getClass()).append(nl);
        for (StackTraceElement element : e.getStackTrace()) {
            str.append(element).append(nl);
        }

        if (e.getCause() != null) {
            str.append("Caused by:").append(nl);
            str.append(format(e.getCause()));
        }

        return str.toString();
    }

    @Nonnull
    public static <T> T checkNotNull(@Nullable T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }

        return obj;
    }

    @SuppressWarnings("unchecked")
    public static <T> T checkAssignable(@Nullable Object obj, @Nonnull Class<T> type) {
        if (obj == null) {
            return null;
        }

        if (!type.isAssignableFrom(obj.getClass())) {
            throw new ClassCastException(obj.toString() + " must be assignable to " + type.getCanonicalName());
        }

        return (T) obj;
    }

    public static <T> T checkedCast(@Nullable Object obj, @Nonnull Class<T> type) {
        return checkAssignable(obj, type);
    }

    public static final class View {

        public static void forEachRadioButton(@Nonnull RadioGroup radioGroup, @Nonnull RadioButtonHandler handler) {
            final int count = radioGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                android.view.View view = radioGroup.getChildAt(i);
                if (!(view instanceof RadioButton)) {
                    continue;
                }

                if (!handler.handleRadioButton((RadioButton) view)) {
                    break;
                }
            }
        }

        @Nullable
        public static <T extends android.view.View> T getTypeSafeView(
                @Nonnull android.view.View parent,
                int id,
                @Nonnull Class<T> type) {
            return checkedCast(parent.findViewById(id), type);
        }

        @Nullable
        public static <T extends Fragment> T getTypeSafeFragment(
                @Nonnull FragmentManager manager,
                @Nonnull String tag,
                @Nonnull Class<T> type) {
            return checkedCast(manager.findFragmentByTag(tag), type);
        }

        @Nullable
        public static <T extends Fragment> T getTypeSafeFragment(
                @Nonnull FragmentManager manager,
                int id,
                @Nonnull Class<T> type) {
            return checkedCast(manager.findFragmentById(id), type);
        }

        public static void assertIsOnUiThread() {
            assertIsOnUiThread(Thread.currentThread());
        }

        public static void assertIsOnUiThread(Thread thread) {
            long appUiThread = App.getUiThreadId();

            if (appUiThread == 0 && thread.getName().equals("main")) {
                App.setsUiThreadId(thread.getId());
            }

            if (App.getUiThreadId() != thread.getId()) {
                throw new NonUiThreadException();
            }
        }

        public interface RadioButtonHandler {
            /**
             * @param button The radio button to handle next.
             * @return true to loop through the other buttons, false to exit with this one.
             */
            boolean handleRadioButton(RadioButton button);
        }

        public static class NonUiThreadException extends RuntimeException {
        }
    }

    public static final class Bundles {
        public static <V> void put(@Nonnull Bundle bundle, @Nonnull String bundleKey, @Nonnull HashMap<String, V> map, @Nonnull HashPutter putter) {
            String[] keys = new String[map.keySet().size()];
            map.keySet().toArray(keys);

            bundle.putStringArray(keysName(bundleKey), keys);

            for (String k : keys) {
                putter.put(bundle, valueName(bundleKey, k), k);
            }
        }

        @Nullable
        public static <V> HashMap<String, V> get(Bundle bundle, String bundleKey, HashGetter<V> getter) {
            Set<String> keySet = bundle.keySet();
            if (!keySet.contains(keysName(bundleKey))) {
                return null;
            }

            String[] keys = bundle.getStringArray(keysName(bundleKey));

            if (keys == null) {
                return null;
            }

            HashMap<String, V> map = new HashMap<>(keys.length);

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

        public interface HashPutter {
            void put(Bundle bundle, String bundleKey, String key);
        }

        public interface HashGetter<T> {
            T get(Bundle bundle, String bundleKey);
        }
    }
}
