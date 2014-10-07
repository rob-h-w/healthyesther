package com.robwilliamson.healthyesther.fragment.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import com.robwilliamson.db.Utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class TimePicker extends FixedDialogFragment {
    private static final String NAME = "timePicker";
    private TimePickerDialog.OnTimeSetListener mListener;
    private DateTime mDateTime;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mDateTime = Utils.Time.unBundle(savedInstanceState, TimePicker.class.getCanonicalName());
        }

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), mListener, mDateTime.getHourOfDay(), mDateTime.getMinuteOfHour(),
                DateFormat.is24HourFormat(getActivity()));

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Utils.Time.bundle(outState, TimePicker.class.getCanonicalName(), mDateTime);
    }

    public void show(android.support.v4.app.FragmentManager manager, DateTime initialTime) {
        mDateTime = initialTime.withZone(DateTimeZone.getDefault());
        show(manager, NAME);
    }

    public void setListener(TimePickerDialog.OnTimeSetListener listener) {
        mListener = listener;
    }
}
