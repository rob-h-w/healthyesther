package robolectric.com.robwilliamson.db.utils;

import com.robwilliamson.db.Utils;
import com.robwilliamson.db.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@Config(manifest = "./src/main/AndroidManifest.xml",
        emulateSdk= BuildConfig.EMULATE_SDK)
@RunWith(RobolectricTestRunner.class)
public class StringUtils {
    @Test
    public void testJoin() {
        String[] list = {"Hello", "world!"};
        String joined = Utils.join(list, " ");
        assertThat(joined, equalTo("Hello world!"));
    }
}
