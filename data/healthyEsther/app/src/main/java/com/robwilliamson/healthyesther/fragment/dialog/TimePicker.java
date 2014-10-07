package com.robwilliamson.healthyesther.fragment.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import com.robwilliamson.db.Utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class TimePicker extends FixedDialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    private static final String NAME = "timePicker";
    private OnTimeSetListener mListener;
    private DateTime mDateTime;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mDateTime = Utils.Time.unBundle(savedInstanceState, TimePicker.class.getCanonicalName());
        }

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, mDateTime.getHourOfDay(), mDateTime.getMinuteOfHour(),
                DateFormat.is24HourFormat(getActivity()));

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Utils.Time.bundle(outState, TimePicker.class.getCanonicalName(), mDateTime);
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        mListener.onTimeSet(this, hourOfDay, minute);
    }

    public void show(android.support.v4.app.FragmentManager manager, DateTime initialTime) {
        mDateTime = initialTime.withZone(DateTimeZone.getDefault());
        show(manager, NAME);
    }

    public void setTimeSetListener(OnTimeSetListener listener) {
        mListener = listener;
    }

    public interface OnTimeSetListener {
        public void onTimeSet(TimePicker sender, int hourOfDay, int minute);
    }
}
