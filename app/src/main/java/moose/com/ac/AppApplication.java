package moose.com.ac;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import moose.com.ac.data.DbHelper;
import moose.com.ac.util.PreferenceUtil;
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
 * Created by Farble on 2015/8/15 11.
 * application
 */
public class AppApplication extends Application {
    private static final String TAG = "AppApplication";
    private static Context context;
    private static DbHelper dbHelper;

    private static RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        new PreferenceUtil(this);
        dbHelper = new DbHelper(this);
        if (!isInUnitTests()) {
            refWatcher = LeakCanary.install(this);
        }
    }

    public static RefWatcher getRefWatcher() {
        return refWatcher;
    }

    protected boolean isInUnitTests() {
        return false;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public static Context getContext() {
        return context;
    }

    public static DbHelper getDbHelper() {
        return dbHelper == null ? new DbHelper(context) : dbHelper;
    }

}
