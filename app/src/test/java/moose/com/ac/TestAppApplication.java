package moose.com.ac;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import ac.moose.com.robolectric.CustomBuildConfig;
import ac.moose.com.robolectric.MooseCustomRobolectricGradleTestRunner;

/**
 * Created by dell on 2016/1/21.
 * See <StackOverFlow href = "http://stackoverflow.com/questions/30169678/nullpointerexception-from-leakcanary-when-running-robolectric-tests"/>
 * See <StackOverFlow href = "https://github.com/square/leakcanary/issues/143"/>
 */
@RunWith(MooseCustomRobolectricGradleTestRunner.class)
@Config(constants = CustomBuildConfig.class, sdk = 21,packageName = "moose.com.ac")
public class TestAppApplication extends AppApplication{
    @Test
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected boolean isInUnitTests() {
        return true;
    }
}
