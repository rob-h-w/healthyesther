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

import com.robwilliamson.db.definition.Event;
import com.robwilliamson.db.definition.EventModification;
import com.robwilliamson.db.definition.HealthScore;
import com.robwilliamson.db.definition.HealthScoreEvent;
import com.robwilliamson.db.definition.Modification;
import com.robwilliamson.db.use.GetHealthScoresQuery;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditScoreEventGroupFragment extends EditFragment<EditScoreEventGroupFragment.Watcher> {

    public void removeScore(EditScoreEventFragment fragment) {
        getFragmentManager().beginTransaction().remove(fragment).commit();
    }

    public interface Watcher {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_score_event_group, container, false);
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

                            final int rowIdIndex = cursor.getColumnIndex(HealthScore._ID);
                            final int nameIndex = cursor.getColumnIndex(HealthScore.NAME);
                            final int bestValueIndex = cursor.getColumnIndex(HealthScore.BEST_VALUE);
                            final int randomQueryIndex = cursor.getColumnIndex(HealthScore.RANDOM_QUERY);
                            final int minLabelIndex = cursor.getColumnIndex(HealthScore.MIN_LABEL);
                            final int maxLabelIndex = cursor.getColumnIndex(HealthScore.MAX_LABEL);

                            if (cursor.moveToFirst()) {
                                do {
                                    String name = cursor.getString(nameIndex);
                                    if (Settings.INSTANCE.getDefaultExcludedEditScores().contains(name)){
                                        continue;
                                    }

                                    EditFragment fragment = EditScoreEventFragment.newInstance(
                                            cursor.getLong(rowIdIndex),
                                            name,
                                            cursor.getInt(bestValueIndex),
                                            cursor.getInt(randomQueryIndex) > 0,
                                            cursor.getString(minLabelIndex),
                                            cursor.getString(maxLabelIndex));

                                    mFragments.add(fragment);

                                    Query[] fragmentQueries = fragment.getQueries();

                                    if (fragmentQueries != null) {
                                        mQueries.addAll(Arrays.asList(fragmentQueries));
                                    }
                                } while (cursor.moveToNext());
                            }
                        }

                        @Override
                        public void onQueryComplete(Cursor cursor) {
                            final FragmentTransaction transaction = getFragmentManager().beginTransaction();

                            for (EditFragment fragment : mFragments) {
                                transaction.add(R.id.edit_score_group_layout, fragment);
                            }

                            transaction.commit();

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
}
