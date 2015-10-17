package moose.com.ac;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import moose.com.ac.ui.CloudCollectFragment;
import moose.com.ac.ui.LocalCollectFragment;
import moose.com.ac.util.ZoomOutPageTransformer;

/**
 * Created by dell on 2015/10/17.
 * sync data between cloud(server) and local
 */
public class SynchronizeActivity extends AppCompatActivity {
    private static final String TAG = "SynchronizeActivity";

    private ViewPager viewPager;
    private FloatingActionButton fab;
    private TabLayout tabLayout;
    private Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.include_list_viewpager);
        initView();
        initData();
    }

    private void initData() {
        adapter = new Adapter(getSupportFragmentManager());
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(R.string.sync_ac);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        tabLayout.setupWithViewPager(viewPager);
        fab.setImageResource(R.drawable.ic_sync_blacy24px);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        //noinspection ConstantConditions
        ab.setDisplayHomeAsUpEnabled(true);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(new LocalCollectFragment(), getString(R.string.local_store));
        adapter.addFragment(new CloudCollectFragment(), getString(R.string.cloud_store));
        viewPager.setAdapter(adapter);
    }

    public class Adapter extends FragmentPagerAdapter {
        private FragmentManager fragmentManager;
        private final List<Fragment> mFragments = new ArrayList<>();
        private List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
            this.fragmentManager = fm;
        }


        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        public Fragment getFragment(int position) {
            return mFragments.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
