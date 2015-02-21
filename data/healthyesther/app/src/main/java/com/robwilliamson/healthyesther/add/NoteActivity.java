package com.robwilliamson.healthyesther.add;

import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import com.robwilliamson.db.use.QueryUser;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.fragment.edit.EditEventFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditFragment;

import java.util.ArrayList;

public class NoteActivity extends AbstractAddActivity {
    private final static String EVENT_TAG = "event";

    @Override
    protected ArrayList<Pair<EditFragment, String>> getEditFragments(boolean create) {
        ArrayList<Pair<EditFragment, String>> list = new ArrayList<Pair<EditFragment, String>>(1);
        EditFragment event = null;
        if (create) {
            event = new EditEventFragment();
        } else {
            event = getEventFragment();
        }

        list.add(new Pair<EditFragment, String>(event, EVENT_TAG));

        return list;
    }

    @Override
    protected void onModifySelected(SQLiteDatabase db) {

    }

    @Override
    protected int getModifyFailedStringId() {
        return R.string.could_not_insert_note_event;
    }

    /**
     * An array of query users that need to run queries every time this activity is resumed.
     *
     * @return The query users that use queries on resume, or an empty array if no query is required.
     */
    @Override
    protected QueryUser[] getOnResumeQueryUsers() {
        return new QueryUser[0];
    }

    private EditEventFragment getEventFragment() {
        return getFragment(EVENT_TAG);
    }
}
