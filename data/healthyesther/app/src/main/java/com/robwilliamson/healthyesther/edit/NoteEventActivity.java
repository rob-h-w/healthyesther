package com.robwilliamson.healthyesther.edit;

import android.os.Bundle;
import android.support.v4.util.Pair;

import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.NoteEventTable;
import com.robwilliamson.healthyesther.db.generated.NoteTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.DateTime;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;
import com.robwilliamson.healthyesther.db.includes.WhereContains;
import com.robwilliamson.healthyesther.db.integration.DatabaseAccessor;
import com.robwilliamson.healthyesther.db.integration.EventTypeTable;
import com.robwilliamson.healthyesther.fragment.BaseFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditEventFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditNoteFragment;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import static com.robwilliamson.healthyesther.db.includes.WhereContains.foreignKey;

public class NoteEventActivity extends AbstractEditActivity
        implements BaseFragment.Watcher {
    private final static String EVENT_TAG = "event";
    private final static String NOTE_TAG = "note";

    @Nonnull
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
        return new TransactionExecutor.Operation() {
            @Override
            public void doTransactionally(@Nonnull Database database, @Nonnull Transaction transaction) {
                EventTable.Row row = Utils.checkNotNull(getEventFragment().getRow());
                row.setTypeId(EventTypeTable.NOTE.getId());
                row.applyTo(transaction);

                NoteTable.Row note = Utils.checkNotNull(getNoteFragment().getRow());
                note.applyTo(transaction);

                NoteEventTable.Row noteEventRow = DatabaseAccessor.NOTE_EVENT_TABLE.select0Or1(
                        database, WhereContains.and(
                                WhereContains.foreignKey(NoteEventTable.EVENT_ID, row.getNextPrimaryKey().getId()),
                                WhereContains.foreignKey(NoteEventTable.NOTE_ID, note.getNextPrimaryKey().getId())));

                if (noteEventRow == null) {
                    noteEventRow = new NoteEventTable.Row(row.getNextPrimaryKey(), note.getNextPrimaryKey());
                }

                noteEventRow.applyTo(transaction);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        };
    }

    @Override
    protected void resumeFromIntentExtras(@Nonnull Bundle bundle) {
        final EventTable.Row event = Utils.checkNotNull((EventTable.Row) bundle.getSerializable(HealthDatabase.EVENT_TABLE.getName()));
        if (!event.getTypeId().equals(EventTypeTable.NOTE.getId())) {
            throw new EventTypeTable.BadEventTypeException(EventTypeTable.NOTE, event.getTypeId().getId());
        }

        getEventFragment().setRow(event);

        getExecutor().perform(new TransactionExecutor.Operation() {
            @Override
            public void doTransactionally(@Nonnull Database database, @Nonnull Transaction transaction) {
                NoteEventTable.Row noteEvent = HealthDatabase.NOTE_EVENT_TABLE.select1(
                        database,
                        foreignKey(NoteEventTable.EVENT_ID, event.getConcretePrimaryKey().getId()));

                final NoteTable.Row note = HealthDatabase.NOTE_TABLE.select1(
                        database,
                        noteEvent.getConcretePrimaryKey().getNoteId());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getNoteFragment().setRow(note);
                    }
                });
            }
        });
    }

    @Override
    protected void resumeFromSavedState(@Nonnull Bundle bundle) {

    }

    private EditNoteFragment getNoteFragment() {
        return getFragment(NOTE_TAG, EditNoteFragment.class);
    }

    private EditEventFragment getEventFragment() {
        return getFragment(EVENT_TAG, EditEventFragment.class);
    }

    @Override
    public void onFragmentUpdated(BaseFragment fragment) {
        if (fragment instanceof EditNoteFragment) {
            EditNoteFragment editFragment = (EditNoteFragment) fragment;
            getEventFragment().suggestEventName(editFragment.getName());
        }

        invalidateOptionsMenu();
    }
}
