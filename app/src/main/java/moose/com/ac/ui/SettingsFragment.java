package moose.com.ac.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;

import moose.com.ac.R;
import moose.com.ac.ui.widget.RxPreferenceFragmentCompat;
import moose.com.ac.util.SettingPreferences;
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
 * Created by dell on 2015/9/1.
 * SettingsFragment
 * see {http://developer.android.com/guide/topics/ui/settings.html#Fragment}
 */
public class SettingsFragment extends RxPreferenceFragmentCompat {
    @VisibleForTesting
    protected SharedPreferences.OnSharedPreferenceChangeListener mListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingPreferences.sync(getPreferenceManager());
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListener = (sharedPreferences, key) -> SettingPreferences.sync(getPreferenceManager(), key);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(mListener);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mListener);
        super.onPause();
    }

}
