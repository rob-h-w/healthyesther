package com.robwilliamson.healthyesther.edit;

import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.db.HealthDbHelper;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.NoteEventTable;
import com.robwilliamson.healthyesther.db.generated.NoteTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.WhereContains;
import com.robwilliamson.healthyesther.db.integration.EventTypeTable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

    private void newNoteIsAdded() {
        mContext.getActivityController().create().start().resume();
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
