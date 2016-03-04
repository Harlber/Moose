package moose.com.ac.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.io.PrintWriter;
import java.io.StringWriter;

import moose.com.ac.common.Config;
import moose.com.ac.crash.CrashActivity;

/**
 * Copyright 2015 Farble Dast
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Created by Farble on 2016/3/3 22.
 */
public abstract class BaseActivity extends RxAppCompatActivity {

    protected Context mContext;
    protected final UncaughtHandler mUncaughtHandler = new UncaughtHandler();

    protected class UncaughtHandler implements Thread.UncaughtExceptionHandler {
        private final String LINE_SEPARATOR = "\n";

        public UncaughtHandler() {
        }

        @Override
        public void uncaughtException(Thread thread, Throwable exception) {
            StringWriter stackTrace = new StringWriter();
            exception.printStackTrace(new PrintWriter(stackTrace));
            StringBuilder errorReport = new StringBuilder();
            errorReport.append("******** CAUSE OF ERROR ********\n\n");
            errorReport.append(stackTrace.toString());

            errorReport.append("\n******** DEVICE INFORMATION ********\n");
            errorReport.append("Brand: ");
            errorReport.append(Build.BRAND);
            errorReport.append(LINE_SEPARATOR);
            errorReport.append("Device: ");
            errorReport.append(Build.DEVICE);
            errorReport.append(LINE_SEPARATOR);
            errorReport.append("Model: ");
            errorReport.append(Build.MODEL);
            errorReport.append(LINE_SEPARATOR);
            errorReport.append("Id: ");
            errorReport.append(Build.ID);
            errorReport.append(LINE_SEPARATOR);
            errorReport.append("Product: ");
            errorReport.append(Build.PRODUCT);
            errorReport.append(LINE_SEPARATOR);

            errorReport.append("\n******** FIRMWARE ********\n");
            errorReport.append("SDK: ");
            errorReport.append(Build.VERSION.SDK);
            errorReport.append(LINE_SEPARATOR);
            errorReport.append("Release: ");
            errorReport.append(Build.VERSION.RELEASE);
            errorReport.append(LINE_SEPARATOR);
            errorReport.append("Incremental: ");
            errorReport.append(Build.VERSION.INCREMENTAL);
            errorReport.append(LINE_SEPARATOR);

            Intent intent = new Intent(mContext, CrashActivity.class);
            intent.putExtra(Config.CRASH, errorReport.toString());
            mContext.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        Thread.setDefaultUncaughtExceptionHandler(mUncaughtHandler);
        onInitView(savedInstanceState);
    }

    protected abstract void onInitView(Bundle savedInstanceState);
}
