package moose.com.ac;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.io.PrintWriter;
import java.io.StringWriter;

import moose.com.ac.common.Config;
import moose.com.ac.crash.CrashActivity;
import moose.com.ac.data.DbHelper;
import moose.com.ac.util.PreferenceUtil;
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
 * Created by Farble on 2015/8/15 11.
 * application
 */
public class AppApplication extends Application {
    private static Context context;
    private static DbHelper dbHelper;

    private static RefWatcher refWatcher;
    //private final UncaughtHandler mUncaughtHandler = new UncaughtHandler();

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        //Thread.setDefaultUncaughtExceptionHandler(mUncaughtHandler);
        new PreferenceUtil(this);
        dbHelper = new DbHelper(this);
        if (!isInUnitTests()) {
            refWatcher = LeakCanary.install(this);
        }
        Stetho.initializeWithDefaults(this);
    }

    public static RefWatcher getRefWatcher() {
        return refWatcher;
    }

    protected boolean isInUnitTests() {
        return true;
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

    protected class UncaughtHandler implements Thread.UncaughtExceptionHandler {
        private final String LINE_SEPARATOR = "\n";

        public UncaughtHandler() {
        }

        @Override
        public void uncaughtException(Thread thread, Throwable exception) {
            StringWriter stackTrace = new StringWriter();
            exception.printStackTrace(new PrintWriter(stackTrace));
            String errorReport = "******** CAUSE OF ERROR ********\n\n" +
                    stackTrace.toString() +
                    "\n******** DEVICE INFORMATION ********\n" +
                    "Brand: " +
                    Build.BRAND +
                    LINE_SEPARATOR +
                    "Device: " +
                    Build.DEVICE +
                    LINE_SEPARATOR +
                    "Model: " +
                    Build.MODEL +
                    LINE_SEPARATOR +
                    "Id: " +
                    Build.ID +
                    LINE_SEPARATOR +
                    "Product: " +
                    Build.PRODUCT +
                    LINE_SEPARATOR +
                    "\n******** FIRMWARE ********\n" +
                    "SDK: " +
                    Build.VERSION.SDK +
                    LINE_SEPARATOR +
                    "Release: " +
                    Build.VERSION.RELEASE +
                    LINE_SEPARATOR +
                    "Incremental: " +
                    Build.VERSION.INCREMENTAL +
                    LINE_SEPARATOR;

            Intent intent = new Intent(context, CrashActivity.class);
            intent.putExtra(Config.CRASH, errorReport);
            startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
        }
    }

}
