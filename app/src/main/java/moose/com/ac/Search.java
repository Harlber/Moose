package moose.com.ac;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import moose.com.ac.common.Config;
import moose.com.ac.ui.SearchFragment;
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
 * Created by dell on 2015/9/15.
 * Search - View
 */
public class Search extends AppCompatActivity {
    private static final String TAG = "Search";
    private SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        //noinspection ConstantConditions
        ab.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        ab.setDisplayHomeAsUpEnabled(true);
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(R.string.search);
        initData();
    }

    private void initData() {
        String key = getIntent().getStringExtra(Config.SEARCH_KEY);
        Bundle bundle = new Bundle();
        bundle.putString(Config.SEARCH_KEY, key);
        searchFragment = new SearchFragment();
        searchFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.search_framelayout, searchFragment)
                .commit();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Search.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
