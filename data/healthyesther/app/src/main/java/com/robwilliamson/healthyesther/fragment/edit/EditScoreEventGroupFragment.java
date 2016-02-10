package com.robwilliamson.healthyesther.fragment.edit;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.view.View;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Settings;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.HealthScoreEventTable;
import com.robwilliamson.healthyesther.db.generated.HealthScoreTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;
import com.robwilliamson.healthyesther.db.includes.WhereContains;
import com.robwilliamson.healthyesther.db.integration.DatabaseAccessor;
import com.robwilliamson.healthyesther.edit.ScoreActivity;
import com.robwilliamson.healthyesther.fragment.AddValueFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EditScoreEventGroupFragment extends EditFragment<EventTable.Row> {
    private static final int REQUEST_ID = 1;
    private static final String ADD_VALUE_FRAGMENT = "add_value_fragment";
    private static final String ADD_SCORE_FRAGMENT = "add_score_fragment";

    private volatile boolean mResumed = false;

    public void clearScoreFragments() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        for (Fragment fragment : getEditScoreEventFragments()) {
            fragmentTransaction.remove(fragment);
        }
        fragmentTransaction.remove(getAddValueFragment());
        fragmentTransaction.commit();
        manager.popBackStack();
    }

    public void refreshScores() {
        Utils.checkNotNull(getExecutor()).perform(new TransactionExecutor.Operation() {
            @Override
            public void doTransactionally(@Nonnull Database database, @Nonnull Transaction transaction) {
                HealthScoreTable.Row[] rows = DatabaseAccessor.HEALTH_SCORE_TABLE.select(database, WhereContains.any());

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                EventTable.Row event = Utils.checkNotNull(getRow());

                for (HealthScoreTable.Row row : rows) {
                    if (Settings.INSTANCE.getDefaultExcludedEditScores().contains(row.getName())) {
                        continue;
                    }
                    addFragment(EditScoreEventFragment.newInstance(event, row), fragmentTransaction);
                }

                fragmentTransaction.add(R.id.edit_score_group_layout, new AddValueFragment(), ADD_VALUE_FRAGMENT);

                fragmentTransaction.commit();

                if (mResumed) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addOnClickListener();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getAddValueFragment() != null) {
            addOnClickListener();
        }

        mResumed = true;
    }

    @Override
    public void onPause() {
        super.onPause();

        mResumed = false;
    }

    protected void addOnClickListener() {
        AddValueFragment fragment = getAddValueFragment();
        fragment.setTitle(R.string.track_another_score);
        fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScoreActivity.class);
                startActivityForResult(intent, REQUEST_ID);
            }
        });
    }

    @Override
    public boolean isValid() {
        for (EditScoreEventFragment fragment : getEditScoreEventFragments()) {
            if (fragment.isValid()) {
                return true;
            }
        }

        return false;
    }

    @Nullable
    @Override
    protected EventTable.Row createRow() {
        throw new UnsupportedOperationException("This is a relation row - please create and set the row from the containing activity.");
    }

    @Override
    protected boolean canCreateRow() {
        return false;
    }

    public void addFragment(final EditFragment fragment, final FragmentTransaction transaction) {
        FragmentTransaction t = transaction;
        if (transaction == null) {
            t = getFragmentManager().beginTransaction();
        }

        t.add(R.id.edit_score_group_layout, fragment);

        if (transaction == null) {
            t.commit();
        }
    }

    private List<EditScoreEventFragment> getEditScoreEventFragments() {
        final ArrayList<EditScoreEventFragment> list = new ArrayList<>();
        final FragmentManager manager = getFragmentManager();

        for (Fragment fragment : manager.getFragments()) {
            if (fragment instanceof EditScoreEventFragment) {
                list.add((EditScoreEventFragment) fragment);
            }
        }

        return list;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_edit_score_event_group;
    }

    private AddValueFragment getAddValueFragment() {
        return Utils.View.getTypeSafeFragment(getFragmentManager(), ADD_VALUE_FRAGMENT, AddValueFragment.class);
    }

    @Nonnull
    public List<Pair<HealthScoreTable.Row, HealthScoreEventTable.Row>> getScores() {
        List<Pair<HealthScoreTable.Row, HealthScoreEventTable.Row>> scores = new ArrayList<>();
        for (EditScoreEventFragment fragment : getEditScoreEventFragments()) {
            HealthScoreTable.Row score = fragment.getScore();
            HealthScoreEventTable.Row scoreEvent = fragment.getRow();

            if (scoreEvent == null) {
                continue;
            }

            scoreEvent.getNextPrimaryKey().setEventId(Utils.checkNotNull(getRow()).getNextPrimaryKey());

            Pair<HealthScoreTable.Row, HealthScoreEventTable.Row> pair = new Pair<>(
                    score,
                    scoreEvent
            );
            scores.add(pair);
        }

        return scores;
    }

    public void removeScore(EditScoreEventFragment fragment) {
        getFragmentManager().beginTransaction().remove(fragment).commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(HealthScoreEventTable.SCORE, (Serializable) getScores());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            refreshScores();
        }
    }
}
