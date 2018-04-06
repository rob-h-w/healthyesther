/**
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.fragment.dialog;

import android.os.Bundle;

import com.robwilliamson.healthyesther.db.Utils;

import java.time.ZonedDateTime;
import java.util.TimeZone;

public abstract class AbstractDateTimePickerFragment extends FixedDialogFragment {
    private DateTimePickerListener mListener;
    private ZonedDateTime mDateTime;

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

    public void show(android.support.v4.app.FragmentManager manager, ZonedDateTime initialTime) {
        setDateTime(initialTime);
        show(manager, getName());
    }

    protected ZonedDateTime getLocalDateTime() {
        return mDateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        mDateTime = dateTime;
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
