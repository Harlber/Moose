package moose.com.ac;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import moose.com.ac.common.Config;
import moose.com.ac.settings.SettingsActivity;
import moose.com.ac.ui.CommentListFragment;
import moose.com.ac.ui.SubmitCommentFragment;
import moose.com.ac.ui.ViewPageAdapter;
import moose.com.ac.util.CommonUtil;
import moose.com.ac.util.ZoomOutPageTransformer;

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
 * Created by Farble on 2015/8/16 13.
 * 整天搞个大新闻
 */
@SuppressWarnings("unused")
public class BigNewsActivity extends AppCompatActivity {
    private static final String TAG = "BigNewsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_news);
        int contendid = getIntent().getIntExtra(Config.CONTENTID, 1);
        String title = getIntent().getStringExtra(Config.TITLE);
        CommentListFragment commentListFragment = new CommentListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Config.CHANNEL_ID, contendid);
        commentListFragment.setArguments(bundle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //noinspection ConstantConditions
        getSupportActionBar().setTitle(CommonUtil.groupTitle(contendid));
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.news_viewpager);
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        if (viewPager != null) {
            adapter.addFragment(commentListFragment);
            adapter.addFragment(new SubmitCommentFragment());
            viewPager.setAdapter(adapter);
        }else {
            Log.e(TAG,"adapter is null");
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
                startActivity(new Intent(BigNewsActivity.this,SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
