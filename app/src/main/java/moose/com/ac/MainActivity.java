package moose.com.ac;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import moose.com.ac.common.Config;
import moose.com.ac.ui.ArticleFragment;
import moose.com.ac.ui.view.SearchBar;
import moose.com.ac.util.ZoomOutPageTransformer;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private Adapter adapter;
    private int type = 0; /*orderBy 0：最近 1：人气最旺 3：评论最多*/

    private SearchBar searchBar;
    private CardView card_search;
    private RelativeLayout view_search;
    private ListView listView, listContainer;
    private EditText edit_text_search;
    private View line_divider, toolbar_shadow;
    private ImageView image_search_back;
    private boolean isSearch = false;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        //noinspection ConstantConditions
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        adapter = new Adapter(getSupportFragmentManager());

        getSupportActionBar().setTitle(getToolBarTitle());

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        searchBar = new SearchBar();
        card_search = (CardView) findViewById(R.id.card_search);
        view_search = (RelativeLayout) findViewById(R.id.view_search);
        edit_text_search = (EditText) findViewById(R.id.edit_text_search);
        listView = (ListView) findViewById(R.id.listView);
        listContainer = (ListView) findViewById(R.id.listContainer);
        line_divider = findViewById(R.id.line_divider);
        toolbar_shadow = findViewById(R.id.toolbar_shadow);

        image_search_back = (ImageView) findViewById(R.id.image_search_back);
        image_search_back.setOnClickListener(view -> {
            searchBar.handleToolBar(MainActivity.this, card_search, toolbar, view_search, listView, edit_text_search, line_divider);
            listContainer.setVisibility(View.GONE);
            toolbar_shadow.setVisibility(View.VISIBLE);
            isSearch = false;
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
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
            case R.id.action_search:
                searchBar.handleToolBar(MainActivity.this, card_search, toolbar, view_search, listView, edit_text_search, line_divider);
                isSearch = true;
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isSearch) {
            searchBar.handleToolBar(MainActivity.this, card_search, toolbar, view_search, listView, edit_text_search, line_divider);
            listContainer.setVisibility(View.GONE);
            toolbar_shadow.setVisibility(View.VISIBLE);
            isSearch = false;
        } else {
            if (viewPager.getCurrentItem() == 0) {
                super.onBackPressed();
            } else {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
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

    public class Adapter extends FragmentPagerAdapter {
        private FragmentManager fragmentManager;
        private final List<ArticleFragment> mFragments = new ArrayList<>();
        private List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
            this.fragmentManager = fm;
        }

        public void changeChannel(int channel) {
            for (int i = 0; i < mFragments.size(); i++) {
                Log.e(TAG,"i:"+i);
                if (mFragments.get(i)!=null) {
                    mFragments.get(i).loadChannel(channel);
                }
            }
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

    @SuppressWarnings("ConstantConditions")
    private Dialog bulidDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        //noinspection RedundantCast
        builder.setTitle(getString(R.string.article_select))
                .setItems(R.array.selest_array, (DialogInterface.OnClickListener) (dialog, which) -> {
                    type = which;
                    getSupportActionBar().setTitle(getToolBarTitle());
                    //refresh request
                    adapter.changeChannel(type);
                });
        return builder.create();
    }

    private String getToolBarTitle() {
        if (type == 0) {
            return getString(R.string.last_comment);
        } else if (type == 1) {
            return getString(R.string.most_views);
        } else {
            return getString(R.string.most_coment);
        }
    }

}