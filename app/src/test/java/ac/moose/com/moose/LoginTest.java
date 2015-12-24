package ac.moose.com.moose;

import android.support.v7.widget.AppCompatEditText;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import ac.moose.com.robolectric.CustomBuildConfig;
import ac.moose.com.robolectric.MooseCustomRobolectricGradleTestRunner;
import moose.com.ac.Login;
import moose.com.ac.R;
import moose.com.ac.ui.widget.EmailEditText;

/**
 * Created by dell on 2015/12/9.
 * Unit Test for {@link Login}
 */
@RunWith(MooseCustomRobolectricGradleTestRunner.class)
@Config(constants = CustomBuildConfig.class, sdk = 21,packageName = "moose.com.ac")
public class LoginTest {
    @Test
    public void clickingLogin_shouldGetLoginStatus() {
        Login activity = Robolectric.setupActivity(Login.class);
        EmailEditText emailEditText = (EmailEditText) activity.findViewById(R.id.login_name);
        emailEditText.setText("username");
        AppCompatEditText appCompatEditText = (AppCompatEditText) activity.findViewById(R.id.login_pwd);
        appCompatEditText.setText("123456");
        activity.findViewById(R.id.login_submit).performClick();

        //assertThat(PreferenceUtil.getPreferences()).contains("LOGIN_STATUS", "LOGIN_IN");
    }
}
