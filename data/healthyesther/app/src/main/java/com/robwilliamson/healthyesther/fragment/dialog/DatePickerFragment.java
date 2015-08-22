package com.robwilliamson.healthyesther.fragment.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

public class DatePickerFragment extends AbstractDateTimePickerFragment
        implements DatePickerDialog.OnDateSetListener {
    public DatePickerFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        restoreDialog(savedInstanceState);

        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(
                getActivity(),
                this,
                getLocalDateTime().getYear(),
                getLocalDateTime().getMonthOfYear() - 1,
                getLocalDateTime().getDayOfMonth());

    }

    @Override
    protected String getName() {
        return "datePicker";
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        setDateTime(getLocalDateTime().withDate(year, monthOfYear + 1, dayOfMonth));
        callCallback();
    }
}
