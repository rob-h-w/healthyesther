package com.robwilliamson.healthyesther.fragment.edit;

import android.database.Cursor;
import android.widget.AutoCompleteTextView;

import com.robwilliamson.db.Contract;
import com.robwilliamson.db.Utils;
import com.robwilliamson.db.definition.Modification;
import com.robwilliamson.db.definition.Note;
import com.robwilliamson.db.use.GetAllNotesQuery;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.healthyesther.R;

import java.util.HashMap;

public class EditNoteFragment extends SuggestionEditFragment<EditNoteFragment.Watcher> {

    public String getName() {
        return getNameView().getText().toString();
    }

    public String getNote() {
        return getNoteView().getText().toString();
    }

    public interface Watcher {
        void onFragmentUpdate(EditNoteFragment fragment);
        void onQueryFailed(EditNoteFragment fragment, Throwable error);
    }

    public EditNoteFragment() {}

    @Override
    public Modification getModification() {
        String name = getName();

        if (name == null) {
            return new Note.Modification(name, getNote());
        }

        return new Note.Modification(getSuggestionId(name), getNote());
    }

    @Override
    public boolean validate() {
        Note note = Contract.getInstance().NOTE;
        return note.validateName(getName()) && note.validateNote(getNote());
    }

    @Override
    protected void updateWatcher(Watcher watcher) {
        watcher.onFragmentUpdate(this);
    }

    @Override
    public Query[] getQueries() {
        return new Query[] {
                new GetAllNotesQuery() {
                    private HashMap<String, Long> mSuggestionIds;

                    @Override
                    public void postQueryProcessing(Cursor cursor) {
                        mSuggestionIds = Utils.Db.cursorToSuggestionList(cursor,
                                Note.NAME,
                                Note._ID);
                    }

                    @Override
                    public void onQueryComplete(Cursor cursor) {
                        EditNoteFragment.this.setSuggestionIds(mSuggestionIds);
                    }

                    @Override
                    public void onQueryFailed(final Throwable error) {
                        callWatcher(new WatcherCaller<Watcher>() {
                            @Override
                            public void call(Watcher watcher) {
                                watcher.onQueryFailed(EditNoteFragment.this, error);
                            }
                        });
                    }
                }
        };
    }

    @Override
    protected AutoCompleteTextView getNameView() {
        return getTypeSafeView(R.id.note_name);
    }

    private AutoCompleteTextView getNoteView() {
        return getTypeSafeView(R.id.note_content);
    }
}
