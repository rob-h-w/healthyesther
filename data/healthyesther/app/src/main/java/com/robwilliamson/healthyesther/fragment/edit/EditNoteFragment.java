/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.fragment.edit;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.NoteTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;
import com.robwilliamson.healthyesther.db.includes.WhereContains;
import com.robwilliamson.healthyesther.db.use.InitializationQuerier;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

public class EditNoteFragment extends SuggestionEditFragment<NoteTable.Row> implements InitializationQuerier {
    private static final String NOTE_CONTENTS = "note contents";
    private static final String USER_SELECTED_ID = "user selected ID";
    private static final String OLD_USER_SELECTED_ID = "old user selected ID";
    private static final String ALWAYS_CREATE = "always create";

    private Map<Long, String> mNoteContents;
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
            return nameView.getText().toString().trim();
        }

        return null;
    }

    public void setAlwaysCreate(boolean alwaysCreate) {
        mAlwaysCreate = alwaysCreate;
    }

    @Override
    public void onResume() {
        super.onResume();

        com.robwilliamson.healthyesther.Utils.checkNotNull(getNoteView()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getRow() != null) {
                    getRow().setNote(s.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        com.robwilliamson.healthyesther.Utils.checkNotNull(getNameView()).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateAttachedActivity();
            }
        });
    }

    @Override
    protected void onNameChanged() {
        super.onNameChanged();

        if (getRow() != null) {
            getRow().setName(getName());
        }
    }

    @Override
    protected void onNameClicked() {
        mOldUserSelectedId = mUserSelectedId;

        super.onNameClicked();

        mUserSelectedId = getSuggestionId(getName());
        suggestNote(mNoteContents.get(mUserSelectedId));
    }

    @Override
    public void setRow(@Nonnull NoteTable.Row row) {
        super.setRow(row);

        updateUi();
    }

    /**
     * Called when all saved state has been restored into the view hierarchy
     * of the fragment.  This can be used to do initialization based on saved
     * state that you are letting the view hierarchy track itself, such as
     * whether check box widgets are currently checked.  This is called
     * after {@link #onActivityCreated(Bundle)} and before
     * {@link #onStart()}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            mNoteContents = (Map<Long, String>) savedInstanceState.getSerializable(NOTE_CONTENTS);
            mUserSelectedId = (Long) savedInstanceState.getSerializable(USER_SELECTED_ID);
            mOldUserSelectedId = (Long) savedInstanceState.getSerializable(OLD_USER_SELECTED_ID);
            mAlwaysCreate = savedInstanceState.getBoolean(ALWAYS_CREATE);
        }
    }

    /**
     * Called to ask the fragment to save its current dynamic state, so it
     * can later be reconstructed in a new instance of its process is
     * restarted.  If a new instance of the fragment later needs to be
     * created, the data you place in the Bundle here will be available
     * in the Bundle given to {@link #onCreate(Bundle)},
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}, and
     * {@link #onActivityCreated(Bundle)}.
     * <p/>
     * <p>This corresponds to {@link Activity#onSaveInstanceState(Bundle)
     * Activity.onSaveInstanceState(Bundle)} and most of the discussion there
     * applies here as well.  Note however: <em>this method may be called
     * at any time before {@link #onDestroy()}</em>.  There are many situations
     * where a fragment may be mostly torn down (such as when placed on the
     * back stack with no UI showing), but its state will not be saved until
     * its owning activity actually needs to save its state.
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(NOTE_CONTENTS, (Serializable) mNoteContents);
        outState.putSerializable(USER_SELECTED_ID, mUserSelectedId);
        outState.putSerializable(OLD_USER_SELECTED_ID, mOldUserSelectedId);
        outState.putBoolean(ALWAYS_CREATE, mAlwaysCreate);
    }

    private void updateUi() {
        NoteTable.Row row = getRow();
        if (row == null) {
            return;
        }

        EditText noteView = getNoteView();

        if (noteView != null) {
            noteView.setText(row.getNote());
        }

        AutoCompleteTextView nameView = getNameView();

        if (nameView != null) {
            nameView.setText(row.getName());
        }
    }

    public void suggestNote(String note) {
        if (mAlwaysCreate) {
            return;
        }

        if (Utils.noString(note)) {
            if (mOldUserSelectedId != null
                    && getNote().equals(mNoteContents.get(mOldUserSelectedId))) {
                EditText noteView = getNoteView();

                if (noteView != null) {
                    noteView.setText(note.trim());
                }
            }

            return;
        }

        if (mUserSelectedId != null) {
            if (Utils.noString(getNote())
                    || getNote().equals(mNoteContents.get(mOldUserSelectedId))) {
                EditText noteView = getNoteView();

                if (noteView != null) {
                    noteView.setText(note.trim());
                }
            }
        }
    }

    @Nonnull
    public String getNote() {
        EditText noteView = getNoteView();

        if (noteView == null) {
            return "";
        }

        return getNoteView().getText().toString().trim();
    }

    @javax.annotation.Nullable
    @Override
    protected NoteTable.Row createRow() {
        return new NoteTable.Row(getName(), getNote());
    }

    @Override
    protected AutoCompleteTextView getNameView() {
        return getTypeSafeView(R.id.note_name, AutoCompleteTextView.class);
    }

    @Nullable
    private EditText getNoteView() {
        return getTypeSafeView(R.id.note_content, EditText.class);
    }

    @Nonnull
    @Override
    public TransactionExecutor.Operation getInitializationQuery() {
        return new TransactionExecutor.Operation() {
            @Override
            public void doTransactionally(@Nonnull Database database, @Nonnull Transaction transaction) {
                NoteTable.Row[] notes = HealthDatabase.NOTE_TABLE.select(database, WhereContains.any());

                final Map<String, Long> suggestionIds = new HashMap<>();
                mNoteContents = new HashMap<>();

                for (NoteTable.Row note : notes) {
                    final String name = note.getName();
                    final Long id = note.getConcretePrimaryKey().getId();
                    suggestionIds.put(name, id);
                    mNoteContents.put(id, name);
                }

                setSuggestionIds(suggestionIds);
            }
        };
    }
}
