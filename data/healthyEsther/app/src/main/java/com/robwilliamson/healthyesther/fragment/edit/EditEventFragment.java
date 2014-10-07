package com.robwilliamson.healthyesther.fragment.edit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.robwilliamson.db.Contract;
import com.robwilliamson.db.Utils;
import com.robwilliamson.db.definition.Event;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.fragment.dialog.TimePicker;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.SimpleDateFormat;

/**
 * Allows the user to edit an event's name and when properties.
 */
public class EditEventFragment extends Fragment implements TimePicker.OnTimeSetListener {
    private String mName;
    private DateTime mWhen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            mWhen = DateTime.now().withZone(DateTimeZone.UTC);
            mName = "";
        } else {
            Contract c = Contract.getInstance();
            mWhen = Utils.Time.unBundle(savedInstanceState, c.EVENT.getQualifiedName(Event.WHEN));
            mName = savedInstanceState.getString(c.EVENT.getQualifiedName(Event.NAME));
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

        getButton(R.id.edit_event_time_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker dialog = new TimePicker();
                dialog.setTimeSetListener(EditEventFragment.this);
                dialog.show(getFragmentManager());
            }
        });

        updateUi();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Contract c = Contract.getInstance();
        Utils.Time.bundle(outState, c.EVENT.getQualifiedName(Event.WHEN),  mWhen);
        outState.putString(c.EVENT.getQualifiedName(Event.NAME), mName);
    }

    @Override
    public void onTimeSet(TimePicker sender, int hourOfDay, int minute) {
        DateTime local = mWhen.withZone(DateTimeZone.getDefault());
        mWhen = local.withTime(hourOfDay, minute, 0, 0).withZone(DateTimeZone.UTC);
        updateUi();
    }

    public void setName(String name) {
        mName = name;
        updateUi();
    }

    public String getName() {
        return mName;
    }

    public void setWhen(DateTime when) {
        mWhen = when;
        updateUi();
    }

    public DateTime getWhen() {
        return mWhen;
    }

    private void updateUi() {
        if (mWhen != null) {
            if (getTimeButton() != null) {
                getTimeButton().setText(Utils.Time.toLocallyFormattedString(mWhen, "HH:mm"));
            }
        }

        if (getNameView() != null && mName != null) {
            getNameView().setQuery(mName, false);
        }
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

    private SearchView getNameView() {
        return getTypeSafeView(R.id.edit_event_name);
    }

    private <T extends View> T getTypeSafeView(int id) {
        View view = getView();

        if (view == null) {
            return null;
        }

        return (T)view.findViewById(id);
    }
}
