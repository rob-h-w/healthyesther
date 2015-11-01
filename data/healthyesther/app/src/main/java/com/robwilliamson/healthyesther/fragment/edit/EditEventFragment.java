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
import com.robwilliamson.healthyesther.db.data.EventData;
import com.robwilliamson.healthyesther.db.definition.Event;
import com.robwilliamson.healthyesther.db.definition.Modification;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.EventTypeTable;
import com.robwilliamson.healthyesther.db.use.Query;
import com.robwilliamson.healthyesther.fragment.dialog.DatePickerFragment;
import com.robwilliamson.healthyesther.fragment.dialog.DateTimePickerListener;
import com.robwilliamson.healthyesther.fragment.dialog.TimePickerFragment;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Allows the user to edit an event's name and when properties.
 */
public class EditEventFragment extends EditFragment<EditEventFragment.Watcher> implements DateTimePickerListener {
    private EventData mEvent = new EventData();
    private boolean mUserEditedEventName;
    private EventTable.Row mRow;

    public EditEventFragment() {
        super(EditEventFragment.Watcher.class);
    }

    public EventTable.Row getRow() {
        if (mRow == null) {
            mRow = new EventTable.Row(
                    new EventTypeTable.PrimaryKey(mEvent.getTypeId()),
                    com.robwilliamson.healthyesther.db.includes.DateTime.from(mEvent.getCreated()),
                    com.robwilliamson.healthyesther.db.includes.DateTime.from(mEvent.getWhen()),
                    com.robwilliamson.healthyesther.db.includes.DateTime.from(mEvent.getModified()),
                    mEvent.getName());
        }

        return mRow;
    }

    public void setRow(@NonNull EventTable.Row row) {
        mRow = row;
        setName(row.getName());
        setWhen(row.getWhen().as(DateTime.class));
    }

    @Override
    public Query[] getQueries() {
        return new Query[0];
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null || !savedInstanceState.containsKey(Event.TABLE_NAME)) {
            final EventData[] event = new EventData[1];
            callWatcher(new WatcherCaller<Watcher>() {
                @Override
                public void call(@NonNull Watcher watcher) {
                    event[0] = watcher.getIntentEventData();
                }
            });

            if (event[0] == null) {
                mEvent.setWhen(Utils.Time.localNow());
            } else {
                mEvent = event[0];
                callWatcher(new WatcherCaller<Watcher>() {
                    @Override
                    public void call(@NonNull Watcher watcher) {
                        watcher.onUseIntentEventData(mEvent);
                    }
                });
            }
        } else {
            mEvent = EventData.from(savedInstanceState.getBundle(Event.TABLE_NAME), EventData.class);
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
                dialog.show(getFragmentManager(), mEvent.getWhen());
            }
        });

        getDateButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dialog = new DatePickerFragment();
                dialog.setListener(EditEventFragment.this);
                dialog.show(getFragmentManager(), mEvent.getWhen());
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
                updateWatcher();
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
        outState.putBundle(Event.TABLE_NAME, mEvent.asBundle());
    }

    @Override
    public void onDateTimeChange(DateTime dateTime) {
        mEvent.setWhen(dateTime);
        updateUi();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_edit_event;
    }

    @Override
    public Modification getModification() {
        if (mEvent.getCreated() == null) {
            mEvent.setCreated(Utils.Time.localNow());
        }

        mEvent.setModified(Utils.Time.localNow());
        return new Event.Modification(mEvent);
    }

    @Override
    public boolean validate() {
        return getName() == null || Event.validateName(getName());
    }

    @Override
    protected void updateWatcher(@NonNull Watcher watcher) {

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
        getNameView().getText().clear();
        getNameView().getText().append(name);
        mEvent.setName(name);
        updateUi();
    }

    public DateTime getWhen() {
        return mEvent.getWhen();
    }

    public void setWhen(DateTime when) {
        mEvent.setWhen(when);
        updateUi();
    }

    public boolean isEventNameEditedByUser() {
        return mUserEditedEventName;
    }

    private void updateUi() {
        if (mEvent != null) {
            if (getNameView() != null) {
                getNameView().getText().clear();
                if (mEvent.getName() != null) {
                    getNameView().getText().append(mEvent.getName());
                }
            }

            if (mEvent.getWhen() != null) {
                if (getTimeButton() != null) {
                    getTimeButton().setText(Utils.Time.toString(mEvent.getWhen(), DateTimeFormat.shortTime()));
                }

                if (getDateButton() != null) {
                    getDateButton().setText(Utils.Time.toString(mEvent.getWhen(), DateTimeFormat.mediumDate()));
                }
            }
        }

        updateWatcher();
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

    public interface Watcher {
        EventData getIntentEventData();

        void onUseIntentEventData(EventData eventData);
    }
}
