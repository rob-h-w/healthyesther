package com.robwilliamson.healthyesther.edit;

import android.util.Pair;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Settings;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.HealthScoreEventTable;
import com.robwilliamson.healthyesther.db.generated.HealthScoreTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.DateTime;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;
import com.robwilliamson.healthyesther.db.includes.WhereContains;
import com.robwilliamson.healthyesther.db.integration.DatabaseAccessor;
import com.robwilliamson.healthyesther.db.integration.EventTypeTable;
import com.robwilliamson.healthyesther.fragment.BaseFragment;
import com.robwilliamson.healthyesther.fragment.dialog.EditScoreDialogFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditEventFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditScoreEventFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditScoreEventGroupFragment;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class ScoreEventActivity extends AbstractEditEventActivity implements EditScoreDialogFragment.Watcher, EditScoreEventFragment.Watcher, BaseFragment.Watcher {
    private final static String SCORE_GROUP_TAG = "score group";
    private final static String EVENT_TAG = "event";

    @Override
    protected void onResume() {
        super.onResume();

        EditEventFragment fragment = getEventFragment();
        if (!fragment.isEventNameEditedByUser()) {
            fragment.setName(getString(R.string.health_score));
        }
    }

    @Override
    protected ArrayList<Pair<EditFragment, String>> getEditFragments(boolean create) {
        ArrayList<Pair<EditFragment, String>> list = new ArrayList<>(2);
        EditFragment scoreGroup;
        EditFragment event;
        if (create) {
            scoreGroup = new EditScoreEventGroupFragment();
            DateTime now = DateTime.from(com.robwilliamson.healthyesther.db.Utils.Time.localNow());
            event = EditEventFragment.getInstance(
                    new EventTable.Row(
                            EventTypeTable.HEALTH.getId(),
                            now,
                            now,
                            null,
                            null));
        } else {
            scoreGroup = getScoreGroupFragment();
            event = getEventFragment();
        }

        list.add(new Pair<>(scoreGroup, SCORE_GROUP_TAG));
        list.add(new Pair<>(event, EVENT_TAG));

        return list;
    }

    @Override
    protected TransactionExecutor.Operation onModifySelected() {
        return new TransactionExecutor.Operation() {
            @Override
            public void doTransactionally(@Nonnull Database database, @Nonnull Transaction transaction) {
                EventTable.Row event = Utils.checkNotNull(getEventFragment().getRow());
                event.setTypeId(EventTypeTable.HEALTH.getId());
                event.applyTo(transaction);

                EditScoreEventGroupFragment groupFragment = getScoreGroupFragment();

                List<Pair<HealthScoreTable.Row, Integer>> scoresAndEvents = groupFragment.getScores();

                for (Pair<HealthScoreTable.Row, Integer> pair : scoresAndEvents) {
                    if (pair.first == null) {
                        continue;
                    }

                    HealthScoreTable.Row healthScoreRow = DatabaseAccessor.HEALTH_SCORE_TABLE.select0Or1(
                            database,
                            WhereContains.columnEqualling(
                                    HealthScoreTable.NAME,
                                    pair.first.getName()));

                    if (healthScoreRow == null) {
                        healthScoreRow = pair.first;
                    }

                    healthScoreRow.applyTo(transaction);
                    Long score = pair.second == null ? null : pair.second.longValue();
                    HealthScoreEventTable.Row row = DatabaseAccessor.HEALTH_SCORE_EVENT_TABLE.select0Or1(database,
                            WhereContains.and(
                                    WhereContains.foreignKey(HealthScoreEventTable.EVENT_ID, event.getNextPrimaryKey().getId()),
                                    WhereContains.foreignKey(HealthScoreEventTable.HEALTH_SCORE_ID, healthScoreRow.getNextPrimaryKey().getId())
                            ));

                    if (row == null) {
                        row = new HealthScoreEventTable.Row(event.getNextPrimaryKey(), healthScoreRow.getNextPrimaryKey(), score);
                    }

                    row.applyTo(transaction);
                }

                finish();
            }
        };
    }

    private EditScoreEventGroupFragment getScoreGroupFragment() {
        return getFragment(SCORE_GROUP_TAG, EditScoreEventGroupFragment.class);
    }

    private EditEventFragment getEventFragment() {
        return getFragment(EVENT_TAG, EditEventFragment.class);
    }

    @Override
    public void onFragmentUpdated(BaseFragment fragment) {
        invalidateOptionsMenu();
    }

    @Override
    public void onScoreModified(@Nonnull final HealthScoreTable.Row score) {
        invalidateOptionsMenu();

        Utils.checkNotNull(getExecutor()).perform(new TransactionExecutor.Operation() {
            @Override
            public void doTransactionally(@Nonnull Database database, @Nonnull Transaction transaction) {
                score.applyTo(transaction);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EditScoreEventGroupFragment groupFragment = getScoreGroupFragment();
                        if (groupFragment != null) {
                            groupFragment.clearScoreFragments();
                            groupFragment.refreshScores();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onFragmentUpdate(EditScoreEventFragment fragment) {
        invalidateOptionsMenu();
    }

    @Override
    public void onFragmentRemoveRequest(EditScoreEventFragment fragment) {
        Settings settings = Settings.INSTANCE;
        settings.hideScore(fragment.getScore());
        getScoreGroupFragment().removeScore(fragment);
        invalidateOptionsMenu();
    }
}
