package com.robwilliamson.healthyesther.fragment.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import java.util.Calendar;

public class TimePicker extends FixedDialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    private static final String NAME = "timePicker";
    private OnTimeSetListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));

    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        mListener.onTimeSet(this, hourOfDay, minute);
    }

    public void show(android.support.v4.app.FragmentManager manager) {
        show(manager, NAME);
    }

    public void setTimeSetListener(OnTimeSetListener listener) {
        mListener = listener;
    }

    public interface OnTimeSetListener {
        public void onTimeSet(TimePicker sender, int hourOfDay, int minute);
    }

}
