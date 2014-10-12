package com.robwilliamson.healthyesther;

import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.robwilliamson.db.Contract;

import java.io.File;

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
            if (App.getUiThreadId() != Thread.currentThread().getId()) {
                throw new NonUiThreadException();
            }
        }
    }

    public static class Dropbox {
        private static final String COM_DROPBOX_ANDROID_FILES_SCRATCH = "com.dropbox.android/files/scratch";
        private static final String COM_ROBWILLIAMSON_HEALTHYESTHER = "com.robwilliamson.healthyesther";

        public static boolean isDbFileInDropboxAppFolder() {
            return com.robwilliamson.db.Utils.File.exists(dbFile());
        }

        public static boolean isAppFolderInDropbox() {
            return com.robwilliamson.db.Utils.File.exists(dbFolder());
        }

        public static boolean isDropboxPresent() {
            return com.robwilliamson.db.Utils.File.exists(folder());
        }

        private static String dbFile() {
            return com.robwilliamson.db.Utils.File.join(
                    dbFolder(),
                    Contract.NAME);
        }

        private static String dbFolder() {
            return com.robwilliamson.db.Utils.File.join(
                    folder(),
                    COM_ROBWILLIAMSON_HEALTHYESTHER);
        }

        private static String folder() {
            return com.robwilliamson.db.Utils.File.join(
                    Environment.getDataDirectory().getAbsolutePath(),
                    COM_DROPBOX_ANDROID_FILES_SCRATCH);
        }
    }
}
