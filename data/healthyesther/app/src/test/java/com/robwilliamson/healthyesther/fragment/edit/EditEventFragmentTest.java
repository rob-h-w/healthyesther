package com.robwilliamson.healthyesther.fragment.edit;

import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.db.generated.EventTable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class EditEventFragmentTest {
    @Mock
    private EventTable.Row mRow;

    @InjectMocks
    private EditEventFragment mEditEventFragment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getRowWhenRowIsSet_returnsRow() {
        assertThat(mEditEventFragment.getRow(), is(mRow));
    }
}
