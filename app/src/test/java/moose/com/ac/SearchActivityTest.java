package moose.com.ac;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import ac.moose.com.robolectric.CustomBuildConfig;
import ac.moose.com.robolectric.MooseCustomRobolectricGradleTestRunner;
import moose.com.ac.ui.SearchFragment;

import static junit.framework.Assert.assertEquals;
import static org.assertj.android.api.Assertions.assertThat;

/**
 * Created by dell on 2016/1/22.
 */
@RunWith(MooseCustomRobolectricGradleTestRunner.class)
@Config(constants = CustomBuildConfig.class, sdk = 21, packageName = "moose.com.ac", application = TestAppApplication.class)
public class SearchActivityTest {
    private SearchActivity activity;
    private SearchFragment searchFragment;
    private FragmentManager fragmentManager;

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(SearchActivity.class);
        fragmentManager = activity.getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString(moose.com.ac.common.Config.SEARCH_KEY, "暴走");
        searchFragment = new SearchFragment();
        searchFragment.setArguments(bundle);
    }

    @Test
    public void containsFragment() {
        fragmentManager.beginTransaction().replace(R.id.search_framelayout, searchFragment)
                .addToBackStack("SearchFragmentTest")
                .commit();
        assertThat(activity.getFragmentManager()).hasBackStackEntryCount(0);//bug here   v4 Fragment & Fragment
    }

    @Test
    public void testSearchCreate() {
        Intent intent = new Intent();
        intent.putExtra(moose.com.ac.common.Config.SEARCH_KEY, "暴走");
        activity = Robolectric.buildActivity(SearchActivity.class).create().start().get();
        assertEquals(activity.getIntent().getStringExtra(moose.com.ac.common.Config.SEARCH_KEY), null);
        activity = Robolectric.buildActivity(SearchActivity.class).withIntent(intent).create().start().get();
        assertEquals("暴走", activity.getIntent().getStringExtra(moose.com.ac.common.Config.SEARCH_KEY));
    }

    @After
    public void tearDown() {
        //controller.pause().stop().destroy();
    }
}
