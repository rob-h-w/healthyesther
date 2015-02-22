package com.robwilliamson.healthyesther.fragment.edit;

import android.database.Cursor;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.robwilliamson.db.Contract;
import com.robwilliamson.db.Utils;
import com.robwilliamson.db.definition.Modification;
import com.robwilliamson.db.definition.Note;
import com.robwilliamson.db.use.GetAllNotesQuery;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.healthyesther.R;

import java.util.HashMap;

public class EditNoteFragment extends SuggestionEditFragment<EditNoteFragment.Watcher> {

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_edit_note_event;
    }

    public String getName() {
        AutoCompleteTextView nameView = getNameView();

        if (nameView != null) {
            return nameView.getText().toString();
        }

        return null;
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

    private EditText getNoteView() {
        return getTypeSafeView(R.id.note_content);
    }
}
