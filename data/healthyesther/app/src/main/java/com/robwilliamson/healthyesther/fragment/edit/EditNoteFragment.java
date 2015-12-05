package com.robwilliamson.healthyesther.fragment.edit;

import android.support.annotation.Nullable;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.generated.NoteTable;

import java.util.HashMap;

import javax.annotation.Nonnull;

public class EditNoteFragment extends SuggestionEditFragment<NoteTable.Row> {
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
                EditText noteView = getNoteView();

                if (noteView != null) {
                    noteView.setText(note);
                }
            }

            return;
        }

        if (mUserSelectedId != null) {
            if (Utils.noString(getNote())
                    || getNote().equals(mNoteContents.get(mOldUserSelectedId))) {
                EditText noteView = getNoteView();

                if (noteView != null) {
                    noteView.setText(note);
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

        return getNoteView().getText().toString();
    }

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
}
