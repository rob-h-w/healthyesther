package com.robwilliamson.healthyesther.add;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import com.robwilliamson.healthyesther.db.definition.Event;
import com.robwilliamson.healthyesther.db.definition.EventModification;
import com.robwilliamson.healthyesther.db.definition.HealthScore;
import com.robwilliamson.healthyesther.db.definition.HealthScoreEvent;
import com.robwilliamson.healthyesther.db.definition.Modification;
import com.robwilliamson.healthyesther.db.use.Query;
import com.robwilliamson.healthyesther.db.use.QueryUser;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Settings;
import com.robwilliamson.healthyesther.dialog.EditScoreDialog;
import com.robwilliamson.healthyesther.fragment.edit.EditEventFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditScoreEventFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditScoreEventGroupFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ScoreActivity extends AbstractAddActivity implements EditScoreEventGroupFragment.Watcher,
        EditScoreEventFragment.Watcher,
        EditEventFragment.Watcher{
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
        ArrayList<Pair<EditFragment, String>> list = new ArrayList<Pair<EditFragment, String>>(2);
        EditFragment scoreGroup = null;
        EditFragment event = null;
        if (create) {
            scoreGroup = new EditScoreEventGroupFragment();
            event = new EditEventFragment();
        } else {
            scoreGroup = getScoreGroupFragment();
            event = getEventFragment();
        }

        list.add(new Pair<EditFragment, String>(scoreGroup, SCORE_GROUP_TAG));
        list.add(new Pair<EditFragment, String>(event, EVENT_TAG));

        return list;
    }

    @Override
    protected void onModifySelected(SQLiteDatabase db) {
        Event.Modification event = (Event.Modification) getEventFragment().getModification();
        event.setTypeId(HealthScoreEvent.EVENT_TYPE_ID);
        event.modify(db);

        EventModification scoreEvent = (EventModification) getScoreGroupFragment().getModification();
        scoreEvent.setEventModification(event);
        scoreEvent.modify(db);
    }

    @Override
    protected int getModifyFailedStringId() {
        return R.string.could_not_insert_health_score_event;
    }

    @Override
    protected QueryUser[] getOnResumeQueryUsers() {
        return new QueryUser[] {
                getScoreGroupFragment()
        };
    }

    private EditScoreEventGroupFragment getScoreGroupFragment() {
        return getFragment(SCORE_GROUP_TAG);
    }

    private EditEventFragment getEventFragment() {
        return getFragment(EVENT_TAG);
    }

    @Override
    public void onFragmentUpdate(EditScoreEventFragment fragment) {
        invalidateOptionsMenu();
    }

    @Override
    public void onQueryFailed(EditScoreEventFragment fragment, Throwable error) {
    }

    @Override
    public void onFragmentRemoveRequest(EditScoreEventFragment fragment) {
        Settings settings = Settings.INSTANCE;
        Set<String> exclusionList = settings.getDefaultExcludedEditScores();
        exclusionList.add(fragment.getName());
        settings.setDefaultEditScoreExclusionList(exclusionList);
        getScoreGroupFragment().removeScore(fragment);
        invalidateOptionsMenu();
    }

    @Override
    public void onFragmentUpdate(EditEventFragment fragment) {

    }

    @Override
    public void onFragmentUpdate(EditScoreEventGroupFragment fragment) {

    }

    @Override
    public void onQueryFailed(EditScoreEventGroupFragment fragment, Throwable error) {
        // TODO Report the error.
    }

    @Override
    public void furtherQueries(EditScoreEventGroupFragment fragment, List<Query> queries) {
        doQueries(queries);
    }

    @Override
    public void onScoreModified(HealthScore.Score score) {
        Settings.INSTANCE.showScore(score);

        EditScoreEventFragment fragment = getScoreGroupFragment().getEditScoreEventFragment(score);

        if (fragment == null) {
            fragment = EditScoreEventFragment.newInstance(score);
            final Modification modification = fragment.getModification();
            getScoreGroupFragment().addFragment(fragment, null);
            List<Query> list = new ArrayList<Query>(1);
            list.add(new Query() {
                @Override
                public Cursor query(SQLiteDatabase db) {
                    modification.modify(db);
                    return null;
                }

                @Override
                public void postQueryProcessing(Cursor cursor) {

                }

                @Override
                public void onQueryComplete(Cursor cursor) {

                }

                @Override
                public void onQueryFailed(Throwable error) {

                }
            });
            doQueries(list);
        } else {
            fragment.setScore(score);
            fragment.setModified(true);
        }

    }

    @Override
    public void onQueryFailed(EditScoreDialog fragment, Throwable error) {
        // TODO Report the error.
    }

    @Override
    public void furtherQueries(EditScoreDialog fragment, List<Query> queries) {
        doQueries(queries);
    }
}
