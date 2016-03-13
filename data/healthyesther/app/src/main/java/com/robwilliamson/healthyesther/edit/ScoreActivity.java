package com.robwilliamson.healthyesther.edit;

import android.os.Bundle;
import android.support.v4.util.Pair;

import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.HealthScoreJudgmentRangeTable;
import com.robwilliamson.healthyesther.db.generated.HealthScoreTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;
import com.robwilliamson.healthyesther.fragment.BaseFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditScoreFragment;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ScoreActivity extends AbstractEditActivity implements BaseFragment.Watcher {
    private static final String EDIT_SCORE_TAG = "edit score tag";

    @Nonnull
    @Override
    protected List<Pair<EditFragment, String>> getEditFragments(boolean create) {
        List<Pair<EditFragment, String>> list = new ArrayList<>(1);
        @Nonnull
        EditScoreFragment fragment;
        if (create) {
            fragment = new EditScoreFragment();
        } else {
            fragment = Utils.checkNotNull(getScoreFragment());
        }

        list.add(new Pair<EditFragment, String>(fragment, EDIT_SCORE_TAG));

        return list;
    }

    @Override
    protected TransactionExecutor.Operation onModifySelected() {
        return new TransactionExecutor.Operation() {
            @Override
            public void doTransactionally(@Nonnull Database database, @Nonnull Transaction transaction) {
                EditScoreFragment fragment = getScoreFragment();
                if (fragment == null) {
                    return;
                }

                HealthScoreTable.Row row = fragment.getRow();
                if (row == null) {
                    return;
                }

                row.applyTo(transaction);

                for (HealthScoreJudgmentRangeTable.Row judgmentRow : fragment.getJudgments()) {
                    judgmentRow.applyTo(transaction);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        };
    }

    @Override
    protected void resumeFromIntentExtras(@Nonnull Bundle bundle) {
        HealthScoreTable.Row row = (HealthScoreTable.Row) bundle.getSerializable(HealthDatabase.HEALTH_SCORE_TABLE.getName());

        EditScoreFragment editScoreFragment = getScoreFragment();

        if (editScoreFragment != null && row != null) {
            editScoreFragment.setRow(row);
        }
    }

    @Override
    protected void resumeFromSavedState(@Nonnull Bundle bundle) {

    }

    @Nullable
    private EditScoreFragment getScoreFragment() {
        return getFragment(EDIT_SCORE_TAG, EditScoreFragment.class);
    }

    @Override
    public void onFragmentUpdated(BaseFragment fragment) {
        invalidateOptionsMenu();
    }
}
