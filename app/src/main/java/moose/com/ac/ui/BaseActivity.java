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

            Intent intent = new Intent(mContext, CrashActivity.class);
            intent.putExtra(Config.CRASH, errorReport);
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
