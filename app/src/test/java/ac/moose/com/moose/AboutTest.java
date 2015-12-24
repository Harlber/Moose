package ac.moose.com.moose;

import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import ac.moose.com.robolectric.CustomBuildConfig;
import ac.moose.com.robolectric.MooseCustomRobolectricGradleTestRunner;
import moose.com.ac.About;
import moose.com.ac.R;

import static org.assertj.android.api.Assertions.assertThat;

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
