package moose.com.ac;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import moose.com.ac.about.AboutActivity;
import moose.com.ac.common.Config;
import moose.com.ac.settings.SettingsActivity;
import moose.com.ac.sync.SynchronizeActivity;
import moose.com.ac.ui.BaseActivity;
import moose.com.ac.ui.widget.CircleImageView;
import moose.com.ac.util.CommonUtil;
import moose.com.ac.util.SettingPreferences;
import moose.com.ac.util.chrome.CustomTabActivityHelper;
import moose.com.ac.util.chrome.WebviewFallback;
/*
 * Copyright 2015,2016 Farble Dast
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
public class MainActivity extends BaseActivity {
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private SearchView searchView;
    private AppCompatTextView userName;
    private CircleImageView logo;
    private LinearLayout linearLayout;

    private boolean searchShow = false;

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(getString(R.string.login_action))) {
                userName.setText(CommonUtil.getUserName());
                Glide.with(mContext)
                        .load(CommonUtil.getUserLogo())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(logo);
            }
        }
    };

    private View.OnClickListener mOnClickListener = view -> {
        switch (view.getId()) {
            case R.id.login_username:
                if (CommonUtil.getLoginStatus().equals(Config.LOGIN_IN)) {
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    intent.putExtra(Config.EXTRA_ADD_ACCOUNT, true);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                break;
            case R.id.tv_complex:
                intentActivity(ChannelManager.COMPLEX);
                break;
            case R.id.tv_hot:
                intentActivity(ChannelManager.HOT);
                break;
            case R.id.tv_work:
                intentActivity(ChannelManager.WORK_EMOTION);
                break;
            case R.id.tv_animation:
                intentActivity(ChannelManager.ANIMATION_CULTURE);
                break;
            case R.id.tv_cartoon:
                intentActivity(ChannelManager.COMIC_FICTION);
                break;
            case R.id.tv_game:
                intentActivity(ChannelManager.GAME);
                break;
            default:
                break;
        }
    };

    @Override
    protected void onInitView(Bundle savedInstanceState) {
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

    private void initData() {
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
        logo = (CircleImageView) drawerHeader.findViewById(R.id.login_userimg);
        userName = (AppCompatTextView) drawerHeader.findViewById(R.id.login_username);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout);

        TextView textViewComplex = (TextView) findViewById(R.id.tv_complex);
        TextView textViewHot = (TextView) findViewById(R.id.tv_hot);
        TextView textViewWork = (TextView) findViewById(R.id.tv_work);
        TextView textViewAnimation = (TextView) findViewById(R.id.tv_animation);
        TextView textViewCartoon = (TextView) findViewById(R.id.tv_cartoon);
        TextView textViewGame = (TextView) findViewById(R.id.tv_game);

        textViewComplex.setOnClickListener(mOnClickListener);
        textViewHot.setOnClickListener(mOnClickListener);
        textViewWork.setOnClickListener(mOnClickListener);
        textViewAnimation.setOnClickListener(mOnClickListener);
        textViewCartoon.setOnClickListener(mOnClickListener);
        textViewGame.setOnClickListener(mOnClickListener);

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
            case R.id.action_search:
                mDrawerToggle.onDrawerSlide(mDrawerLayout, 1);
                return true;
            case R.id.action_fetch:
                fetchDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void intentActivity(@ChannelManager.ChannelMode int channel) {
        Intent intent = new Intent(mContext, ChannelItemListActivity.class);
        intent.putExtra(Config.CHANNEL_ID, channel);
        startActivity(intent);
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
        navigationView.setCheckedItem(R.id.nav_home);
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
                case R.id.nav_check_in:
                    navigationView.setCheckedItem(R.id.nav_check_in);
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

    private void fetchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_fetch, null);
        builder.setView(rootView)
                // Add action buttons
                .setTitle(R.string.fetch)
                .setPositiveButton(R.string.positive, (dialog, id) -> {
                    AppCompatEditText input = (AppCompatEditText) rootView.findViewById(R.id.dialog_fetch_input);
                    if (!input.getText().toString().equals("")) {
                        if (!SettingPreferences.externalBrowserEnabled(MainActivity.this)) {
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            Uri content_url = Uri.parse(Config.WEB_URL + input.getText().toString() + "/");
                            intent.setData(content_url);
                            startActivity(intent);
                        } else {
                            String url = Config.WAP_URL + "v#ac=" + input.getText().toString() + ";type=article";
                            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
                            CustomTabActivityHelper.openCustomTab(
                                    MainActivity.this, customTabsIntent, Uri.parse(url), new WebviewFallback());
                        }
                    }
                });
        builder.show();
    }

    @Deprecated
    public void snack(String msg) {
        Snackbar snackBar = Snackbar.make(linearLayout, msg, Snackbar.LENGTH_SHORT);
        snackBar.setAction(R.string.snackbar_action, v -> {
            snackBar.dismiss();
        });
        snackBar.getView().setBackgroundResource(R.color.colorPrimary);
        snackBar.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mBroadcastReceiver,
                new IntentFilter(getString(R.string.login_action)));
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }
}