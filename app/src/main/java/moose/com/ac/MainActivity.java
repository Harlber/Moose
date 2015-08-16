package moose.com.ac;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import moose.com.ac.common.Config;
import moose.com.ac.ui.ArticleFragment;
import moose.com.ac.util.ZoomOutPageTransformer;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Adapter adapter;
    private int type = 0; /*orderBy 0：最近 1：人气最旺 3：评论最多*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        adapter = new Adapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_filter:
                bulidDialog().show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(getArticleFragment(Config.COMPLEX), getString(R.string.complex));
        adapter.addFragment(getArticleFragment(Config.WORK), getString(R.string.work));
        adapter.addFragment(getArticleFragment(Config.ANIMATION), getString(R.string.animation));
        adapter.addFragment(getArticleFragment(Config.CARTOON), getString(R.string.cartoon));
        viewPager.setAdapter(adapter);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    menuItem.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    return true;
                });
    }

    private Bundle setBundle(int key, int ty) {
        Bundle bundle = new Bundle();
        bundle.putInt(Config.CHANNEL_ID, key);
        bundle.putString(Config.CHANNEL_TYPE, String.valueOf(ty));
        return bundle;
    }

    private ArticleFragment getArticleFragment(int key) {
        ArticleFragment fragment = new ArticleFragment();
        fragment.setArguments(setBundle(key, type));
        return fragment;
    }

    public static class Adapter extends FragmentPagerAdapter {
        private FragmentManager fragmentManager;
        private final List<ArticleFragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
            this.fragmentManager = fm;
        }

        public void clearFragments() {
            for (int i=0;i<mFragments.size();i++) {
                fragmentManager.beginTransaction().remove(mFragments.get(i)).commit();
            }
            mFragments.clear();
        }

        public void addFragment(ArticleFragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
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

    private Dialog bulidDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.article_select))
                .setItems(R.array.selest_array, (DialogInterface.OnClickListener) (dialog, which) -> {
                        type = which;
                        //refresh request
                        int position = viewPager.getCurrentItem();
                        adapter.clearFragments();
                        setupViewPager(viewPager);
                    viewPager.setCurrentItem(position);
                });
        return builder.create();
    }

}