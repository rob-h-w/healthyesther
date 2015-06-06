package com.robwilliamson.healthyesther.fragment.edit;

import android.database.Cursor;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.Contract;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.data.NoteData;
import com.robwilliamson.healthyesther.db.definition.Modification;
import com.robwilliamson.healthyesther.db.definition.Note;
import com.robwilliamson.healthyesther.db.use.GetAllNotesQuery;
import com.robwilliamson.healthyesther.db.use.Query;

import java.util.HashMap;

public class EditNoteFragment extends SuggestionEditFragment<EditNoteFragment.Watcher> {
    private HashMap<Long, String> mNoteContents;
    private Long mUserSelectedId = null;
    private Long mOldUserSelectedId = null;
    private boolean mAlwaysCreate = false;

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

    public void setAlwaysCreate(boolean alwaysCreate) {
        mAlwaysCreate = alwaysCreate;
    }

    @Override
    protected void onNameClicked() {
        mOldUserSelectedId = mUserSelectedId;

        super.onNameClicked();

        mUserSelectedId = getSuggestionId(getName());
        suggestNote(mNoteContents.get(mUserSelectedId));
    }

    public void suggestNote(String note) {
        if (mAlwaysCreate) {
            return;
        }

        if (Utils.noString(note)) {
            if (mOldUserSelectedId != null
                    && getNote().equals(mNoteContents.get(mOldUserSelectedId))) {
                getNoteView().setText(note);
            }

            return;
        }

        if (mUserSelectedId != null) {
            if (Utils.noString(getNote())
                    || getNote().equals(mNoteContents.get(mOldUserSelectedId))) {
                getNoteView().setText(note);
            }
        }
    }

    public String getNote() {
        return getNoteView().getText().toString();
    }

    public interface Watcher {
        void onFragmentUpdate(EditNoteFragment fragment);
        void onQueryFailed(EditNoteFragment fragment, Throwable error);
    }

    public EditNoteFragment() {
        super(EditNoteFragment.Watcher.class);
    }

    @Override
    public Modification getModification() {
        String name = getName();
        Long id = getSuggestionId(name);

        if (id == null || mAlwaysCreate) {
            return new Note.Modification(name, getNote());
        }

        NoteData data = new NoteData();
        data.set_id(id);
        data.setNote(getNote());

        return new Note.Modification(data);
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

                        mNoteContents = new HashMap<Long, String>(cursor.getCount());
                        final int rowIdIndex = cursor.getColumnIndex(Note._ID);
                        final int noteIndex = cursor.getColumnIndex(Note.NOTE);

                        if (cursor.moveToFirst()) {
                            do {
                                mNoteContents.put(cursor.getLong(rowIdIndex), cursor.getString(noteIndex));
                            } while(cursor.moveToNext());
                        }
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
        return getTypeSafeView(R.id.note_name, AutoCompleteTextView.class);
    }

    private EditText getNoteView() {
        return getTypeSafeView(R.id.note_content, EditText.class);
    }
}
