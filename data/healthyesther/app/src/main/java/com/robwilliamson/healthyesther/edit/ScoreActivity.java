package com.robwilliamson.healthyesther.edit;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Settings;
import com.robwilliamson.healthyesther.db.definition.Event;
import com.robwilliamson.healthyesther.db.definition.EventModification;
import com.robwilliamson.healthyesther.db.definition.HealthScore;
import com.robwilliamson.healthyesther.db.definition.HealthScoreEvent;
import com.robwilliamson.healthyesther.db.definition.Modification;
import com.robwilliamson.healthyesther.db.use.Query;
import com.robwilliamson.healthyesther.db.use.QueryUser;
import com.robwilliamson.healthyesther.fragment.dialog.EditScoreDialogFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditEventFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditScoreEventFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditScoreEventGroupFragment;

import java.util.ArrayList;
import java.util.List;

public class ScoreActivity extends AbstractEditActivity implements EditScoreEventGroupFragment.Watcher,
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
        ArrayList<Pair<EditFragment, String>> list = new ArrayList<>(2);
        EditFragment scoreGroup;
        EditFragment event;
        if (create) {
            scoreGroup = new EditScoreEventGroupFragment();
            event = new EditEventFragment();
        } else {
            scoreGroup = getScoreGroupFragment();
            event = getEventFragment();
        }

        list.add(new Pair<>(scoreGroup, SCORE_GROUP_TAG));
        list.add(new Pair<>(event, EVENT_TAG));

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
    public QueryUser[] getQueryUsers() {
        return new QueryUser[] {
                getScoreGroupFragment()
        };
    }

    private EditScoreEventGroupFragment getScoreGroupFragment() {
        return getFragment(SCORE_GROUP_TAG, EditScoreEventGroupFragment.class);
    }

    private EditEventFragment getEventFragment() {
        return getFragment(EVENT_TAG, EditEventFragment.class);
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
        settings.hideScore(fragment.getScore());
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
    public void onScoreModified(HealthScore.Value score) {
        Settings.INSTANCE.showScore(score);

        EditScoreEventFragment fragment = getScoreGroupFragment().getEditScoreEventFragment(score);

        if (fragment == null) {
            fragment = EditScoreEventFragment.newInstance(score);
            final Modification modification = fragment.getModification();
            getScoreGroupFragment().addFragment(fragment, null);
            List<Query> list = new ArrayList<>(1);
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
    public void onQueryFailed(EditScoreDialogFragment fragment, Throwable error) {
        // TODO Report the error.
    }
}
