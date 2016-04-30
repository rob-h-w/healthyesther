package com.robwilliamson.healthyesther.fragment.dialog;

import android.os.Bundle;

import com.robwilliamson.healthyesther.db.Utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.TimeZone;

public abstract class AbstractDateTimePickerFragment extends FixedDialogFragment {
    private DateTimePickerListener mListener;
    private DateTime mDateTime;

    protected void restoreDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mDateTime = Utils.Time.unBundle(savedInstanceState, getClass().getCanonicalName());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Utils.Time.bundle(outState, getClass().getCanonicalName(), mDateTime);
    }

    public void show(android.support.v4.app.FragmentManager manager, DateTime initialTime) {
        setDateTime(initialTime);
        show(manager, getName());
    }

    protected DateTime getLocalDateTime() {
        return mDateTime;
    }

    public void setDateTime(DateTime dateTime) {
        mDateTime = dateTime.withZone(DateTimeZone.forTimeZone(TimeZone.getDefault()));
    }

    protected abstract String getName();

    protected DateTimePickerListener getListener() {
        return mListener;
    }

    public void setListener(DateTimePickerListener listener) {
        mListener = listener;
    }

    protected void callCallback() {
        getListener().onDateTimeChange(getLocalDateTime());
    }
}
