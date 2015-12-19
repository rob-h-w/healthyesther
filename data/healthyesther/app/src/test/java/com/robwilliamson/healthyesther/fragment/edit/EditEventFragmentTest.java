package com.robwilliamson.healthyesther.fragment.edit;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.robwilliamson.healthyesther.BuildConfig;
import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.includes.DateTime;
import com.robwilliamson.healthyesther.db.integration.DateTimeConverter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class EditEventFragmentTest {
    private static final DateTime WHEN = DateTime.from(org.joda.time.DateTime.now());
    private static final String NAME = "Event Name";
    private static final String ANOTHER_NAME = "Another name";

    static {
        DateTimeConverter.now();
    }

    @Mock
    private EventTable.Row mRow;
    @Mock
    private Button mDateButton;
    @Mock
    private AutoCompleteTextView mNameView;
    @Mock
    private Button mTimeButton;
    @InjectMocks
    private TestableEditEventFragment mEditEventFragment;
    @Mock
    private Editable mEditable;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // Mock Row Values
        doReturn(NAME).when(mRow).getName();
        doReturn(WHEN).when(mRow).getWhen();

        // Mock view editable text.
        doReturn(mEditable).when(mNameView).getEditableText();
    }

    @Test
    public void getRowWhenRowIsSet_returnsRow() {
        assertThat(mEditEventFragment.getRow(), is(mRow));
    }

    @Test
    public void setRow_updatesName() {
        doReturn(ANOTHER_NAME).when(mRow).getName();
        mEditEventFragment.setRow(mRow);

        verify(mEditable).clear();
        verify(mEditable).append(ANOTHER_NAME);
    }

    @Test
    public void nameChanged_updatesRow() {
        mEditEventFragment.onResume();
        //noinspection ResultOfMethodCallIgnored
        doReturn(ANOTHER_NAME).when(mEditable).toString();
        //noinspection ConstantConditions
        mEditEventFragment.getTextChangedListener().afterTextChanged(mEditable);

        verify(mRow).setName(ANOTHER_NAME);
    }

    @Test
    public void whenResumed_setsDateOnClickListener() {
        mEditEventFragment.onResume();

        verify(mDateButton).setOnClickListener(any(View.OnClickListener.class));
    }

    @Test
    public void whenResumed_setsTimeOnClickListener() {
        mEditEventFragment.onResume();

        verify(mTimeButton).setOnClickListener(any(View.OnClickListener.class));
    }

    @Test
    public void whenResumed_setsNameOnFocusChangeListener() {
        mEditEventFragment.onResume();

        verify(mNameView).setOnFocusChangeListener(any(View.OnFocusChangeListener.class));
    }

    @Test
    public void whenResumed_setsNameTextChangedListener() {
        mEditEventFragment.onResume();

        verify(mNameView).addTextChangedListener(any(TextWatcher.class));
    }

    @SuppressWarnings("unused")
    @SuppressLint("ValidFragment")
    private static class TestableEditEventFragment extends EditEventFragment {
        private Button mDateButton;
        private AutoCompleteTextView mNameView;
        private Button mTimeButton;
        private TextWatcher mTextWatcher;

        @Override
        protected Button getDateButton() {
            return mDateButton;
        }

        @Override
        protected AutoCompleteTextView getNameView() {
            return mNameView;
        }

        @Override
        protected Button getTimeButton() {
            return mTimeButton;
        }

        @Override
        @Nonnull
        TextWatcher createTextChangedListener() {
            mTextWatcher = super.createTextChangedListener();
            return mTextWatcher;
        }

        @Nullable
        public TextWatcher getTextChangedListener() {
            return mTextWatcher;
        }
    }
}
