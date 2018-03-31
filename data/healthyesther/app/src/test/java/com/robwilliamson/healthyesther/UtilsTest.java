/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class UtilsTest {
    @Mock
    private Throwable mThrowable;

    @Mock
    private RadioGroup mRadioGroup;

    @Mock
    private Utils.View.RadioButtonHandler mRadioButtonHandler;

    @Mock
    private RadioButton mRadioButton;

    @Mock
    private Button mButton;

    @Mock
    private FragmentManager mFragmentManager;

    @Mock
    private Fragment mFragment;

    @Mock
    private Thread mThread;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void formatNoCause_writesMessageClassAndStacktrace() {
        String message = "message";
        StackTraceElement[] stackTrace = new StackTraceElement[]{new StackTraceElement("cls", "method", "file", 1)};

        doReturn(message).when(mThrowable).getMessage();
        doReturn(stackTrace).when(mThrowable).getStackTrace();

        String formatted = Utils.format(mThrowable);

        String[] lines = formatted.split("/n");

        assertThat(lines.length, is(3));
        assertThat(lines[0], is(message));
        assertThat(lines[1], is(String.valueOf(mThrowable.getClass())));
        assertThat(lines[2], is(String.valueOf(stackTrace[0])));
    }

    @Test
    public void formatCause_writesCauseToo() {
        String message = "message";
        StackTraceElement[] stackTrace = new StackTraceElement[]{new StackTraceElement("cls", "method", "file", 2)};
        Throwable cause = mock(Throwable.class);

        doReturn(message).when(mThrowable).getMessage();
        doReturn(stackTrace).when(mThrowable).getStackTrace();
        doReturn(cause).when(mThrowable).getCause();

        doReturn(stackTrace).when(cause).getStackTrace();

        String formatted = Utils.format(mThrowable);

        String[] lines = formatted.split("/n");

        assertThat(lines.length, is(7));
        assertThat(lines[0], is(message));
        assertThat(lines[1], is(String.valueOf(mThrowable.getClass())));
        assertThat(lines[2], is(String.valueOf(stackTrace[0])));
        assertThat(lines[3], is("Caused by:"));
        assertThat(lines[4], is("null"));
        assertThat(lines[5], is(String.valueOf(cause.getClass())));
        assertThat(lines[6], is(String.valueOf(stackTrace[0])));
    }

    @Test(expected = NullPointerException.class)
    public void checkNotNullWithNull_throwsNpe() {
        Utils.checkNotNull(null);
    }

    @Test
    public void checkNotNullWithNonNull_returnsValue() {
        String test = "test";
        assertThat(Utils.checkNotNull(test), is(test));
    }

    @Test
    public void checkAssignableWithNull_returnsNull() {
        assertThat(Utils.checkAssignable(null, String.class), is((String) null));
    }

    @Test(expected = ClassCastException.class)
    public void checkAssignableWithNonAssignable_throws() {
        Utils.checkAssignable("hello", Integer.class);
    }

    @Test
    public void checkAssignableWithAssignable_returnsAssignedType() {
        String hello = "hello";
        CharSequence sequence = hello;
        String result = Utils.checkAssignable(sequence, String.class);

        assertThat(result, is(hello));
    }

    @Test
    public void checkedCastWithNull_returnsNull() {
        assertThat(Utils.checkedCast(null, String.class), is((String) null));
    }

    @Test
    public void view_forEachRadioButtonNoButtons_getsCountOnly() {
        doReturn(0).when(mRadioGroup).getChildCount();

        Utils.View.forEachRadioButton(mRadioGroup, mRadioButtonHandler);

        verify(mRadioGroup).getChildCount();
        verifyZeroInteractions(mRadioButtonHandler);
    }

    @Test
    public void view_forEachRadioButtonChildNotRadioButton_getsCountOnly() {
        doReturn(1).when(mRadioGroup).getChildCount();
        doReturn(mButton).when(mRadioGroup).getChildAt(0);

        Utils.View.forEachRadioButton(mRadioGroup, mRadioButtonHandler);

        verify(mRadioGroup).getChildCount();
        verifyZeroInteractions(mRadioButtonHandler);
    }

    @Test
    public void view_forEachRadioButtonHeterogeneous_skipsNonRadioButtonViews() {
        doReturn(3).when(mRadioGroup).getChildCount();
        doReturn(mButton).when(mRadioGroup).getChildAt(0);
        doReturn(mRadioButton).when(mRadioGroup).getChildAt(1);
        doReturn(mButton).when(mRadioGroup).getChildAt(2);
        doReturn(true).when(mRadioButtonHandler).handleRadioButton(mRadioButton);

        Utils.View.forEachRadioButton(mRadioGroup, mRadioButtonHandler);

        verify(mRadioButtonHandler).handleRadioButton(mRadioButton);
    }

    @Test
    public void view_forEachRadioButtonHandlerReturnsFalse_skipsRemainder() {
        doReturn(2).when(mRadioGroup).getChildCount();
        doReturn(mRadioButton).when(mRadioGroup).getChildAt(0);
        doReturn(mRadioButton).when(mRadioGroup).getChildAt(1);
        doReturn(false).when(mRadioButtonHandler).handleRadioButton(mRadioButton);

        Utils.View.forEachRadioButton(mRadioGroup, mRadioButtonHandler);

        verify(mRadioButtonHandler).handleRadioButton(mRadioButton);
    }

    @Test
    public void view_getTypeSafeView_returnsView() {
        doReturn(mButton).when(mRadioButton).findViewById(0);

        Button button = Utils.View.getTypeSafeView(mRadioButton, 0, Button.class);

        assertThat(button, is(mButton));
    }

    @Test
    public void view_getTypeSafeFragmentWithTag_returnsFragment() {
        String tag = "tag";
        doReturn(mFragment).when(mFragmentManager).findFragmentByTag(tag);
        assertThat(Utils.View.getTypeSafeFragment(mFragmentManager, tag, Fragment.class), is(mFragment));
    }

    @Test
    public void view_getTypeSafeFragmentWithId_returnsFragment() {
        int id = 1;
        //noinspection ResourceType
        doReturn(mFragment).when(mFragmentManager).findFragmentById(id);
        assertThat(Utils.View.getTypeSafeFragment(mFragmentManager, id, Fragment.class), is(mFragment));
    }

    @Test
    public void view_assertIsOnUiThreadWhenOnUiThread_doesNothing() {
        App.setsUiThreadId(0);
        doReturn(1L).when(mThread).getId();
        mThread.setName("main");

        Utils.View.assertIsOnUiThread(mThread);

        verify(mThread, times(2)).getId();
    }

    @Test(expected = Utils.View.NonUiThreadException.class)
    public void view_assertIsOnUiThreadWhenNotOnUiThread_throws() {
        App.setsUiThreadId(123);
        doReturn(1L).when(mThread).getId();

        Utils.View.assertIsOnUiThread(mThread);
    }
}
