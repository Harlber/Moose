package moose.com.ac;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import moose.com.ac.common.Config;
import moose.com.ac.ui.ArticleFragment;
import moose.com.ac.ui.view.CircleImageView;
import moose.com.ac.ui.view.SearchBar;
import moose.com.ac.util.CommonUtil;
import moose.com.ac.util.ZoomOutPageTransformer;

/**
 * when intent another activity,need cancel network request
 * SearchView see http://stackoverflow.com/questions/27556623/creating-a-searchview-that-looks-like-the-material-design-guidelines
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private FloatingActionButton fab;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private TextView user_name;
    private CircleImageView logo;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            /**https://developer.android.com/intl/zh-cn/samples/ImmersiveMode/src/com.example.android.immersivemode/ImmersiveModeFragment.html
             * https://developer.android.com/training/system-ui/navigation.html*/
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Translucent navigation bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        //noinspection ConstantConditions
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

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

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view ->
                adapter.getFragment(viewPager.getCurrentItem()).getmRecyclerView().smoothScrollToPosition(0));

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        user_name = (TextView) findViewById(R.id.login_username);

        user_name.setOnClickListener(view -> {
                    if (CommonUtil.getLoginStatus().equals(Config.LOGIN_IN)) {
                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(MainActivity.this, Login.class));
                    }
                }
        );
        logo = (CircleImageView) findViewById(R.id.login_userimg);

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

        user_name.setText(CommonUtil.getUserName());
        Glide.with(this)
                .load(CommonUtil.getUserLogo())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(logo);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle == null) {
            return super.onOptionsItemSelected(item);
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(mDrawerLayout)) {
                    mDrawerLayout.closeDrawer(mDrawerLayout);
                } else {
                    mDrawerLayout.openDrawer(mDrawerLayout);
                }
                //mDrawerLayout.openDrawer(GravityCompat.START);
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
                MainActivity.this.finish();
            } else {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!user_name.getText().equals(CommonUtil.getUserName())) {
            user_name.setText(CommonUtil.getUserName());
            Glide.with(this)
                    .load(CommonUtil.getUserLogo())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(logo);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    private void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(getArticleFragment(Config.COMPLEX), getString(R.string.complex));
        adapter.addFragment(getArticleFragment(Config.WORK), getString(R.string.work));
        adapter.addFragment(getArticleFragment(Config.ANIMATION), getString(R.string.animation));
        adapter.addFragment(getArticleFragment(Config.CARTOON), getString(R.string.cartoon));
        viewPager.setAdapter(adapter);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    break;
                case R.id.nav_about:
                    Intent intent = new Intent(MainActivity.this, About.class);
                    startActivity(intent);
                    break;
                case R.id.nav_store:
                    Intent intents = new Intent(MainActivity.this, Collects.class);
                    startActivity(intents);
                    break;
                case R.id.nav_history:
                    Intent intenth = new Intent(MainActivity.this, History.class);
                    startActivity(intenth);
                    break;
                case R.id.nav_setting:
                    Intent intent1s = new Intent(MainActivity.this, Settings.class);
                    startActivity(intent1s);
                    break;
                case R.id.nav_checkin:
                    Intent intentIn = CommonUtil.getLoginStatus().equals(Config.LOGIN_IN) ?
                            new Intent(MainActivity.this, ProfileActivity.class) :
                            new Intent(MainActivity.this, Login.class);
                    startActivity(intentIn);
                default:
                    break;
            }
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
                Log.e(TAG, "i:" + i);
                if (mFragments.get(i) != null) {
                    mFragments.get(i).loadChannel(channel);
                }
            }
        }

        public void addFragment(ArticleFragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        public ArticleFragment getFragment(int position) {
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

    // Same animation that FloatingActionButton.Behavior uses to hide the FAB when the AppBarLayout exits
    public void animateOut() {
        Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
        if (Build.VERSION.SDK_INT >= 14) {
            ViewCompat.animate(fab).scaleX(0.0F).scaleY(0.0F).alpha(0.0F).setInterpolator(INTERPOLATOR).withLayer()
                    .setListener(new ViewPropertyAnimatorListener() {
                        public void onAnimationStart(View view) {
                        }

                        public void onAnimationCancel(View view) {
                        }

                        public void onAnimationEnd(View view) {
                            view.setVisibility(View.GONE);
                        }
                    }).start();
        } else {
            Animation anim = AnimationUtils.loadAnimation(fab.getContext(), R.anim.fab_out);
            anim.setInterpolator(INTERPOLATOR);
            anim.setDuration(200L);
            anim.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    fab.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(final Animation animation) {
                }
            });
            fab.startAnimation(anim);
        }
    }

    // Same animation that FloatingActionButton.Behavior uses to show the FAB when the AppBarLayout enters
    public void animateIn() {
        Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
        fab.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= 14) {
            ViewCompat.animate(fab).scaleX(1.0F).scaleY(1.0F).alpha(1.0F)
                    .setInterpolator(INTERPOLATOR).withLayer().setListener(null)
                    .start();
        } else {
            Animation anim = AnimationUtils.loadAnimation(fab.getContext(), R.anim.fab_in);
            anim.setDuration(200L);
            anim.setInterpolator(INTERPOLATOR);
            fab.startAnimation(anim);
        }
    }

    public void Snack(String msg) {
        Snackbar.make(viewPager, msg, Snackbar.LENGTH_SHORT).show();
    }

}