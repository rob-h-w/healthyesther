package com.robwilliamson.healthyesther.fragment.edit;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.generated.HealthScoreTable;
import com.robwilliamson.healthyesther.fragment.dialog.EditScoreDialogFragment;

import javax.annotation.Nonnull;

public class EditScoreEventFragment extends EditFragment<HealthScoreTable.Row> {
    private static final String VALUE = "value";
    private static final String SCORE = "score";
    private static final String EDIT_SCORE_FRAGMENT = "edit_score_fragment";
    private HealthScoreTable.Row mScore;
    private int mValue;
    private ContextMenu mContextMenu;

    @Nullable
    private Watcher mWatcher;

    public static EditScoreEventFragment newInstance(@Nonnull HealthScoreTable.Row score) {
        EditScoreEventFragment fragment = new EditScoreEventFragment();
        fragment.mScore = score;
        return fragment;
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
        getRatingBar().setRating(mValue);
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
            mValue = a.getInt(VALUE);
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
                if (mWatcher != null) {
                    mWatcher.onFragmentUpdate(EditScoreEventFragment.this);
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle args) {
        super.onSaveInstanceState(args);
        args.putInt(VALUE, getValue());
        args.putSerializable(SCORE, mScore);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_edit_score_event;
    }

    @Override
    public boolean validate() {
        return getValue() != 0;
    }

    @Override
    protected HealthScoreTable.Row createRow() {
        return new HealthScoreTable.Row(0L, "", false, null, null);
    }

    public int getValue() {
        return (int) getRatingBar().getRating();
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
