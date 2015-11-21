package com.robwilliamson.healthyesther.fragment.edit;

import android.database.Cursor;
import android.os.Bundle;

import com.robwilliamson.healthyesther.adapter.EventListAdapter;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.data.EventData;
import com.robwilliamson.healthyesther.db.use.GetEventsAfterDateTimeQuery;
import com.robwilliamson.healthyesther.db.use.Query;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.List;

public class EventHistoryListFragment extends EventListFragment {
    private static final String FROM = "from";
    private static final Duration WEEK = Duration.standardDays(7);

    private DateTime mFrom;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventHistoryListFragment() {
        mFrom = DateTime.now().minus(WEEK);
    }

    public static EventHistoryListFragment getInstance(DateTime from) {
        EventHistoryListFragment fragment = new EventHistoryListFragment();
        Bundle arguments = new Bundle();
        arguments.putString(FROM, Utils.Time.toDatabaseString(from));
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mFrom = Utils.Time.fromDatabaseString(arguments.getString(FROM));
        }

        super.onCreate(savedInstanceState);
    }
}
