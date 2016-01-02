package moose.com.ac;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

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
public class App extends Application {
    private static final String TAG = "App";
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
        /*if (!isApkDebugable(this)) {
            LeakCanary.install(this);
        }*/
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
        App.isVistor = isVistor;
    }

    public static boolean isWifi() {
        SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(App.getmContext());
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

    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
