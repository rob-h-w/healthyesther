package com.robwilliamson.healthyesther.fragment.edit;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Settings;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.DataAbstraction;
import com.robwilliamson.healthyesther.db.definition.EventModification;
import com.robwilliamson.healthyesther.db.definition.HealthScore;
import com.robwilliamson.healthyesther.db.definition.HealthScoreEvent;
import com.robwilliamson.healthyesther.db.definition.Modification;
import com.robwilliamson.healthyesther.db.use.GetHealthScoresQuery;
import com.robwilliamson.healthyesther.db.use.Query;
import com.robwilliamson.healthyesther.fragment.AddValueFragment;
import com.robwilliamson.healthyesther.fragment.dialog.EditScoreDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class EditScoreEventGroupFragment extends EditFragment<EditScoreEventGroupFragment.Watcher> {
    private static final String ADD_VALUE_FRAGMENT = "add_value_fragment";
    private static final String ADD_SCORE_FRAGMENT = "add_score_fragment";

    public void removeScore(EditScoreEventFragment fragment) {
        getFragmentManager().beginTransaction().remove(fragment).commit();
    }

    public interface Watcher extends EditScoreDialogFragment.Watcher {
        void onFragmentUpdate(EditScoreEventGroupFragment fragment);
        void onQueryFailed(EditScoreEventGroupFragment fragment, Throwable error);
    }

    public static EditScoreEventGroupFragment newInstance() {
        EditScoreEventGroupFragment fragment = new EditScoreEventGroupFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    public EditScoreEventGroupFragment() {
        // Required empty public constructor
    }

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
    public Modification getModification() {
        return new EventModification() {

            @Override
            public void modify(SQLiteDatabase db) {
                for (EditScoreEventFragment fragment : getEditScoreEventFragments()) {
                    if (!fragment.validate()) {
                        continue;
                    }

                    HealthScore.Modification editScoreModification = (HealthScore.Modification) fragment.getModification();

                    editScoreModification.modify(db);

                    HealthScoreEvent.Modification eventModification = new HealthScoreEvent.Modification(
                            editScoreModification,
                            getEventModification(),
                            fragment.getValue());
                    eventModification.modify(db);

                    setRowId(eventModification.getRowId());
                }
            }
        };
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
    protected void updateWatcher(Watcher watcher) {
        watcher.onFragmentUpdate(this);
    }

    @Override
    public Query[] getQueries() {
        if (getEditScoreEventFragments().isEmpty()) {
            return new Query[]{
                    new GetHealthScoresQuery() {

                        private ArrayList<EditFragment> mFragments;
                        private ArrayList<Query> mQueries;

                        @Override
                        public void postQueryProcessing(Cursor cursor) {
                            mFragments = new ArrayList<EditFragment>(cursor.getCount());
                            mQueries = new ArrayList<Query>(cursor.getCount());
                            List<HealthScore.Value> scores = DataAbstraction.listFrom(cursor, HealthScore.Value.class);
                            Set<String> excludedEditScoreTitles = Settings.INSTANCE.getDefaultExcludedEditScores();

                            for (HealthScore.Value score : scores) {
                                if (excludedEditScoreTitles.contains(score.name)){
                                    continue;
                                }

                                EditFragment fragment = EditScoreEventFragment.newInstance(score);

                                mFragments.add(fragment);

                                Query[] fragmentQueries = fragment.getQueries();

                                if (fragmentQueries != null) {
                                    mQueries.addAll(Arrays.asList(fragmentQueries));
                                }
                            }
                        }

                        @Override
                        public void onQueryComplete(Cursor cursor) {
                            addFragments(mFragments);

                            EditScoreEventGroupFragment.this.callWatcher(new WatcherCaller<EditScoreEventGroupFragment.Watcher>() {
                                @Override
                                public void call(EditScoreEventGroupFragment.Watcher watcher) {
                                    watcher.enqueueQueries(mQueries);
                                }
                            });
                        }

                        @Override
                        public void onQueryFailed(final Throwable error) {
                            EditScoreEventGroupFragment.this.callWatcher(new WatcherCaller<EditScoreEventGroupFragment.Watcher>() {
                                @Override
                                public void call(EditScoreEventGroupFragment.Watcher watcher) {
                                    watcher.onQueryFailed(EditScoreEventGroupFragment.this, error);
                                }
                            });
                        }
                    }
            };
        } else {
            return new Query[0];
        }
    }

    private void addFragments(List<EditFragment> fragments) {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();

        for (EditFragment fragment : fragments) {
            addFragment(fragment, transaction);
        }

        transaction.commit();
    }

    public void addFragment(final EditFragment fragment, final FragmentTransaction transaction){
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
        final ArrayList<EditScoreEventFragment> list = new ArrayList<EditScoreEventFragment>();
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
                boolean matchId = fragmentScore._id != 0 && fragmentScore._id == score._id;
                boolean matchName = fragmentScore.name == score.name;
                if (matchId || matchName) {
                    return fragment;
                }
            }
        }

        return null;
    }

    private AddValueFragment getAddValueFragment() {
        return Utils.View.getTypeSafeFragment(getFragmentManager(), ADD_VALUE_FRAGMENT);
    }
}
