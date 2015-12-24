package ac.moose.com.moose;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import ac.moose.com.robolectric.CustomBuildConfig;

/**
 * Created by dell on 2015/12/23.
 * Test Robolectric work or not
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = CustomBuildConfig.class, sdk = 21)
public class RobolectricTest {
    @Test
    public void test(){
        assert true;
    }
}
