package com.robwilliamson.healthyesther.fragment.edit;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(android.os.Bundle)} and {@link #onActivityCreated(android.os.Bundle)}.
     * <p/>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_note_event, container, false);
    }

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
