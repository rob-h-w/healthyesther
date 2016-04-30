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
import javax.annotation.Nullable;

/**
 * Allows the user to edit an event's name and when properties.
 */
public class EditEventFragment extends EditFragment<EventTable.Row>
        implements DateTimePickerListener {
    private boolean mUserEditedEventName;
    private boolean mSettingRowName;
    private boolean mRowNameSet;

    public static EditEventFragment getInstance(@Nonnull EventTable.Row row) {
        EditEventFragment fragment = new EditEventFragment();
        fragment.setRow(row);
        return fragment;
    }

    @Override
    public void setRow(@NonNull EventTable.Row row) {
        super.setRow(row);
        mRowNameSet = !Utils.Strings.nullOrEmpty(row.getName());
        updateUi();
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
                mRowNameSet = false;
            }
        });

        getNameView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mSettingRowName) {
                    return;
                }
                updateRowName(s.toString());
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
    protected EventTable.Row createRow() {
        throw new UnsupportedOperationException("The event has to be set by the owning activity.");
    }

    public void suggestEventName(String name) {

        if ((!getName().isEmpty() && mUserEditedEventName) || mRowNameSet) {
            return;
        }

        mUserEditedEventName = false;

        setName(name);
    }

    public String getName() {
        return getNameView().getText().toString();
    }

    public void setName(@Nullable String name) {
        updateRowName(name);

        updateUi();
    }

    private void updateRowName(@Nullable String name) {
        if (getRow() != null) {
            getRow().setName(name);
        }
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
        mSettingRowName = true;
        if (hasRow()) {
            @SuppressWarnings("ConstantConditions") @Nonnull EventTable.Row row = getRow();
            if (getNameView() != null) {
                String rowName = row.getName();
                Editable editable = getNameView().getEditableText();

                editable.clear();
                if (rowName != null) {
                    editable.append(rowName);
                }
            }

            if (getTimeButton() != null) {
                getTimeButton().setText(Utils.Time.toString(row.getWhen().as(DateTime.class), DateTimeFormat.shortTime()));
            }

            if (getDateButton() != null) {
                getDateButton().setText(Utils.Time.toString(row.getWhen().as(DateTime.class), DateTimeFormat.mediumDate()));
            }
        }
        mSettingRowName = false;
    }

    protected Button getTimeButton() {
        return getButton(R.id.edit_event_time_button);
    }

    protected Button getDateButton() {
        return getButton(R.id.edit_event_date_button);
    }

    protected Button getButton(int id) {
        return getTypeSafeView(id, Button.class);
    }

    protected AutoCompleteTextView getNameView() {
        return getTypeSafeView(R.id.edit_event_name, AutoCompleteTextView.class);
    }
}
