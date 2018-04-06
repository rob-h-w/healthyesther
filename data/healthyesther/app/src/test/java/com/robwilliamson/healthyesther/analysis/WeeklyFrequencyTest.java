/**
 * © Robert Williamson 2014-2018.
 * This program is distributed under the terms of the GNU General Public License.
 */
package com.robwilliamson.healthyesther.analysis;

import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.EventTypeTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.DateTime;
import com.robwilliamson.healthyesther.db.integration.DateTimeConverter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import test.HealthDatabaseAccessor;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class WeeklyFrequencyTest {
    private static final String[] dateTimeStrings = {
            "2016-05-08T11:22:51 +01:00"
    };
    private static final List<DateTime> dateTimes;

    static {
        DateTimeConverter.now();
        dateTimes = new ArrayList<>(dateTimeStrings.length);
        for (String dateTime : dateTimeStrings) {
            ZonedDateTime jTime = Utils.Time.fromLocalString(dateTime);
            dateTimes.add(DateTime.from(jTime));
        }
    }

    private WeeklyFrequency weeklyFrequency;

    @Mock
    private EventTable mEventTable;

    @Mock
    private EventTable.PrimaryKey mEventTablePrimaryKey;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HealthDatabaseAccessor.INSTANCE.setEventTable(mEventTable);
    }

    @After
    public void tearDown() {
        HealthDatabaseAccessor.INSTANCE.resetAll();
    }

    @Test
    public void instantiatedWithNoEvents_isEmpty() {
        weeklyFrequency = new WeeklyFrequency(new ArrayList<EventTable.Row>());

        assertThat(weeklyFrequency.getUniqueCount(), is(0));
    }

    @Test
    public void instantiatedWith1Event_has1() {
        weeklyFrequency = new WeeklyFrequency(events(1));

        assertThat(weeklyFrequency.getUniqueCount(), is(1));
    }

    @Test
    public void name_isCorrect() {
        assertThat(WeeklyFrequency.name(events(1).get(0)), is("Sun:11"));
    }

    @Test
    public void instantiatedFromArrayOfPrimaryKeys_selectsUsingTheKey() {
        doReturn(events(1).get(0)).when(mEventTable).select1((Database) any(), eq(mEventTablePrimaryKey));
        weeklyFrequency = WeeklyFrequency.from(new EventTable.PrimaryKey[]{mEventTablePrimaryKey});

        verify(mEventTable, times(1)).select1((Database) any(), eq(mEventTablePrimaryKey));
    }

    private List<EventTable.Row> events(int count) {
        List<EventTable.Row> events = new ArrayList<>(count);

        for (int i = 0; i < count && i < dateTimeStrings.length; i++) {
            events.add(new EventTable.Row(
                    new EventTypeTable.PrimaryKey(i),
                    dateTimes.get(i),
                    dateTimes.get(i),
                    null,
                    "Event " + i
            ));
        }

        return events;
    }
}
