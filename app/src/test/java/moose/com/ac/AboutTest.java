package moose.com.ac;

import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import ac.moose.com.robolectric.CustomBuildConfig;
import ac.moose.com.robolectric.MooseCustomRobolectricGradleTestRunner;

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
 * Unit Test for {@link About}
 */

@RunWith(MooseCustomRobolectricGradleTestRunner.class)
@Config(constants = CustomBuildConfig.class, sdk = 21,packageName = "moose.com.ac")
public class AboutTest {
    @Test
    public void test() {
        About activity = Robolectric.setupActivity(About.class);
        assertThat((TextView) activity.findViewById(R.id.text_application_info)).containsText("Version");
        assertThat((TextView) activity.findViewById(R.id.text_developer_info)).containsText("咲くやこの花");
        assertThat((TextView) activity.findViewById(R.id.text_libraries)).isNotEmpty();
        assertThat((TextView) activity.findViewById(R.id.text_libraries)).containsText("Square");
        assertThat((TextView) activity.findViewById(R.id.text_3rd_party_licenses)).containsText("License");
    }
}
