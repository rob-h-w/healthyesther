package com.robwilliamson.healthyesther.fragment.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

public class TimePicker extends AbstractDateTimePicker
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        restoreDialog(savedInstanceState);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(
                getActivity(),
                this,
                getLocalDateTime().getHourOfDay(),
                getLocalDateTime().getMinuteOfHour(),
                DateFormat.is24HourFormat(getActivity()));

    }

    @Override
    protected String getName() {
        return "timePicker";
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        setDateTime(getLocalDateTime().withTime(hourOfDay, minute, 0, 0));
        callCallback();
    }
}
