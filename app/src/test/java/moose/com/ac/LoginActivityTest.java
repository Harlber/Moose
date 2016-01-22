package moose.com.ac;

import android.annotation.SuppressLint;
import android.support.v7.widget.AppCompatEditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import ac.moose.com.robolectric.CustomBuildConfig;
import ac.moose.com.robolectric.MooseCustomRobolectricGradleTestRunner;
import moose.com.ac.ui.widget.EmailEditText;
import moose.com.ac.util.CommonUtil;
import moose.com.ac.util.PreferenceUtil;
import static org.assertj.android.api.Assertions.assertThat;
/*
 * Copyright Farble Dast. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Created by dell on 2015/12/9.
 * Unit Test for {@link LoginActivity}
 */
@RunWith(MooseCustomRobolectricGradleTestRunner.class)
@Config(constants = CustomBuildConfig.class, sdk = 21,packageName = "moose.com.ac")
public class LoginActivityTest {
    private LoginActivity activity;
    private EmailEditText emailEditText;
    private AppCompatEditText appCompatEditText;
    @Test
    public void clickingLogin_shouldGetLoginStatus() {
        emailEditText.setText("username");
        appCompatEditText.setText("123456");
        activity.findViewById(R.id.login_submit).performClick();
        assertThat(PreferenceUtil.getPreferences()).contains(moose.com.ac.common.Config.LOGIN_STATUS,"LOGIN_STATUS");
    }

    @Before
    public void setUp(){
        activity = Robolectric.setupActivity(LoginActivity.class);
        emailEditText = (EmailEditText) activity.findViewById(R.id.login_name);
        appCompatEditText = (AppCompatEditText) activity.findViewById(R.id.login_pwd);
        CommonUtil.setLoginStatus(moose.com.ac.common.Config.LOGIN_STATUS);
    }

    @SuppressLint("Assert")
    @Test
    public void clickLogin_emptyUsername(){
        appCompatEditText.setText("123456");
        activity.findViewById(R.id.login_submit).performClick();
        assert(!activity.check());
    }

    @SuppressLint("Assert")
    @Test
    public void clickLogin_emptyPassword(){
        emailEditText.setText("username");
        activity.findViewById(R.id.login_submit).performClick();
        assert(!activity.check());
    }
}
