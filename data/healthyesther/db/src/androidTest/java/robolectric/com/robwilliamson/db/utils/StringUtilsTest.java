package robolectric.com.robwilliamson.db.utils;

import com.robwilliamson.db.Utils;
import com.robwilliamson.db.BuildConfig;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class StringUtilsTest {

    @Config(manifest = "./src/main/AndroidManifest.xml",
            emulateSdk= BuildConfig.EMULATE_SDK)
    @RunWith(RobolectricTestRunner.class)
    public static class JoinObjectListTest {
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

        @Test
        public void testStringListSpace() {
            String[] list = {"Hello", "world!"};
            String joined = Utils.join(list, " ");
            assertThat(joined, equalTo("Hello world!"));
        }

        @Test
        public void testNullSpace() {
            String joined = Utils.join(null, " ");
            assertThat(joined, equalTo(""));
        }

        @Test
        public void testNullNull() {
            String joined = Utils.join(null, null);
            assertThat(joined, equalTo(""));
        }

        @Test
        public void testStringObjectStringNull() {
            Object[] list = {"Hello",
                    new ToStringObject("world!")};
            String joined = Utils.join(list, null);
            assertThat(joined, equalTo("Hellonullworld!"));
        }

        @Test
        public void testStringObjectNullStringSpace() {
            Object[] list = {"Hello",
                    null,
                    new ToStringObject("world!")};
            String joined = Utils.join(list, " ");
            assertThat(joined, equalTo("Hello null world!"));
        }
    }
}
