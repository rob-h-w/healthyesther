package com.robwilliamson.healthyesther.fragment.edit;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.abstraction.DataAbstraction;
import com.robwilliamson.healthyesther.db.definition.HealthScore;
import com.robwilliamson.healthyesther.db.definition.Modification;
import com.robwilliamson.healthyesther.db.use.Query;
import com.robwilliamson.healthyesther.fragment.dialog.EditScoreDialogFragment;

public class EditScoreEventFragment extends EditFragment<EditScoreEventFragment.Watcher> {
    public interface Watcher {
        void onFragmentUpdate(EditScoreEventFragment fragment);
        void onQueryFailed(EditScoreEventFragment fragment, Throwable error);
        void onFragmentRemoveRequest(EditScoreEventFragment fragment);
    }

    private static final String VALUE = "value";
    private static final String SCORE = "score";
    private static final String EDIT_SCORE_FRAGMENT = "edit_score_fragment";

    private int mValue;
    private HealthScore.Value mScore = new HealthScore.Value();
    private ContextMenu mContextMenu;

    public String getName() {
        return mScore.name;
    }

    public void setScore(HealthScore.Value score) {
        mScore = score;
        updateUi();
    }

    public HealthScore.Value getScore() {
        return mScore;
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

    @Override
    public Query[] getQueries() {
        return new Query[0];
    }

    public static EditScoreEventFragment newInstance(HealthScore.Value score) {
        EditScoreEventFragment fragment = new EditScoreEventFragment();
        fragment.mScore = score;
        return fragment;
    }

    public EditScoreEventFragment() {
        super(EditScoreEventFragment.Watcher.class);
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
                callWatcher(new WatcherCaller<Watcher>() {
                    @Override
                    public void call(Watcher watcher) {
                        watcher.onFragmentRemoveRequest(EditScoreEventFragment.this);
                    }
                });

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
            mScore = DataAbstraction.from(args.getBundle(SCORE), HealthScore.Value.class);
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
        return new HealthScore.Modification(mScore, getModified());
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
    protected void updateWatcher(Watcher watcher) {
        watcher.onFragmentUpdate(this);
    }

    public int getValue() {
        return (int) getRatingBar().getRating();
    }

    private TextView getTitle() {
        return Utils.View.getTypeSafeView(getView(), R.id.score_name_title, TextView.class);
    }

    private TextView getMinLabel() {
        return Utils.View.getTypeSafeView(getView(), R.id.score_minimum_label, TextView.class);
    }

    private TextView getMaxLabel() {
        return Utils.View.getTypeSafeView(getView(), R.id.score_maximum_label, TextView.class);
    }

    private RatingBar getRatingBar() {
        return Utils.View.getTypeSafeView(getView(), R.id.score_bar, RatingBar.class);
    }
}
