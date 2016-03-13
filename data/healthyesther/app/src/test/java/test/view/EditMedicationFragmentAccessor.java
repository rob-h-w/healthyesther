package test.view;

import com.robwilliamson.healthyesther.R;

import javax.annotation.Nonnull;

import test.ActivityTestContext;

public class EditMedicationFragmentAccessor extends Accessor {
    public EditMedicationFragmentAccessor(@Nonnull ActivityTestContext context) {
        super(context);
    }

    public String getName() {
        return getText(R.id.medication_name);
    }

    public void setName(@Nonnull String name) {
        setText(name, R.id.medication_name);
    }
}
