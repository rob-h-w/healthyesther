package com.robwilliamson.healthyesther.edit;

import android.content.Intent;

import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.NoteEventTable;
import com.robwilliamson.healthyesther.db.generated.NoteTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.WhereContains;
import com.robwilliamson.healthyesther.db.integration.EventTypeTable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import test.ActivityTestContext;
import test.view.EditEventFragmentAccessor;
import test.view.EditNoteEventFragmentAccessor;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class NoteEventActivityTest {
    private static final String EVENT_NAME = "Event Name";
    private static final String NOTE_NAME = "Note";
    private static final String NOTE_DETAIL = "Note detail.";
    private static final String EDITED_EVENT_NAME = "Don't read this";
    private static final String EDITED_NOTE_NAME = "Diary Entry";
    private static final String EDITED_NOTE_DETAIL = "Secret diary notes.";

    private ActivityTestContext<TestableNoteEventActivity> mContext;

    private EditEventFragmentAccessor mEventFragmentAccessor;
    private EditNoteEventFragmentAccessor mNoteEventFragmentAccessor;

    @Before
    public void setup() {
        mContext = new ActivityTestContext<>(this, TestableNoteEventActivity.class);

        mEventFragmentAccessor = new EditEventFragmentAccessor(mContext);
        mNoteEventFragmentAccessor = new EditNoteEventFragmentAccessor(mContext);
    }

    @After
    public void teardown() {
        mContext.close();
    }

    @Test
    public void whenNewNoteIsAdded_createsNewEvent() {
        newNoteIsAdded();

        Database db = HealthDbHelper.getDatabase();

        EventTable.Row row = HealthDatabase.EVENT_TABLE.select1(db, WhereContains.any());
        assertThat(row.getTypeId(), is(EventTypeTable.NOTE.getId()));
    }

    @Test
    public void whenNewNoteIsAdded_createsNewEventWithName() {
        newNoteIsAdded();

        Database db = HealthDbHelper.getDatabase();

        EventTable.Row row = HealthDatabase.EVENT_TABLE.select1(db, WhereContains.any());
        assertThat(row.getName(), is(EVENT_NAME));
    }

    @Test
    public void whenNewNoteIsAdded_createsNewNoteEvent() {
        newNoteIsAdded();

        Database db = HealthDbHelper.getDatabase();

        EventTable.Row row = HealthDatabase.EVENT_TABLE.select1(db, WhereContains.any());
        HealthDatabase.NOTE_EVENT_TABLE.select1(db, WhereContains.foreignKey(NoteEventTable.EVENT_ID, row.getConcretePrimaryKey().getId()));
    }

    @Test
    public void whenNewNoteIsAdded_createsNewNoteAttachedToEvent() {
        newNoteIsAdded();

        Database db = HealthDbHelper.getDatabase();

        EventTable.Row row = HealthDatabase.EVENT_TABLE.select1(db, WhereContains.any());
        NoteEventTable.Row noteEvent = HealthDatabase.NOTE_EVENT_TABLE.select1(db, WhereContains.foreignKey(NoteEventTable.EVENT_ID, row.getConcretePrimaryKey().getId()));
        HealthDatabase.NOTE_TABLE.select1(db, noteEvent.getConcretePrimaryKey().getNoteId());
    }

    @Test
    public void whenNewNoteIsAdded_createsNewNoteAttachedToEventWithName() {
        newNoteIsAdded();

        Database db = HealthDbHelper.getDatabase();

        EventTable.Row row = HealthDatabase.EVENT_TABLE.select1(db, WhereContains.any());
        NoteEventTable.Row noteEvent = HealthDatabase.NOTE_EVENT_TABLE.select1(db, WhereContains.foreignKey(NoteEventTable.EVENT_ID, row.getConcretePrimaryKey().getId()));
        NoteTable.Row note = HealthDatabase.NOTE_TABLE.select1(db, noteEvent.getConcretePrimaryKey().getNoteId());

        assertThat(note.getName(), is(NOTE_NAME));
    }

    @Test
    public void whenNewNoteIsAdded_createsNewNoteAttachedToEventWithDetail() {
        newNoteIsAdded();

        Database db = HealthDbHelper.getDatabase();

        EventTable.Row row = HealthDatabase.EVENT_TABLE.select1(db, WhereContains.any());
        NoteEventTable.Row noteEvent = HealthDatabase.NOTE_EVENT_TABLE.select1(db, WhereContains.foreignKey(NoteEventTable.EVENT_ID, row.getConcretePrimaryKey().getId()));
        NoteTable.Row note = HealthDatabase.NOTE_TABLE.select1(db, noteEvent.getConcretePrimaryKey().getNoteId());

        assertThat(note.getNote(), is(NOTE_DETAIL));
    }

    @Test
    public void whenOpenedWithAnExistingNote_showsEventTitle() {
        openedWithAnExistingNote();

        assertThat(mEventFragmentAccessor.getName(), is(EVENT_NAME));
    }

    @Test
    public void whenOpenedWithAnExistingNote_showsNoteTitle() {
        openedWithAnExistingNote();

        assertThat(mNoteEventFragmentAccessor.getName(), is(NOTE_NAME));
    }

    @Test
    public void whenOpenedWithAnExistingNote_showsNoteDetail() {
        openedWithAnExistingNote();

        assertThat(mNoteEventFragmentAccessor.getDetail(), is(NOTE_DETAIL));
    }

    @Test
    public void whenExistingNoteIsEdited_replacesEvent() {
        existingNoteIsEdited();

        Database db = HealthDbHelper.getDatabase();

        EventTable.Row row = HealthDatabase.EVENT_TABLE.select1(db, WhereContains.any());

        assertThat(row.getName(), is(EDITED_EVENT_NAME));
    }

    @Test
    public void whenExistingNoteIsEdited_replacesNoteEvent() {
        existingNoteIsEdited();

        Database db = HealthDbHelper.getDatabase();

        EventTable.Row row = HealthDatabase.EVENT_TABLE.select1(db, WhereContains.any());
        NoteEventTable.Row[] noteEvents = HealthDatabase.NOTE_EVENT_TABLE.select(db, WhereContains.any());

        assertThat(noteEvents.length, is(1));
        assertThat(noteEvents[0].getConcretePrimaryKey().getEventId(), is(row.getConcretePrimaryKey()));
    }

    @Test
    public void whenExistingNoteIsEdited_replacesNoteName() {
        existingNoteIsEdited();

        Database db = HealthDbHelper.getDatabase();

        EventTable.Row row = HealthDatabase.EVENT_TABLE.select1(db, WhereContains.any());
        NoteEventTable.Row noteEvent = HealthDatabase.NOTE_EVENT_TABLE.select1(db, WhereContains.foreignKey(NoteEventTable.EVENT_ID, row.getConcretePrimaryKey().getId()));
        NoteTable.Row note = HealthDatabase.NOTE_TABLE.select1(db, WhereContains.foreignKey(NoteTable._ID, noteEvent.getConcretePrimaryKey().getNoteId().getId()));

        assertThat(note.getName(), is(EDITED_NOTE_NAME));
    }

    @Test
    public void whenExistingNoteIsEdited_replacesNoteDetail() {
        existingNoteIsEdited();

        Database db = HealthDbHelper.getDatabase();

        EventTable.Row row = HealthDatabase.EVENT_TABLE.select1(db, WhereContains.any());
        NoteEventTable.Row noteEvent = HealthDatabase.NOTE_EVENT_TABLE.select1(db, WhereContains.foreignKey(NoteEventTable.EVENT_ID, row.getConcretePrimaryKey().getId()));
        NoteTable.Row note = HealthDatabase.NOTE_TABLE.select1(db, WhereContains.foreignKey(NoteTable._ID, noteEvent.getConcretePrimaryKey().getNoteId().getId()));

        assertThat(note.getNote(), is(EDITED_NOTE_DETAIL));
    }

    @Test
    public void whenNoteAlreadyExists_populatesAutocomplete() {
        noteAlreadyExists();

        assertThat(Utils.checkNotNull(mNoteEventFragmentAccessor.getNameView()).getAdapter().getCount(), is(1));
    }

    @Test
    public void whenNoteAlreadyExists_populatesAutocompleteWithNoteName() {
        noteAlreadyExists();

        assertThat((String) Utils.checkNotNull(mNoteEventFragmentAccessor.getNameView()).getAdapter().getItem(0), is(NOTE_NAME));
    }

    private void noteAlreadyExists() {
        Database db = HealthDbHelper.getDatabase();

        try (Transaction transaction = db.getTransaction()) {
            NoteTable.Row note = new NoteTable.Row(NOTE_NAME, NOTE_DETAIL);
            note.applyTo(transaction);
            transaction.commit();
        }

        mContext.getActivityController().setup();
        Robolectric.flushBackgroundThreadScheduler();
        Robolectric.flushForegroundThreadScheduler();
    }

    private void existingNoteIsEdited() {
        openedWithAnExistingNote();

        mEventFragmentAccessor.setName(EDITED_EVENT_NAME);
        mNoteEventFragmentAccessor.setName(EDITED_NOTE_NAME);
        mNoteEventFragmentAccessor.setDetail(EDITED_NOTE_DETAIL);
        mContext.pressOk();
        Robolectric.flushForegroundThreadScheduler();
        Robolectric.flushBackgroundThreadScheduler();
    }

    private void openedWithAnExistingNote() {
        Database db = HealthDbHelper.getDatabase();
        EventTable.Row event;

        try (Transaction transaction = db.getTransaction()) {
            event = new EventTable.Row(EventTypeTable.NOTE.getId(),
                    mContext.getNow(),
                    mContext.getNow(),
                    mContext.getNow(),
                    EVENT_NAME);
            NoteTable.Row note = new NoteTable.Row(NOTE_NAME, NOTE_DETAIL);

            (new NoteEventTable.Row(event, note)).applyTo(transaction);
            transaction.commit();
            Robolectric.flushBackgroundThreadScheduler();
        }

        Intent intent = new Intent();
        intent.putExtra(HealthDatabase.EVENT_TABLE.getName(), event);
        mContext.getActivityController().withIntent(intent).setup();
        Robolectric.flushBackgroundThreadScheduler();
        Robolectric.flushForegroundThreadScheduler();
    }

    private void newNoteIsAdded() {
        mContext.getActivityController().setup();
        mNoteEventFragmentAccessor.setName(NOTE_NAME);
        mNoteEventFragmentAccessor.setDetail(NOTE_DETAIL);
        mEventFragmentAccessor.setName(EVENT_NAME);
        mContext.pressOk();
    }

    private static class TestableNoteEventActivity extends NoteEventActivity {
        private boolean mBusy = false;

        @Override
        protected boolean isBusy() {
            return mBusy;
        }

        @Override
        protected void setBusy(boolean busy) {
            mBusy = busy;
        }
    }
}
