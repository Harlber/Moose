package moose.com.ac;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.squareup.leakcanary.LeakCanary;

import moose.com.ac.data.DbHelper;
import moose.com.ac.util.CommonUtil;
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
    private static Context mContext;
    private static DbHelper dbHelper;
    private static boolean isVistor;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        new PreferenceUtil(mContext);
        dbHelper = new DbHelper(this);
        isVistor = CommonUtil.isVisistor();
        if (!isInUnitTests()) {
            LeakCanary.install(this);
        }
    }

    protected boolean isInUnitTests() {
        return false;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public static Context getmContext() {
        return mContext;
    }

    public static DbHelper getDbHelper() {
        return dbHelper;
    }

    public static boolean isVistor() {
        return isVistor;
    }

    public static void setIsVistor(boolean isVistor) {
        AppApplication.isVistor = isVistor;
    }

    public static boolean isWifi() {
        SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(AppApplication.getmContext());
        boolean isSupport = shp.getBoolean("pref_key_wifi_load", false);
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return isSupport;
        }
        return false;
    }
}
