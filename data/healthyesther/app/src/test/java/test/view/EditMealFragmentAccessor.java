package test.view;

import android.app.Activity;

import com.robwilliamson.healthyesther.R;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EditMealFragmentAccessor extends Accessor {
    public EditMealFragmentAccessor(@Nonnull Activity activity) {
        super(activity);
    }

    @Nullable
    public String getName() {
        return getText(R.id.meal_name);
    }
}
