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

    public static EventHistoryListFragment getInstance(DateTime from) {
        EventHistoryListFragment fragment = new EventHistoryListFragment();
        Bundle arguments = new Bundle();
        arguments.putString(FROM, Utils.Time.toDatabaseString(from));
        fragment.setArguments(arguments);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventHistoryListFragment() {
        mFrom = DateTime.now().minus(WEEK);
    }

    @Override
    public Query[] getQueries() {
        return new Query[] {
                new GetEventsAfterDateTimeQuery(mFrom) {
                    List<EventData> mResults;

                    /**
                     * Called after the query in the worker thread. Use this to parse the cursor output.
                     * The query has been successfully completed if this method is called.
                     * {@link #onQueryComplete(android.database.Cursor)} has not yet been called.<br/>
                     * If this method throws, no further callbacks will be called; the thrown object will not be
                     * handled.
                     */
                    @Override
                    public void postQueryProcessing(Cursor cursor) {
                        mResults = EventData.listFrom(cursor, EventData.class);
                    }

                    /**
                     * Called in the UI thread after successful processing of the query.
                     */
                    @Override
                    public void onQueryComplete(Cursor cursor) {
                        EventListAdapter adapter = getAdapter();
                        adapter.clear();
                        adapter.addAll(mResults);
                    }

                    /**
                     * Called in the UI thread after processing the query threw. It is assumed that the query was
                     * rolled back.
                     */
                    @Override
                    public void onQueryFailed(Throwable error) {
                        // TODO: Implement a toast or something.
                    }
                }
        };
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
