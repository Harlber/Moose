package moose.com.ac;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import moose.com.ac.common.Config;
import moose.com.ac.settings.SettingsActivity;
import moose.com.ac.ui.BaseActivity;
import moose.com.ac.ui.CommentListFragment;
import moose.com.ac.util.CommonUtil;

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

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_big_news);
        int contendId = Integer.valueOf(getIntent().getStringExtra(Config.CONTENTID));
        String title = getIntent().getStringExtra(Config.TITLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //noinspection ConstantConditions
        getSupportActionBar().setTitle(CommonUtil.groupTitle(contendId));
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Fragment fragment = getSingleFragment();
        if (fragment == null) {
            fragment = CommentListFragment.newInstance(contendId);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.root_container, fragment)
                    .commit();
        }
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

    @Nullable
    protected Fragment getSingleFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return fragmentManager.findFragmentById(R.id.root_container);
    }
}
