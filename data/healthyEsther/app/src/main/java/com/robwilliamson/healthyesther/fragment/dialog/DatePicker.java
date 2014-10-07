package com.robwilliamson.healthyesther.fragment.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import com.robwilliamson.db.Utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class DatePicker extends FixedDialogFragment {
    private static final String NAME = "datePicker";
    private DatePickerDialog.OnDateSetListener mListener;
    private DateTime mDateTime;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mDateTime = Utils.Time.unBundle(savedInstanceState, TimePicker.class.getCanonicalName());
        }

        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(
                getActivity(),
                mListener,
                mDateTime.getYear(),
                mDateTime.getMonthOfYear() - 1,
                mDateTime.getDayOfMonth());

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Utils.Time.bundle(outState, DatePicker.class.getCanonicalName(), mDateTime);
    }

    public void show(android.support.v4.app.FragmentManager manager, DateTime initialTime) {
        mDateTime = initialTime.withZone(DateTimeZone.getDefault());
        show(manager, NAME);
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        mListener = listener;
    }
}
