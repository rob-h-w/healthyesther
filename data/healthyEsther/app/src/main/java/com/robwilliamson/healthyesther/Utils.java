package com.robwilliamson.healthyesther;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
    }
}
