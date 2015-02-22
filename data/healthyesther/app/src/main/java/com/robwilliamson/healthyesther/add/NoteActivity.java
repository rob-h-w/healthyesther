package com.robwilliamson.healthyesther.add;

import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;
import android.widget.Toast;

import com.robwilliamson.db.definition.Event;
import com.robwilliamson.db.definition.Note;
import com.robwilliamson.db.definition.NoteEvent;
import com.robwilliamson.db.use.QueryUser;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.fragment.edit.EditEventFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditNoteFragment;

import java.util.ArrayList;

public class NoteActivity extends AbstractAddActivity
        implements EditEventFragment.Watcher, EditNoteFragment.Watcher {
    private final static String EVENT_TAG = "event";
    private final static String NOTE_TAG = "note";

    @Override
    protected ArrayList<Pair<EditFragment, String>> getEditFragments(boolean create) {
        ArrayList<Pair<EditFragment, String>> list = new ArrayList<Pair<EditFragment, String>>(1);
        EditFragment note = null;
        EditFragment event = null;
        if (create) {
            note = new EditNoteFragment();
            event = new EditEventFragment();
        } else {
            note = getNoteFragment();
            event = getEventFragment();
        }

        list.add(new Pair<EditFragment, String>(note, NOTE_TAG));
        list.add(new Pair<EditFragment, String>(event, EVENT_TAG));

        return list;
    }

    @Override
    protected void onModifySelected(SQLiteDatabase db) {
        Note.Modification note = (Note.Modification)getNoteFragment().getModification();
        Event.Modification event = (Event.Modification)getEventFragment().getModification();
        NoteEvent.Modification noteEvent = new NoteEvent.Modification(note, event);
        noteEvent.modify(db);
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
        return new QueryUser[] {
                getNoteFragment()
        };
    }

    private EditNoteFragment getNoteFragment() {
        return getFragment(NOTE_TAG);
    }

    private EditEventFragment getEventFragment() {
        return getFragment(EVENT_TAG);
    }

    @Override
    public void onFragmentUpdate(EditNoteFragment fragment) {
        getEventFragment().suggestEventName(fragment.getName());
        invalidateOptionsMenu();
    }

    @Override
    public void onQueryFailed(EditNoteFragment fragment, Throwable error) {
        Toast.makeText(this, getText(R.string.could_not_get_autocomplete_text_for_notes), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFragmentUpdate(EditEventFragment fragment) {
        invalidateOptionsMenu();
    }
}
