/**
 * © Robert Williamson 2014-2016.
 * This program is distributed under the terms of the GNU General Public License.
 */
package com.robwilliamson.healthyesther.analysis;

import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.EventTypeTable;
import com.robwilliamson.healthyesther.db.generated.MealEventTable;
import com.robwilliamson.healthyesther.db.integration.DateTimeConverter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class WeeklyFrequencyAnalyzerTest {
    private static EventTypeTable.PrimaryKey mMealTypeKey = com.robwilliamson.healthyesther.db.integration.EventTypeTable.MEAL.getId();

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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mAnalyzer = new WeeklyFrequency.Analyzer();
    }

    @Test
    public void instantiated_hasNoFrequencies() {
        assertThat(mAnalyzer.getFrequencyFor(mMealEventKey), is((WeeklyFrequency) null));
    }

    @Test
    public void whenConsideringAnEvent_addsItToAFrequency() {
        mockMealEvent();
        mAnalyzer.consider(mEvent);

        assertThat(mAnalyzer.getFrequencyFor(mMealEventKey), is(not((WeeklyFrequency) null)));
    }

    @Test
    public void whenConsideringAnEvent_addsItOnceToAFrequency() {
        mockMealEvent();
        mAnalyzer.consider(mEvent);

        //noinspection ConstantConditions
        assertThat(mAnalyzer.getFrequencyFor(mMealEventKey).getUniqueCount(), is(1));
    }

    private void mockMealEvent() {
        doReturn(mEventKey).when(mEvent).getConcretePrimaryKey();
        doReturn(mEventType).when(mEvent).getEventTypeRow();
        doReturn(DateTimeConverter.now()).when(mEvent).getWhen();
        doReturn(mMealTypeKey).when(mEventType).getConcretePrimaryKey();
        doReturn(new MealEventTable.Row[]{
                mMealEvent
        }).when(mEvent).getMealEventEventId();
        doReturn(mMealEventKey).when(mMealEvent).getConcretePrimaryKey();
    }
}
