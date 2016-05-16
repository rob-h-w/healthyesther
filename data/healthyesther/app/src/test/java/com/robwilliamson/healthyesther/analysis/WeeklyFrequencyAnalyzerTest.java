/**
 * © Robert Williamson 2014-2016.
 * This program is distributed under the terms of the GNU General Public License.
 */
package com.robwilliamson.healthyesther.analysis;

import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.EventTypeTable;
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
    private static EventTypeTable.PrimaryKey mTypeKey = new EventTypeTable.PrimaryKey(0);

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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mAnalyzer = new WeeklyFrequency.Analyzer();
    }

    @Test
    public void instantiated_hasNoFrequencies() {
        assertThat(mAnalyzer.getFrequencyFor(mTypeKey), is((WeeklyFrequency) null));
    }

    @Test
    public void whenConsideringAnEvent_addsItToAFrequency() {
        mockEvent();
        mAnalyzer.consider(mEvent);

        assertThat(mAnalyzer.getFrequencyFor(mTypeKey), is(not((WeeklyFrequency) null)));
    }

    @Test
    public void whenConsideringAnEvent_addsItOnceToAFrequency() {
        mockEvent();
        mAnalyzer.consider(mEvent);

        //noinspection ConstantConditions
        assertThat(mAnalyzer.getFrequencyFor(mTypeKey).getUniqueCount(), is(1));
    }

    private void mockEvent() {
        doReturn(mEventKey).when(mEvent).getConcretePrimaryKey();
        doReturn(mEventType).when(mEvent).getEventTypeRow();
        doReturn(DateTimeConverter.now()).when(mEvent).getWhen();
        doReturn(new EventTypeTable.PrimaryKey(0)).when(mEventType).getConcretePrimaryKey();
    }
}
