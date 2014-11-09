package com.robwilliamson.healthyesther.fragment.edit;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robwilliamson.db.definition.EventModification;
import com.robwilliamson.db.definition.HealthScore;
import com.robwilliamson.db.definition.HealthScoreEvent;
import com.robwilliamson.db.definition.Modification;
import com.robwilliamson.db.use.GetHealthScoresQuery;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Settings;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.dialog.AddScoreDialog;
import com.robwilliamson.healthyesther.fragment.AddValueFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditScoreEventGroupFragment extends EditFragment<EditScoreEventGroupFragment.Watcher> {
    private static final String ADD_VALUE_FRAGMENT = "add_value_fragment";
    private static final String ADD_SCORE_FRAGMENT = "add_score_fragment";

    public void removeScore(EditScoreEventFragment fragment) {
        getFragmentManager().beginTransaction().remove(fragment).commit();
    }

    public interface Watcher extends AddScoreDialog.Watcher {
        void onFragmentUpdate(EditScoreEventGroupFragment fragment);
        void onQueryFailed(EditScoreEventGroupFragment fragment, Throwable error);
        void furtherQueries(EditScoreEventGroupFragment fragment, List<Query> queries);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_score_event_group, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        AddValueFragment fragment = getAddValueFragment();
        fragment.setTitle(R.string.track_another_score);
        fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddScoreDialog dialog = new AddScoreDialog();

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

                    if (editScoreModification == null) {
                        continue;
                    }

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
                            List<HealthScore.Score> scores = scoresFrom(cursor);

                            for (HealthScore.Score score: scores) {
                                if (Settings.INSTANCE.getDefaultExcludedEditScores().contains(score.name)){
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
                                    watcher.furtherQueries(EditScoreEventGroupFragment.this, mQueries);
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

    public EditScoreEventFragment getEditScoreEventFragment(HealthScore.Score score) {
        List<EditScoreEventFragment> fragments = getEditScoreEventFragments();

        for (EditScoreEventFragment fragment : fragments) {
            if (fragment.getName().equals(score.name)) {
                return fragment;
            }
        }

        return null;
    }

    private AddValueFragment getAddValueFragment() {
        return Utils.View.getTypeSafeFragment(getFragmentManager(), ADD_VALUE_FRAGMENT);
    }
}
