package moose.com.ac;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import moose.com.ac.common.Config;
import moose.com.ac.settings.SettingsActivity;
import moose.com.ac.ui.BaseActivity;
import moose.com.ac.ui.CommentListFragment;
import moose.com.ac.ui.ViewPageAdapter;
import moose.com.ac.util.CommonUtil;
import moose.com.ac.util.ZoomOutPageTransformer;

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
 * Created by Farble on 2015/8/16 13.
 * 整天搞个大新闻
 */
@SuppressWarnings("unused")
public class BigNewsActivity extends BaseActivity {
    private static final String TAG = "BigNewsActivity";
    private ViewPager viewPager;
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(getString(R.string.send_action))) {
                refreshListAfterBroadcastReceiver();
            }
        }
    };

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_big_news);
        int contendId = Integer.valueOf(getIntent().getStringExtra(Config.CONTENTID));
        String title = getIntent().getStringExtra(Config.TITLE);
        CommentListFragment commentListFragment = CommentListFragment.newInstance(contendId);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //noinspection ConstantConditions
        getSupportActionBar().setTitle(CommonUtil.groupTitle(contendId));
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.news_viewpager);
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        adapter.addFragment(commentListFragment);
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.big_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                BigNewsActivity.this.finish();
                return true;
            case R.id.action_set:
                startActivity(new Intent(BigNewsActivity.this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mBroadcastReceiver,
                new IntentFilter(getString(R.string.send_action)));
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    /**
     * refresh store status after receiver BroadcastReceiver
     */
    public void refreshListAfterBroadcastReceiver() {
        viewPager.setCurrentItem(0);
    }

}
