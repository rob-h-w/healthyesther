package com.robwilliamson.healthyesther;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.robwilliamson.db.*;
import com.robwilliamson.db.definition.Event;
import com.robwilliamson.db.definition.Table;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.db.use.SelectEventAndType;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.HashMap;

public class ActivityGraphFragment extends Fragment {


    public ActivityGraphFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity_graph, container, false);
    }

    Query getOnResumeQuery() {
        return new SelectEventAndType(DateTime.now().minusWeeks(1).withZone(DateTimeZone.UTC).withTime(0, 0, 0, 0),
                DateTime.now().minusDays(1).withZone(DateTimeZone.UTC).withTime(0, 0, 0, 0)) {
            private static final int DAYS = 7;
            private final DateTimeFormatter FORMAT = ISODateTimeFormat.dateTime();
            private HashMap<String, Integer> mEntriesPerDay = new HashMap<String, Integer>(DAYS); // 7 days.
            private DateTime mNow;
            private int mMax = 0;

            @Override
            public void postQueryProcessing(Cursor cursor) {
                if (cursor != null && cursor.moveToFirst()) {
                    for (int i = 1; i <= DAYS; i++) {
                        mEntriesPerDay.put(FORMAT.print(now().minusDays(i)), 0);
                    }

                    int whenIndex = cursor.getColumnIndex(Table.cleanName(Event.WHEN));
                    do {
                        DateTime when = com.robwilliamson.db.Utils.Time.dateTimeFromDatabaseString(cursor.getString(whenIndex)).withTime(0, 0, 0, 0);
                        Integer count = mEntriesPerDay.get(FORMAT.print(when)) + 1;
                        mEntriesPerDay.put(FORMAT.print(when), count);

                        if (count > mMax) {
                            mMax = count;
                        }

                    } while (cursor.moveToNext());
                }
            }

            @Override
            public void onQueryComplete(Cursor cursor) {// init example series data
                GraphView.GraphViewData[] data = new GraphView.GraphViewData[DAYS];
                String [] dateStrings = new String[DAYS];
                String [] integerStrings = new String[mMax + 1];
                DateTimeFormatter formatter = DateTimeFormat.forPattern("E");

                for (int i = 1; i <= DAYS; i++) {
                    DateTime day = now().minusDays(i);
                    String dayStr = FORMAT.print(day);
                    data[i - 1] = new GraphView.GraphViewData(i, mEntriesPerDay.get(dayStr));
                    dateStrings[i - 1] = formatter.print(day);
                }

                for (int i = 0; i < mMax + 1; i++) {
                    integerStrings[i] = String.valueOf(mMax - i);
                }

                GraphViewSeries activitySeries = new GraphViewSeries(data);

                GraphView graphView = new LineGraphView(
                        ActivityGraphFragment.this.getActivity(),
                        getString(R.string.activity_last_week)
                );
                graphView.addSeries(activitySeries);
                graphView.setHorizontalLabels(dateStrings);
                graphView.setVerticalLabels(integerStrings);
                graphView.setMinimumHeight(getActivity().getResources().getDimensionPixelSize(R.dimen.activity_graph_minimum_height));

                getLayout().addView(graphView);
            }

            @Override
            public void onQueryFailed(Throwable error) {

            }

            private DateTime now() {
                if (mNow == null) {
                    mNow = DateTime.now().withZone(DateTimeZone.UTC).withTime(0, 0, 0, 0);
                }

                return mNow;
            }
        };
    }

    private LinearLayout getLayout() {
        return Utils.View.getTypeSafeView(getView(), R.id.activity_graph_layout);
    }
}
