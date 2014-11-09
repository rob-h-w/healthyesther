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
import com.robwilliamson.db.use.GetHealthScoresQuery;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;

public class EditScoreEventFragment extends EditFragment<EditScoreEventFragment.Watcher> {

    private static final String VALUE = "value";
    private static final String SCORE = "score";

    private int mValue;
    private GetHealthScoresQuery.Score mScore = new GetHealthScoresQuery.Score();

    public String getName() {
        return mScore.name;
    }

    public void setScore(GetHealthScoresQuery.Score score) {
        mScore = score;
        updateUi();
    }

    private void updateUi() {
        if (getView() == null) {
            return;
        }

        getTitle().setText(mScore.name);
        getMinLabel().setText(mScore.minLabel);
        getMaxLabel().setText(mScore.maxLabel);
        getRatingBar().setMax(HealthScore.MAX);
        getRatingBar().setRating(mValue);
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

    public static EditScoreEventFragment newInstance(GetHealthScoresQuery.Score score) {
        EditScoreEventFragment fragment = new EditScoreEventFragment();
        fragment.mScore = score;
        return fragment;
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
            mScore = GetHealthScoresQuery.Score.from(args, SCORE);
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

        updateUi();

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
        mScore.bundle(args, SCORE);
    }

    @Override
    public Modification getModification() {
        if (getModified()) {
            return new HealthScore.Modification(mScore);
        }

        return null;
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
