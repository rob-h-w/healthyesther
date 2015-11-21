package com.robwilliamson.healthyesther.edit;

import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.includes.DateTime;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;
import com.robwilliamson.healthyesther.db.integration.EventTypeTable;
import com.robwilliamson.healthyesther.fragment.BaseFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditEventFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditNoteFragment;

import java.util.ArrayList;

public class NoteEventActivity extends AbstractEditEventActivity
        implements BaseFragment.Watcher {
    private final static String EVENT_TAG = "event";
    private final static String NOTE_TAG = "note";

    @Override
    protected ArrayList<Pair<EditFragment, String>> getEditFragments(boolean create) {
        ArrayList<Pair<EditFragment, String>> list = new ArrayList<>(1);
        EditNoteFragment note;
        EditFragment event;
        if (create) {
            note = new EditNoteFragment();
            DateTime now = DateTime.from(com.robwilliamson.healthyesther.db.Utils.Time.localNow());
            event = EditEventFragment.getInstance(new EventTable.Row(
                    EventTypeTable.NOTE.getId(),
                    now,
                    now,
                    null,
                    null));
        } else {
            note = getNoteFragment();
            event = getEventFragment();
        }

        note.setAlwaysCreate(true);

        list.add(new Pair<>((EditFragment) note, NOTE_TAG));
        list.add(new Pair<>(event, EVENT_TAG));

        return list;
    }

    @Override
    protected TransactionExecutor.Operation onModifySelected() {
        return null; // TODO
    }

    private EditNoteFragment getNoteFragment() {
        return getFragment(NOTE_TAG, EditNoteFragment.class);
    }

    private EditEventFragment getEventFragment() {
        return getFragment(EVENT_TAG, EditEventFragment.class);
    }

    @Override
    public void onFragmentUpdated(BaseFragment fragment) {
        invalidateOptionsMenu();
    }
}
