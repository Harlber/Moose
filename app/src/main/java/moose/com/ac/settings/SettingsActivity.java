package moose.com.ac.settings;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import moose.com.ac.ui.BaseActivity;
import moose.com.ac.R;
import moose.com.ac.ui.SettingsFragment;
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
 * Created by dell on 2015/9/1.
 * Setting
 */
public class SettingsActivity extends BaseActivity {

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.set_all);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.set_content, new SettingsFragment())/*android.R.id.content*/
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                SettingsActivity.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
