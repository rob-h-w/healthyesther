package com.robwilliamson.healthyesther;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class UtilsTest {
    @Mock
    private Throwable mThrowable;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void formatNoCause_writesMessageClassAndStacktrace() {
        String message = "message";
        StackTraceElement[] stackTrace = new StackTraceElement[] { new StackTraceElement("cls", "method", "file", 1) };

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
        StackTraceElement[] stackTrace = new StackTraceElement[] { new StackTraceElement("cls", "method", "file", 2) };
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
}
