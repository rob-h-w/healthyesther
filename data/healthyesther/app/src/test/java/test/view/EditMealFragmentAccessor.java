/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package test.view;

import android.widget.AutoCompleteTextView;

import com.robwilliamson.healthyesther.R;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import test.ActivityTestContext;

public class EditMealFragmentAccessor extends Accessor {
    public EditMealFragmentAccessor(@Nonnull ActivityTestContext context) {
        super(context);
    }

    @Nullable
    public String getName() {
        return getText(R.id.meal_name);
    }

    @Nullable
    public AutoCompleteTextView getNameTextView() {
        return (AutoCompleteTextView) getActivity().findViewById(R.id.meal_name);
    }
}
