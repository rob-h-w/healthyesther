package com.robwilliamson.healthyesther.fragment.edit;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.robwilliamson.db.Contract;
import com.robwilliamson.db.Utils;
import com.robwilliamson.db.definition.Event;
import com.robwilliamson.db.definition.Modification;
import com.robwilliamson.db.use.GetAllMealsQuery;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.fragment.dialog.DatePickerFragment;
import com.robwilliamson.healthyesther.fragment.dialog.DateTimePickerListener;
import com.robwilliamson.healthyesther.fragment.dialog.TimePickerFragment;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import java.util.HashMap;

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
            mWhen = DateTime.now().withZone(DateTimeZone.UTC);
        } else {
            Contract c = Contract.getInstance();
            mWhen = Utils.Time.unBundle(savedInstanceState, c.EVENT.getQualifiedName(Event.WHEN));
        }

        updateUi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_event, container, false);
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
                setUserEditedEventName(!getNameView().getText().toString().isEmpty());
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

    public void setUserEditedEventName(boolean userEditedTheEventName) {
        mUserEditedEventName = userEditedTheEventName;
    }

    public boolean getUserEditedEventName() {
        return mUserEditedEventName;
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

    private void updateUi() {
        if (mWhen != null) {
            if (getTimeButton() != null) {
                getTimeButton().setText(Utils.Time.toString(mWhen, DateTimeFormat.shortTime().withZone(DateTimeZone.getDefault())));
            }

            if (getDateButton() != null) {
                getDateButton().setText(Utils.Time.toString(mWhen, DateTimeFormat.mediumDate().withZone(DateTimeZone.getDefault())));
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

    private <T extends View> T getTypeSafeView(int id) {
        return com.robwilliamson.healthyesther.Utils.View.getTypeSafeView(getView(), id);
    }
}
