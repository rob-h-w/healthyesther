package test.view;

import android.app.Activity;

import com.robwilliamson.healthyesther.R;

import javax.annotation.Nonnull;

public class EditMedicationFragmentAccessor extends Accessor {
    public EditMedicationFragmentAccessor(@Nonnull Activity activity) {
        super(activity);
    }

    public String getName() {
        return getText(R.id.medication_name);
    }

    public void setName(@Nonnull String name) {
        setText(name, R.id.medication_name);
    }
}
