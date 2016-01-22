package moose.com.ac;

import android.app.Dialog;
import android.app.SearchManager;
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
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SearchView;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import moose.com.ac.about.AboutActivity;
import moose.com.ac.common.Config;
import moose.com.ac.retrofit.Api;
import moose.com.ac.settings.SettingsActivity;
import moose.com.ac.sync.SynchronizeActivity;
import moose.com.ac.ui.ArticleFragment;
import moose.com.ac.ui.widget.CircleImageView;
import moose.com.ac.util.CommonUtil;
import moose.com.ac.util.RxUtils;
import moose.com.ac.util.ZoomOutPageTransformer;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
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
 * when intent another activity,need cancel network request
 * SearchView see http://stackoverflow.com/questions/27556623/creating-a-searchview-that-looks-like-the-material-design-guidelines
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private FloatingActionButton fab;
    private NavigationView navigationView;
    private SearchView searchView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private AppCompatTextView userName;
    private CircleImageView logo;
    private Adapter adapter;
    private int type = 0; /*orderBy 0：最近 1：人气最旺 3：评论最多*/

    private boolean searchShow = false;

    private CompositeSubscription cscription = new CompositeSubscription();
    private Api api = RxUtils.createApi(Api.class, Config.GITHUB_URL);

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.login_username:
                    if (CommonUtil.getLoginStatus().equals(Config.LOGIN_IN)) {
                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                    break;
                case R.id.fab:
                    adapter.getFragment(viewPager.getCurrentItem()).scrollToTop();
                    break;
                default:
                    break;
            }
        }
    };

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
        //new Handler().postDelayed(this::checkVersion, Config.TIME_LATE);
        initView();
        initData();
    }

    @Deprecated
    private void checkVersion() {
        cscription.add(api.receiveVeision()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "--onError--");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ResponseBody response) {
                        Log.e(TAG, "--onNext--");
                        rx.Observable.create(subscriber -> {
                            try {
                                String result = CommonUtil.slurp(response.byteStream(), 256);
                                Log.e(TAG, "result:" + result);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            subscriber.onNext("0");
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(s -> {
                                    Log.e(TAG, "--complete--");
                                });
                    }
                }));
    }

    private void initData() {
        adapter = new Adapter(getSupportFragmentManager());
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(getToolBarTitle());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        fab.setOnClickListener(mOnClickListener);
        tabLayout.setupWithViewPager(viewPager);
        userName.setOnClickListener(mOnClickListener);
        userName.setText(CommonUtil.getUserName());
        Glide.with(this)
                .load(CommonUtil.getUserLogo())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(logo);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View drawerHeader = navigationView.inflateHeaderView(R.layout.nav_header);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        logo = (CircleImageView) drawerHeader.findViewById(R.id.login_userimg);
        userName = (AppCompatTextView) drawerHeader.findViewById(R.id.login_username);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /**see http://stackoverflow.com/questions/27556623/creating-a-searchview-that-looks-like-the-material-design-guidelines*/
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnSearchClickListener(view -> searchShow = true);
        searchView.setOnCloseListener(() -> {
            searchShow = false;
            return false;
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!CommonUtil.isEmpty(query)) {
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra(Config.SEARCH_KEY, query);
                    startActivity(intent);
                    searchView.onActionViewCollapsed();
                    searchShow = false;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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
            case R.id.action_filter:
                bulidDialog().show();
                return true;
            case R.id.action_search:
                mDrawerToggle.onDrawerSlide(mDrawerLayout, 1);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (searchShow) {
            searchView.onActionViewCollapsed();
            searchShow = false;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        cscription = RxUtils.getNewCompositeSubIfUnsubscribed(cscription);
        navigationView.setCheckedItem(R.id.nav_home);
        if (!userName.getText().equals(CommonUtil.getUserName())) {
            userName.setText(CommonUtil.getUserName());
            Glide.with(this)
                    .load(CommonUtil.getUserLogo())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(logo);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        RxUtils.unsubscribeIfNotNull(cscription);
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

    public void resume() {
        super.onResume();
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
                    navigationView.setCheckedItem(R.id.nav_home);
                    break;
                case R.id.nav_about:
                    navigationView.setCheckedItem(R.id.nav_about);
                    Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nav_store:
                    navigationView.setCheckedItem(R.id.nav_store);
                    Intent intents = new Intent(MainActivity.this, CollectsActivity.class);
                    startActivity(intents);
                    break;
                case R.id.nav_history:
                    navigationView.setCheckedItem(R.id.nav_history);
                    Intent intenth = new Intent(MainActivity.this, HistoryActivity.class);
                    startActivity(intenth);
                    break;
                case R.id.nav_setting:
                    navigationView.setCheckedItem(R.id.nav_setting);
                    Intent intent1s = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent1s);
                    break;
                case R.id.nav_checkin:
                    navigationView.setCheckedItem(R.id.nav_checkin);
                    Intent intentIn = CommonUtil.getLoginStatus().equals(Config.LOGIN_IN) ?
                            new Intent(MainActivity.this, ProfileActivity.class) :
                            new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intentIn);
                    break;
                case R.id.nav_sync:
                    navigationView.setCheckedItem(R.id.nav_sync);
                    Intent intentS = new Intent(MainActivity.this, SynchronizeActivity.class);
                    startActivity(intentS);
                    break;
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
                Log.i(TAG, "i:" + i);
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
                .setItems(R.array.select_channel_array, (DialogInterface.OnClickListener) (dialog, which) -> {
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
            return getString(R.string.most_comment);
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

    public void snack(String msg) {
        Snackbar snackBar = Snackbar.make(viewPager, msg, Snackbar.LENGTH_SHORT);
        snackBar.setAction(R.string.snackbar_action, v -> {
            snackBar.dismiss();
        });
        snackBar.getView().setBackgroundResource(R.color.colorPrimary);
        snackBar.show();
    }
}