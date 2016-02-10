package com.robwilliamson.healthyesther.fragment.edit;

import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
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
    public void onResume() {
        super.onResume();

        com.robwilliamson.healthyesther.Utils.checkNotNull(getNoteView()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (getRow() != null) {
                    getRow().setNote(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
}
