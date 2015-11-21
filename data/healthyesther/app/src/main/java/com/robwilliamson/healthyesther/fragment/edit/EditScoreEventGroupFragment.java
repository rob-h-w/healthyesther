package com.robwilliamson.healthyesther.fragment.edit;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Pair;
import android.view.View;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.definition.HealthScore;
import com.robwilliamson.healthyesther.db.generated.HealthScoreEventTable;
import com.robwilliamson.healthyesther.db.generated.HealthScoreTable;
import com.robwilliamson.healthyesther.fragment.AddValueFragment;
import com.robwilliamson.healthyesther.fragment.dialog.EditScoreDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class EditScoreEventGroupFragment extends EditFragment<HealthScoreEventTable.Row> {
    private static final String ADD_VALUE_FRAGMENT = "add_value_fragment";
    private static final String ADD_SCORE_FRAGMENT = "add_score_fragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            return;
        }

        getFragmentManager().beginTransaction().add(R.id.edit_score_group_layout, new AddValueFragment(), ADD_VALUE_FRAGMENT).commit();
    }

    @Override
    public void onResume() {
        super.onResume();

        AddValueFragment fragment = getAddValueFragment();
        fragment.setTitle(R.string.track_another_score);
        fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditScoreDialogFragment dialog = new EditScoreDialogFragment();

                dialog.show(getFragmentManager(), ADD_SCORE_FRAGMENT);
            }
        });
    }

    @Override
    public boolean validate() {
        for (EditScoreEventFragment fragment : getEditScoreEventFragments()) {
            if (fragment.validate()) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected HealthScoreEventTable.Row createRow() {
        throw new UnsupportedOperationException("This is a relation row - please create and set the row from the containing activity.");
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

    public EditScoreEventFragment getEditScoreEventFragment(HealthScore.Value score) {
        List<EditScoreEventFragment> fragments = getEditScoreEventFragments();

        for (EditScoreEventFragment fragment : fragments) {
            HealthScore.Value fragmentScore = fragment.getScore();
            if (fragmentScore != null && fragmentScore._id != null) {
                boolean matchId = fragmentScore._id != 0 && fragmentScore._id.equals(score._id);
                boolean matchName = fragmentScore.name.equals(score.name);
                if (matchId || matchName) {
                    return fragment;
                }
            }
        }

        return null;
    }

    private AddValueFragment getAddValueFragment() {
        return Utils.View.getTypeSafeFragment(getFragmentManager(), ADD_VALUE_FRAGMENT, AddValueFragment.class);
    }

    public List<Pair<HealthScoreTable.Row, Integer>> getScores() {
        List<Pair<HealthScoreTable.Row, Integer>> scores = new ArrayList<>();
        for (EditScoreEventFragment fragment : getEditScoreEventFragments()) {
            HealthScoreTable.Row row = fragment.getRow();
            Pair<HealthScoreTable.Row, Integer> pair = new Pair<>(
                    row,
                    fragment.getValue()
            );
            scores.add(pair);
        }

        return scores;
    }
}
