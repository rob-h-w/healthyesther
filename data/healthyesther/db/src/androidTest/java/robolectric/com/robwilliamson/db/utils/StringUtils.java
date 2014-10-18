package robolectric.com.robwilliamson.db.utils;

import com.robwilliamson.db.BuildConfig;
import com.robwilliamson.db.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

//@RunWith(Enclosed.class)
public class StringUtils {

    private static class ToStringObject {
        private final String mValue;

        public ToStringObject(String value) {
            mValue = value;
        }

        @Override
        public String toString() {
            return mValue;
        }
    }

    @Config(manifest = "./src/main/AndroidManifest.xml",
            emulateSdk= BuildConfig.EMULATE_SDK)
    @RunWith(ParameterizedRobolectricTestRunner.class)
    public static class JoinObjectListTest {
        private Object[] mList;
        private String mSeparator;
        private String mExpected;

        @ParameterizedRobolectricTestRunner.Parameters(
                name = "{index}: mList {0}, mSeparator \"{1}\", expects \"{2}\""
        )
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    { new String[]{"Hello", "world!"}, " ", "Hello world!" },
                    { null, " ", "" },
                    { null, null, "" },
                    { new Object[]{"Hello", new ToStringObject("world!")}, null, "Hellonullworld!" },
                    { new Object[]{"Hello", null, new ToStringObject("world!")}, " ", "Hello null world!" }
            });
        }

        public JoinObjectListTest(Object[] list, String separator, String expected) {
            mList = list;
            mSeparator = separator;
            mExpected = expected;
        }

        @Test
        public void test() {
            assertThat(Utils.join(mList, mSeparator), equalTo(mExpected));
        }
    }
}
