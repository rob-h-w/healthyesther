package com.robwilliamson.healthyesther.fragment.edit;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.Contract;
import com.robwilliamson.healthyesther.db.Utils;
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
    private long mId = -1;
    private DateTime mWhen;
    private boolean mUserEditedEventName;

    @Override
    public Query[] getQueries() {
        return new Query[0];
    }

    public interface Watcher {
        void onFragmentUpdate(EditEventFragment fragment);
    }

    public EditEventFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            mWhen = Utils.Time.localNow();
        } else {
            Contract c = Contract.getInstance();
            mWhen = Utils.Time.unBundle(savedInstanceState, c.EVENT.getQualifiedName(Event.WHEN));
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
                dialog.show(getFragmentManager(), mWhen);
            }
        });

        getDateButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dialog = new DatePickerFragment();
                dialog.setListener(EditEventFragment.this);
                dialog.show(getFragmentManager(), mWhen);
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
        Contract c = Contract.getInstance();
        Utils.Time.bundle(outState, c.EVENT.getQualifiedName(Event.WHEN), mWhen);
    }

    @Override
    public void onDateTimeChange(DateTime dateTime) {
        mWhen = dateTime;
        updateUi();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_edit_event;
    }

    @Override
    public Modification getModification() {
        return new Event.Modification(getName(), mWhen);
    }

    @Override
    public boolean validate() {
        return getName() == null || Contract.getInstance().EVENT.validateName(getName());
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
        updateUi();
    }

    public String getName() {
        return getNameView().getText().toString();
    }

    public void setWhen(DateTime when) {
        mWhen = when;
        updateUi();
    }

    public DateTime getWhen() {
        return mWhen;
    }

    public void setEventId(long id) {
        mId = id;
    }

    public long getEventId() {
        return mId;
    }

    public boolean isEventIdSet() {
        return mId > 0;
    }

    public boolean isEventNameEditedByUser() {
        return mUserEditedEventName;
    }

    private void updateUi() {
        if (mWhen != null) {
            if (getTimeButton() != null) {
                getTimeButton().setText(Utils.Time.toString(mWhen, DateTimeFormat.shortTime()));
            }

            if (getDateButton() != null) {
                getDateButton().setText(Utils.Time.toString(mWhen, DateTimeFormat.mediumDate()));
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
        return getTypeSafeView(id);
    }

    private AutoCompleteTextView getNameView() {
        return getTypeSafeView(R.id.edit_event_name);
    }
}
