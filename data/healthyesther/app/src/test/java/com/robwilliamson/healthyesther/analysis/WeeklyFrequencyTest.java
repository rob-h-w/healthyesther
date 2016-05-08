/**
 * © Robert Williamson 2014-2016.
 * This program is distributed under the terms of the GNU General Public License.
 */
package com.robwilliamson.healthyesther.analysis;

import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.EventTypeTable;
import com.robwilliamson.healthyesther.db.includes.DateTime;
import com.robwilliamson.healthyesther.db.integration.DateTimeConverter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
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
            org.joda.time.DateTime jTime = Utils.Time.fromLocalString(dateTime);
            dateTimes.add(DateTime.from(jTime));
        }
    }

    private WeeklyFrequency weeklyFrequency;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
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
        assertThat(WeeklyFrequency.name(events(1).get(0)), is("7:11"));
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
