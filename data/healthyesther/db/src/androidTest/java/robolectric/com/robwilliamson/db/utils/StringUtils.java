package robolectric.com.robwilliamson.db.utils;

import com.google.common.collect.Lists;
import com.robwilliamson.db.BuildConfig;
import com.robwilliamson.db.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

//@RunWith(Enclosed.class)
public class StringUtils {

    /**
     * To string object.
     */
    private static class To {
        private final String mValue;

        public static To string(String value) {
            return new To(value);
        }

        public To(String value) {
            mValue = value;
        }

        @Override
        public String toString() {
            return mValue;
        }
    }

    public static class Join {

        @Config(manifest = "./src/main/AndroidManifest.xml",
                emulateSdk = BuildConfig.EMULATE_SDK)
        @RunWith(ParameterizedRobolectricTestRunner.class)
        public static class ObjectListTest {
            private Object[] mList;
            private String mSeparator;
            private String mExpected;

            @ParameterizedRobolectricTestRunner.Parameters(
                    name = "{index}: list {0}, separator \"{1}\", expects \"{2}\""
            )
            public static Collection<Object[]> data() {
                return Arrays.asList(new Object[][]{
                        {new String[]{"Hello", "world!"}, " ", "Hello world!"},
                        {null, " ", ""},
                        {null, null, ""},
                        {new Object[]{"Hello", To.string("world!")}, null, "Hellonullworld!"},
                        {new Object[]{"Hello", null, To.string("world!")}, " ", "Hello null world!"},
                });
            }

            public ObjectListTest(Object[] list, String separator, String expected) {
                mList = list;
                mSeparator = separator;
                mExpected = expected;
            }

            @Test
            public void test() {
                assertThat(Utils.join(mList, mSeparator), equalTo(mExpected));
            }
        }

        @Config(manifest = "./src/main/AndroidManifest.xml",
                emulateSdk = BuildConfig.EMULATE_SDK)
        @RunWith(ParameterizedRobolectricTestRunner.class)
        public static class JoinNullArrayListTest {
            private String mSeparator;
            private boolean mJoinNull;
            private ArrayList<? extends Object> mList;
            private String mExpected;

            @ParameterizedRobolectricTestRunner.Parameters(
                    name = "{index}: separator \"{0}\", join null: {1}, list {2}, expects \"{3}\""
            )
            public static Collection<Object[]> data() {
                return Arrays.asList(new Object[][]{
                        {", ", true, Lists.newArrayList(new String[]{"Mak", "mak", "mak!"}), "Mak, mak, mak!"},
                        {", ", true, Lists.newArrayList(new String[]{"Mak", null, "mak!"}), "Mak, null, mak!"},
                        {", ", true, Lists.newArrayList(new Object[]{To.string("Mak"), null, "mak!"}), "Mak, null, mak!"},
                        {", ", false, Lists.newArrayList(new String[]{"Mak", "mak", "mak!"}), "Mak, mak, mak!"},
                        {", ", false, Lists.newArrayList(new String[]{"Mak", null, "mak!"}), "Mak, mak!"},
                        {", ", false, Lists.newArrayList(new Object[]{To.string("Mak"), null, "mak!"}), "Mak, mak!"},
                });
            }

            public JoinNullArrayListTest(String separator, boolean joinNull, ArrayList<? extends Object> list, String expected) {
                mSeparator = separator;
                mJoinNull = joinNull;
                mList = list;
                mExpected = expected;
            }

            @Test
            public void test() {
                assertThat(Utils.join(mSeparator, mJoinNull, mList), equalTo(mExpected));
            }
        }
    }
}
