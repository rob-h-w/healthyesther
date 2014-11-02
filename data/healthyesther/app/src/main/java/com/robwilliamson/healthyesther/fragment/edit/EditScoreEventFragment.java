package com.robwilliamson.healthyesther.fragment.edit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.robwilliamson.db.definition.HealthScore;
import com.robwilliamson.db.definition.Modification;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;

public class EditScoreEventFragment extends EditFragment<EditScoreEventFragment.Watcher> {

    private static final String VALUE = "value";

    private int mValue;
    private long mId;
    private String mName;
    private int mBestValue;
    private boolean mRandomQuery;
    private String mMinLabel;
    private String mMaxLabel;

    public String getName() {
        return mName;
    }

    public interface Watcher {
        void onFragmentUpdate(EditScoreEventFragment fragment);
        void onQueryFailed(EditScoreEventFragment fragment, Throwable error);
        void onFragmentRemoveRequest(EditScoreEventFragment fragment);
    }

    @Override
    public Query[] getQueries() {
        return new Query[0];
    }

    public static EditScoreEventFragment newInstance(long id){
        EditScoreEventFragment fragment = new EditScoreEventFragment();
        Bundle args = new Bundle();
        args.putInt(VALUE, 0);
        args.putLong(HealthScore._ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public static EditScoreEventFragment newInstance(
            long id,
            String name,
            int bestValue,
            boolean randomQuery,
            String minLabel,
            String maxLabel) {
        EditScoreEventFragment fragment = new EditScoreEventFragment();
        Bundle args = new Bundle();
        args.putInt(VALUE, 0);
        args.putLong(HealthScore._ID, id);
        args.putString(HealthScore.NAME, name);
        args.putInt(HealthScore.BEST_VALUE, bestValue);
        args.putBoolean(HealthScore.RANDOM_QUERY, randomQuery);
        args.putString(HealthScore.MIN_LABEL, minLabel);
        args.putString(HealthScore.MAX_LABEL, maxLabel);
        fragment.setArguments(args);
        return fragment;
    }

    public static EditScoreEventFragment newInstance(String name,
                                                     int bestValue,
                                                     boolean randomQuery,
                                                     String minLabel,
                                                     String maxLabel) {
        return newInstance(-1, name, bestValue, randomQuery, minLabel, maxLabel);
    }

    public EditScoreEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle args) {
        super.onCreate(args);
        Bundle a = args;

        if (a == null) {
            a = getArguments();
        }

        if (a != null) {
            mValue = a.getInt(VALUE);
            mId = a.getLong(HealthScore._ID);
            mName = a.getString(HealthScore.NAME);
            mBestValue = a.getInt(HealthScore.BEST_VALUE);
            mRandomQuery = a.getBoolean(HealthScore.RANDOM_QUERY, false);
            mMinLabel = a.getString(HealthScore.MIN_LABEL, null);
            mMaxLabel = a.getString(HealthScore.MAX_LABEL, null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_score_event, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        getLayout().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.hide_score);
                builder.setPositiveButton(R.string.hide, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        EditScoreEventFragment.this.callWatcher(new WatcherCaller<Watcher>() {
                            @Override
                            public void call(Watcher watcher) {
                                watcher.onFragmentRemoveRequest(EditScoreEventFragment.this);
                            }
                        });
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();
                return true;
            }
        });

        getTitle().setText(mName);
        getMinLabel().setText(mMinLabel);
        getMaxLabel().setText(mMaxLabel);
        getRatingBar().setMax(HealthScore.MAX);
        getRatingBar().setRating(mValue);

        getRatingBar().setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                updateWatcher();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle args) {
        super.onSaveInstanceState(args);
        args.putInt(VALUE, getValue());
        args.putLong(HealthScore._ID, mId);
        args.putString(HealthScore.NAME, mName);
        args.putInt(HealthScore.BEST_VALUE, mBestValue);
        args.putBoolean(HealthScore.RANDOM_QUERY, mRandomQuery);
        args.putString(HealthScore.MIN_LABEL, mMinLabel);
        args.putString(HealthScore.MAX_LABEL, mMaxLabel);
    }

    @Override
    public Modification getModification() {
        if (mId == -1) {
            return new HealthScore.Modification(
                    mName, mBestValue, mRandomQuery, mMinLabel, mMaxLabel);
        }

        return new HealthScore.Modification(mId);
    }

    @Override
    public boolean validate() {
        return getValue() != 0;
    }

    @Override
    protected void updateWatcher(Watcher watcher) {
        watcher.onFragmentUpdate(this);
    }

    public int getValue() {
        return (int) getRatingBar().getRating();
    }

    private TextView getTitle() {
        return Utils.View.getTypeSafeView(getView(), R.id.score_name_title);
    }

    private TextView getMinLabel() {
        return Utils.View.getTypeSafeView(getView(), R.id.score_minimum_label);
    }

    private TextView getMaxLabel() {
        return Utils.View.getTypeSafeView(getView(), R.id.score_maximum_label);
    }

    private RatingBar getRatingBar() {
        return Utils.View.getTypeSafeView(getView(), R.id.score_bar);
    }

    private LinearLayout getLayout() {
        return Utils.View.getTypeSafeView(getView(), R.id.edit_score_layout);
    }
}
