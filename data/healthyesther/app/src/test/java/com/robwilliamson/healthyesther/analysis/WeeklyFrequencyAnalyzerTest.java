/**
 * © Robert Williamson 2014-2018.
 * This program is distributed under the terms of the GNU General Public License.
 */
package com.robwilliamson.healthyesther.analysis;

import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.EventTypeTable;
import com.robwilliamson.healthyesther.db.generated.HealthScoreEventTable;
import com.robwilliamson.healthyesther.db.generated.MealEventTable;
import com.robwilliamson.healthyesther.db.generated.MedicationEventTable;
import com.robwilliamson.healthyesther.db.generated.NoteEventTable;
import com.robwilliamson.healthyesther.db.includes.BaseRow;
import com.robwilliamson.healthyesther.db.integration.DateTimeConverter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import javax.annotation.Nonnull;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class WeeklyFrequencyAnalyzerTest {
    static {
        DateTimeConverter.now();
    }

    private WeeklyFrequency.Analyzer mAnalyzer;
    @Mock
    private EventTable.Row mEvent;

    @Mock
    private EventTable.PrimaryKey mEventKey;

    @Mock
    private EventTypeTable.Row mEventType;

    @Mock
    private MealEventTable.Row mMealEvent;

    @Mock
    private MealEventTable.PrimaryKey mMealEventKey;

    @Mock
    private MedicationEventTable.Row mMedEvent;

    @Mock
    private MedicationEventTable.PrimaryKey mMedEventKey;

    @Mock
    private HealthScoreEventTable.Row mHealthEvent;

    @Mock
    private HealthScoreEventTable.PrimaryKey mHealthEventKey;

    @Mock
    private NoteEventTable.Row mNoteEvent;

    @Mock
    private NoteEventTable.PrimaryKey mNoteEventKey;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mAnalyzer = new WeeklyFrequency.Analyzer();
    }

    @Test
    public void instantiated_hasNoFrequencies() {
        assertThat(mAnalyzer.getFrequencyFor(mMealEventKey), is((WeeklyFrequency) null));
        assertThat(mAnalyzer.getFrequencyFor(mMedEventKey), is((WeeklyFrequency) null));
        assertThat(mAnalyzer.getFrequencyFor(mHealthEventKey), is((WeeklyFrequency) null));
        assertThat(mAnalyzer.getFrequencyFor(mNoteEventKey), is((WeeklyFrequency) null));
    }

    @Test
    public void whenConsideringAMealEvent_addsItToAFrequency() {
        mockMealEvent();
        mAnalyzer.consider(mEvent);

        assertThat(mAnalyzer.getFrequencyFor(mMealEventKey), is(not((WeeklyFrequency) null)));
    }

    @Test
    public void whenConsideringAMealEvent_doesNotAddItToMedFrequency() {
        mockMealEvent();
        mAnalyzer.consider(mEvent);

        assertThat(mAnalyzer.getFrequencyFor(mMedEventKey), is((WeeklyFrequency) null));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void whenConsideringAMealEvent_addsItOnceToAFrequency() {
        mockMealEvent();
        mAnalyzer.consider(mEvent);

        assertThat(mAnalyzer.getFrequencyFor(mMealEventKey).getUniqueCount(), is(1));
        assertThat(mAnalyzer.getFrequencyFor(mMealEventKey).getCount(WeeklyFrequency.name(mEvent)), is(1L));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void whenConsidering2MedEvents_addsThemToAFrequency() {
        mockMedEvent();
        mAnalyzer.consider(mEvent);
        mAnalyzer.consider(mEvent);

        assertThat(mAnalyzer.getFrequencyFor(mMedEventKey).getUniqueCount(), is(1));
        assertThat(mAnalyzer.getFrequencyFor(mMedEventKey).getCount(WeeklyFrequency.name(mEvent)), is(2L));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void whenConsideringAHealthScoreEvent_addsItToAFrequency() {
        mockHealthScoreEvent();

        mAnalyzer.consider(mEvent);
        assertThat(mAnalyzer.getFrequencyFor(mHealthEventKey).getCount(WeeklyFrequency.name(mEvent)), is(1L));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void whenConsideringANoteEvent_addsItToAFrequency() {
        mockNoteScoreEvent();

        mAnalyzer.consider(mEvent);
        assertThat(mAnalyzer.getFrequencyFor(mNoteEventKey).getCount(WeeklyFrequency.name(mEvent)), is(1L));
    }

    private void mockMealEvent() {
        mockTEvent(
                mMealEventKey,
                com.robwilliamson.healthyesther.db.integration.EventTypeTable.MEAL.getId(),
                mMealEvent,
                new Runnable() {
                    @Override
                    public void run() {
                        doReturn(
                                new MealEventTable.Row[]{
                                        mMealEvent
                                }).when(mEvent).getMealEventEventId();
                    }
                });
    }

    private void mockMedEvent() {
        mockTEvent(
                mMedEventKey,
                com.robwilliamson.healthyesther.db.integration.EventTypeTable.MEDICATION.getId(),
                mMedEvent,
                new Runnable() {
                    @Override
                    public void run() {
                        doReturn(
                                new MedicationEventTable.Row[]{
                                        mMedEvent
                                }).when(mEvent).getMedicationEventEventId();
                    }
                });
    }

    private void mockHealthScoreEvent() {
        mockTEvent(
                mHealthEventKey,
                com.robwilliamson.healthyesther.db.integration.EventTypeTable.HEALTH.getId(),
                mHealthEvent,
                new Runnable() {
                    @Override
                    public void run() {
                        doReturn(
                                new HealthScoreEventTable.Row[]{
                                        mHealthEvent
                                }).when(mEvent).getHealthScoreEventEventId();
                    }
                });
    }

    private void mockNoteScoreEvent() {
        mockTEvent(
                mNoteEventKey,
                com.robwilliamson.healthyesther.db.integration.EventTypeTable.NOTE.getId(),
                mNoteEvent,
                new Runnable() {
                    @Override
                    public void run() {
                        doReturn(
                                new NoteEventTable.Row[]{
                                        mNoteEvent
                                }).when(mEvent).getNoteEventEventId();
                    }
                });
    }

    private <T, U, V extends BaseRow> void mockTEvent(T key, @Nonnull U eventType, @Nonnull V row, @Nonnull Runnable bespokeMock) {
        doReturn(mEventKey).when(mEvent).getConcretePrimaryKey();
        doReturn(mEventType).when(mEvent).getEventTypeRow();
        doReturn(DateTimeConverter.now()).when(mEvent).getWhen();
        doReturn(eventType).when(mEventType).getConcretePrimaryKey();
        doReturn(key).when(row).getConcretePrimaryKey();
        bespokeMock.run();
    }
}
