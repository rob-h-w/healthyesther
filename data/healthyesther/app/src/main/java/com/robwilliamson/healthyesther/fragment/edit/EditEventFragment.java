package com.robwilliamson.healthyesther.fragment.edit;

import android.os.Bundle;
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
import com.robwilliamson.healthyesther.db.use.Query;
import com.robwilliamson.healthyesther.fragment.dialog.DatePickerFragment;
import com.robwilliamson.healthyesther.fragment.dialog.DateTimePickerListener;
import com.robwilliamson.healthyesther.fragment.dialog.TimePickerFragment;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Allows the user to edit an event's name and when properties.
 */
public class EditEventFragment extends EditFragment <EditEventFragment.Watcher> implements DateTimePickerListener {
    private EventData mEvent = new EventData();
    private boolean mUserEditedEventName;

    @Override
    public Query[] getQueries() {
        return new Query[0];
    }

    public interface Watcher {
        EventData getIntentEventData();
        void onUseIntentEventData(EventData eventData);
    }

    public EditEventFragment() {
        super(EditEventFragment.Watcher.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null || !savedInstanceState.containsKey(Event.TABLE_NAME)) {
            final EventData[] event = new EventData[1];
            callWatcher(new WatcherCaller<Watcher>() {
                @Override
                public void call(Watcher watcher) {
                    event[0] = watcher.getIntentEventData();
                }
            });

            if (event[0] == null) {
                mEvent.when = Utils.Time.localNow();
            } else {
                mEvent = event[0];
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
                dialog.show(getFragmentManager(), mEvent.when);
            }
        });

        getDateButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dialog = new DatePickerFragment();
                dialog.setListener(EditEventFragment.this);
                dialog.show(getFragmentManager(), mEvent.when);
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
        mEvent.when = dateTime;
        updateUi();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_edit_event;
    }

    @Override
    public Modification getModification() {
        if (mEvent.created == null) {
            mEvent.created = Utils.Time.localNow();
        }

        mEvent.modified = Utils.Time.localNow();
        return new Event.Modification(mEvent);
    }

    @Override
    public boolean validate() {
        return getName() == null || Event.validateName(getName());
    }

    @Override
    protected void updateWatcher(Watcher watcher) {

    }

    public void suggestEventName(String name) {

        if (mUserEditedEventName && !getName().isEmpty()) {
            return;
        }

        mUserEditedEventName = false;

        setName(name);
    }

    public void setName(String name) {
        getNameView().getText().clear();
        getNameView().getText().append(name);
        mEvent.name = name;
        updateUi();
    }

    public String getName() {
        return getNameView().getText().toString();
    }

    public void setWhen(DateTime when) {
        mEvent.when = when;
        updateUi();
    }

    public DateTime getWhen() {
        return mEvent.when;
    }


    public boolean isEventNameEditedByUser() {
        return mUserEditedEventName;
    }

    private void updateUi() {
        if (mEvent != null) {
            if (getNameView() != null) {
                getNameView().getText().clear();
                getNameView().getText().append(mEvent.name);
            }

            if (mEvent.when != null) {
                if (getTimeButton() != null) {
                    getTimeButton().setText(Utils.Time.toString(mEvent.when, DateTimeFormat.shortTime()));
                }

                if (getDateButton() != null) {
                    getDateButton().setText(Utils.Time.toString(mEvent.when, DateTimeFormat.mediumDate()));
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
}
