/**
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
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
                getLocalDateTime().getMonthValue() - 1,
                getLocalDateTime().getDayOfMonth());

    }

    @Override
    protected String getName() {
        return "datePicker";
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        setDateTime(getLocalDateTime()
                .withYear(year)
                .withMonth(monthOfYear)
                .withDayOfMonth(dayOfMonth));
        callCallback();
    }
}
