package com.robwilliamson.healthyesther.fragment;

import android.database.Cursor;
import android.widget.LinearLayout;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.definition.Event;
import com.robwilliamson.healthyesther.db.definition.Table;
import com.robwilliamson.healthyesther.db.use.Query;
import com.robwilliamson.healthyesther.db.use.SelectEventAndType;
import com.robwilliamson.healthyesther.R;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.HashMap;

public class ActivityGraphFragment extends AbstractQueryFragment {
    private static final String LOG_TAG = ActivityGraphFragment.class.getName();
    private static final int DAYS = 7;

    private GraphView mGraphView;

    public ActivityGraphFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_activity_graph;
    }

    private LinearLayout getLayout() {
        return com.robwilliamson.healthyesther.Utils.View.getTypeSafeView(getView(), R.id.activity_graph_layout);
    }

    @Override
    public Query[] getQueries() {
        return new Query[] {
                new SelectEventAndType(DateTime.now().minusDays(DAYS - 1).withZone(DateTimeZone.UTC).withTime(0, 0, 0, 0),
                        DateTime.now().withZone(DateTimeZone.UTC)) {
                    private HashMap<String, Integer> mEntriesPerDay = new HashMap<String, Integer>(DAYS); // 7 days.
                    private DateTime mNow;
                    private int mMax = 0;

                    @Override
                    public void postQueryProcessing(Cursor cursor) {
                        for (int i = 0; i < DAYS; i++) {
                            mEntriesPerDay.put(format().print(now().minusDays(i)), 0);
                        }

                        if (cursor != null && cursor.moveToFirst()) {
                            int whenIndex = cursor.getColumnIndex(Table.cleanName(Event.WHEN));
                            do {
                                DateTime when = Utils.Time.dateTimeFromDatabaseString(cursor.getString(whenIndex)).withTime(0, 0, 0, 0);
                                Integer count = mEntriesPerDay.get(format().print(when)) + 1;
                                mEntriesPerDay.put(format().print(when), count);

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

                        for (int i = 0; i < DAYS; i++) {
                            int minusDays = DAYS - 1 - i; // Range from 6-0.
                            DateTime day = now().minusDays(minusDays);
                            String dayStr = format().print(day);
                            data[i] = new GraphView.GraphViewData(i + 1, mEntriesPerDay.get(dayStr));
                            dateStrings[i] = formatter.print(day);
                        }

                        for (int i = 0; i < mMax + 1; i++) {
                            integerStrings[i] = String.valueOf(mMax - i);
                        }

                        GraphViewSeries activitySeries = new GraphViewSeries(data);

                        if (mGraphView != null) {
                            getLayout().removeView(mGraphView);
                        }

                        mGraphView = new LineGraphView(
                                ActivityGraphFragment.this.getActivity(),
                                getString(R.string.activity_last_week));
                        mGraphView.setManualYAxisBounds(mMax, 0);
                        mGraphView.setMinimumHeight(getActivity().getResources().getDimensionPixelSize(R.dimen.activity_graph_minimum_height));

                        getLayout().addView(mGraphView);

                        mGraphView.addSeries(activitySeries);
                        mGraphView.setHorizontalLabels(dateStrings);
                        mGraphView.setVerticalLabels(integerStrings);
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
                }
        };
    }

    private static DateTimeFormatter format() {
        return ISODateTimeFormat.dateTime();
    }
}
