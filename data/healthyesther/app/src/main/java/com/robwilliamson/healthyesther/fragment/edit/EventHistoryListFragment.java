/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.fragment.edit;

import android.os.Bundle;

import com.robwilliamson.healthyesther.db.Utils;

import java.time.Duration;
import java.time.ZonedDateTime;

public class EventHistoryListFragment extends EventListFragment {
    private static final String FROM = "from";
    private static final Duration WEEK = Duration.ofDays(7);

    private ZonedDateTime mFrom;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventHistoryListFragment() {
        mFrom = ZonedDateTime.now().minus(WEEK);
    }

    public static EventHistoryListFragment getInstance(ZonedDateTime from) {
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
