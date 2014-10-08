package com.robwilliamson.healthyesther.fragment.dialog;

import android.os.Bundle;

import com.robwilliamson.db.Utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

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

    public void setListener(DateTimePickerListener listener) {
        mListener = listener;
    }

    protected DateTime getLocalDateTime() {
        return mDateTime;
    }

    public void setDateTime(DateTime dateTime) {
        mDateTime = dateTime.withZone(DateTimeZone.getDefault());
    }

    protected abstract String getName();

    protected DateTimePickerListener getListener() {
        return mListener;
    }

    protected void callCallback() {
        getListener().onDateTimeChange(getLocalDateTime().withZone(DateTimeZone.UTC));
    }
}
