package com.robwilliamson.healthyesther;

import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.robwilliamson.db.Contract;

import java.io.File;

public final class Utils {
    public static final class View {

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
