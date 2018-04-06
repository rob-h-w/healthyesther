/**
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.fragment.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

public class TimePickerFragment extends AbstractDateTimePickerFragment
        implements TimePickerDialog.OnTimeSetListener {
    public TimePickerFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        restoreDialog(savedInstanceState);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(
                getActivity(),
                this,
                getLocalDateTime().getHour(),
                getLocalDateTime().getMinute(),
                DateFormat.is24HourFormat(getActivity()));

    }

    @Override
    protected String getName() {
        return "timePicker";
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        setDateTime(getLocalDateTime()
                .withHour(hourOfDay)
                .withMinute(minute)
                .withSecond(0)
                .withNano(0));
        callCallback();
    }
}
