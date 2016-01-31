package com.robwilliamson.healthyesther.fragment.edit;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.HealthScoreEventTable;
import com.robwilliamson.healthyesther.db.generated.HealthScoreTable;
import com.robwilliamson.healthyesther.fragment.dialog.EditScoreDialogFragment;

import java.util.Map;

import javax.annotation.Nonnull;

public class EditScoreEventFragment extends EditFragment<HealthScoreEventTable.Row> {
    private static final String SCORE = "score";
    private static final String EDIT_SCORE_FRAGMENT = "edit_score_fragment";
    private HealthScoreTable.Row mScore;
    private ContextMenu mContextMenu;

    @Nullable
    private Watcher mWatcher;

    public static EditScoreEventFragment newInstance(@Nonnull EventTable.Row event, @Nonnull HealthScoreTable.Row score) {
        EditScoreEventFragment fragment = new EditScoreEventFragment();
        fragment.setRow(new HealthScoreEventTable.Row(event.getNextPrimaryKey(), score.getNextPrimaryKey(), 0L));
        fragment.mScore = score;
        return fragment;
    }

    public static EditScoreEventFragment newInstance(@Nonnull HealthScoreEventTable.Row scoreEvent, @Nonnull HealthScoreTable.Row score) {
        EditScoreEventFragment fragment = new EditScoreEventFragment();
        fragment.setRow(scoreEvent);
        fragment.mScore = score;
        return fragment;
    }

    public static EditScoreEventFragment newInstance(@Nonnull Map.Entry<HealthScoreTable.Row, HealthScoreEventTable.Row> pair) {
        return newInstance(Utils.checkNotNull(pair.getValue()), Utils.checkNotNull(pair.getKey()));
    }

    public static EditScoreEventFragment newInstance(@Nonnull Pair<HealthScoreTable.Row, HealthScoreEventTable.Row> pair) {
        return newInstance(Utils.checkNotNull(pair.second), Utils.checkNotNull(pair.first));
    }

    @javax.annotation.Nullable
    @Override
    public HealthScoreEventTable.Row getRow() {
        HealthScoreEventTable.Row row = super.getRow();
        if (row != null) {
            if ((row.getScore() == null || row.getScore() == 0L) && !row.isInDatabase()) {
                return null;
            }
        }

        return row;
    }

    public String getName() {
        return mScore.getName();
    }

    public HealthScoreTable.Row getScore() {
        return mScore;
    }

    public void setScore(HealthScoreTable.Row score) {
        mScore = score;
        updateUi();
    }

    private void updateUi() {
        if (getView() == null) {
            return;
        }

        getTitle().setText(mScore.getName());
        getMaxLabel().setText(mScore.getMaxLabel());
        getMinLabel().setText(mScore.getMinLabel());
        getRatingBar().setMax(EditScoreDialogFragment.MAX);
        HealthScoreEventTable.Row row = getRow();
        if (row != null && row.getScore() != null) {
            getRatingBar().setRating(row.getScore());
        } else {
            getRatingBar().setRating(0);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mWatcher = (Watcher) context;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (mContextMenu == null || mContextMenu.findItem(item.getItemId()) != item) {
            mContextMenu = null;
            return super.onContextItemSelected(item);
        }

        mContextMenu = null;

        switch (item.getItemId()) {
            case R.id.action_edit:
                EditScoreDialogFragment dialog = EditScoreDialogFragment.createDialog(mScore);
                dialog.show(getFragmentManager(), EDIT_SCORE_FRAGMENT);
                return true;
            case R.id.action_hide:
                if (mWatcher != null) {
                    mWatcher.onFragmentRemoveRequest(EditScoreEventFragment.this);
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle args) {
        super.onCreate(args);
        Bundle a = args;

        if (a == null) {
            a = getArguments();
        }

        if (a != null) {
            mScore = (HealthScoreTable.Row) args.getSerializable(SCORE);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.score, menu);
        mContextMenu = menu; // Remember the menu we created to check if we should handle the
        // callback.
    }

    @Override
    public void onResume() {
        super.onResume();
        View view = Utils.checkNotNull(getView());
        registerForContextMenu(view);

        updateUi();

        getRatingBar().setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                HealthScoreEventTable.Row row = EditScoreEventFragment.super.getRow();
                if (row != null) {
                    row.setScore((long)Math.round(rating));
                }

                if (mWatcher != null) {
                    mWatcher.onFragmentUpdate(EditScoreEventFragment.this);
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle args) {
        super.onSaveInstanceState(args);
        args.putSerializable(SCORE, mScore);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_edit_score_event;
    }

    @Override
    public boolean isValid() {
        HealthScoreEventTable.Row row = getRow();
        return row != null && row.isValid();
    }

    @Override
    protected HealthScoreEventTable.Row createRow() {
        throw new UnsupportedOperationException("This edits a join row; the row can't be created independently.");
    }

    private TextView getTitle() {
        return Utils.View.getTypeSafeView(Utils.checkNotNull(getView()), R.id.score_name_title, TextView.class);
    }

    private TextView getMinLabel() {
        return Utils.View.getTypeSafeView(Utils.checkNotNull(getView()), R.id.score_minimum_label, TextView.class);
    }

    private TextView getMaxLabel() {
        return Utils.View.getTypeSafeView(Utils.checkNotNull(getView()), R.id.score_maximum_label, TextView.class);
    }

    private RatingBar getRatingBar() {
        return Utils.View.getTypeSafeView(Utils.checkNotNull(getView()), R.id.score_bar, RatingBar.class);
    }

    public interface Watcher {
        void onFragmentUpdate(EditScoreEventFragment fragment);

        void onFragmentRemoveRequest(EditScoreEventFragment fragment);
    }
}
