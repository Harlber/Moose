package moose.com.ac.data;

import android.annotation.SuppressLint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import ac.moose.com.robolectric.CustomBuildConfig;
import ac.moose.com.robolectric.MooseCustomRobolectricGradleTestRunner;
import moose.com.ac.R;
import moose.com.ac.settings.SettingsActivity;

/**
 * Created by dell on 2016/7/18.
 * Multi-language test
 */
@RunWith(MooseCustomRobolectricGradleTestRunner.class)
@Config(constants = CustomBuildConfig.class, sdk = 21, packageName = "moose.com.ac.data")
public class LanguageTest {
    private SettingsActivity settingsActivity;

    @Before
    public void setUp() {
        settingsActivity = Robolectric.setupActivity(SettingsActivity.class);
    }

    @SuppressLint("Assert")
    @Test
    @Config(qualifiers = "zh-rCN")
    public void zh_rCN_test() {
        assert(settingsActivity.getString(R.string.login).equals("登录"));
    }

    @SuppressLint("Assert")
    @Test
    @Config(qualifiers = "en-rUS")
    public void en_rUS_test() {
        assert(settingsActivity.getString(R.string.login).equals("Login"));
    }

}
