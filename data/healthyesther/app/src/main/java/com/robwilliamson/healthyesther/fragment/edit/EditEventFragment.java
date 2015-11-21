package com.robwilliamson.healthyesther.fragment.edit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.fragment.dialog.DatePickerFragment;
import com.robwilliamson.healthyesther.fragment.dialog.DateTimePickerListener;
import com.robwilliamson.healthyesther.fragment.dialog.TimePickerFragment;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import javax.annotation.Nonnull;

/**
 * Allows the user to edit an event's name and when properties.
 */
public class EditEventFragment extends EditFragment<EventTable.Row>
        implements DateTimePickerListener {
    private boolean mUserEditedEventName;

    public static EditEventFragment getInstance(@Nonnull EventTable.Row row) {
        EditEventFragment fragment = new EditEventFragment();
        fragment.setRow(row);
        return fragment;
    }

    @Override
    public void setRow(@NonNull EventTable.Row row) {
        super.setRow(row);
        setName(row.getName());
        setWhen(row.getWhen().as(DateTime.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(EventTable.NAME)) {
            EventTable.Row row = (EventTable.Row) savedInstanceState.getSerializable(EventTable.NAME);
            if (row != null) {
                setRow(row);
            }
        }

        updateUi();
    }

    @Override
    public void onResume() {
        super.onResume();

        getTimeButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment dialog = new TimePickerFragment();
                dialog.setListener(EditEventFragment.this);
                dialog.show(getFragmentManager(), getWhen());
            }
        });

        getDateButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dialog = new DatePickerFragment();
                dialog.setListener(EditEventFragment.this);
                dialog.show(getFragmentManager(), getWhen());
            }
        });

        getNameView().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mUserEditedEventName = !getName().isEmpty();
            }
        });

        getNameView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        updateUi();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EventTable.NAME, getRow());
    }

    @Override
    public void onDateTimeChange(DateTime dateTime) {
        if (getRow() != null) {
            getRow().setWhen(com.robwilliamson.healthyesther.db.includes.DateTime.from(dateTime));
        }
        updateUi();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_edit_event;
    }

    @Override
    public boolean validate() {
        // TODO
        return true;
    }

    @Override
    protected EventTable.Row createRow() {
        throw new UnsupportedOperationException("The event has to be set by the owning activity.");
    }

    public void suggestEventName(String name) {

        if (mUserEditedEventName && !getName().isEmpty()) {
            return;
        }

        mUserEditedEventName = false;

        setName(name);
    }

    public String getName() {
        return getNameView().getText().toString();
    }

    public void setName(String name) {
        AutoCompleteTextView nameView = getNameView();

        if (nameView != null) {
            getNameView().getText().clear();
            getNameView().getText().append(name);
        }

        if (getRow() != null) {
            getRow().setName(name);
        }
        updateUi();
    }

    public DateTime getWhen() {
        if (hasRow()) {
            //noinspection ConstantConditions
            return getRow().getWhen().as(DateTime.class);
        }

        return null;
    }

    public void setWhen(@Nonnull DateTime when) {
        if (hasRow()) {
            //noinspection ConstantConditions
            getRow().setWhen(com.robwilliamson.healthyesther.db.includes.DateTime.from(when));
        }
        updateUi();
    }

    public boolean isEventNameEditedByUser() {
        return mUserEditedEventName;
    }

    private void updateUi() {
        if (hasRow()) {
            @SuppressWarnings("ConstantConditions") @Nonnull EventTable.Row row = getRow();
            if (getNameView() != null) {
                getNameView().getText().clear();
                if (row.getName() != null) {
                    getNameView().getText().append(row.getName());
                }
            }

            if (getTimeButton() != null) {
                getTimeButton().setText(Utils.Time.toString(row.getWhen().as(DateTime.class), DateTimeFormat.shortTime()));
            }

            if (getDateButton() != null) {
                getDateButton().setText(Utils.Time.toString(row.getWhen().as(DateTime.class), DateTimeFormat.mediumDate()));
            }
        }
    }

    private Button getTimeButton() {
        return getButton(R.id.edit_event_time_button);
    }

    private Button getDateButton() {
        return getButton(R.id.edit_event_date_button);
    }

    private Button getButton(int id) {
        return getTypeSafeView(id, Button.class);
    }

    private AutoCompleteTextView getNameView() {
        return getTypeSafeView(R.id.edit_event_name, AutoCompleteTextView.class);
    }
}
