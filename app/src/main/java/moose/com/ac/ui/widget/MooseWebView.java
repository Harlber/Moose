package moose.com.ac.ui.widget;

/*       Copyright (C) 2015 The Android Open Source Project

         Licensed under the Apache License, Version 2.0 (the "License");
         you may not use this file except in compliance with the License.
         You may obtain a copy of the License at

              http://www.apache.org/licenses/LICENSE-2.0

         Unless required by applicable law or agreed to in writing, software
         distributed under the License is distributed on an "AS IS" BASIS,
         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
         See the License for the specific language governing permissions and
         limitations under the License.*/

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import java.io.IOException;

import moose.com.ac.BuildConfig;
import moose.com.ac.R;
import moose.com.ac.util.RxUtils;

/**
 * Created by Farble on 2015/8/16 10.
 *
 * @author Yrom
 */
public class MooseWebView extends AppCompatActivity {
    private static final String TAG = "MooseWebView";

    protected WebView mWeb;
    private View mProgress;

    @Override
    protected void onPause() {
        super.onPause();
        mWeb.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWeb.resumeTimers();
    }

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        mProgress = findViewById(R.id.loading);
        mWeb = (WebView) findViewById(R.id.webview);
        mWeb.getSettings().setAllowFileAccess(true);
        mWeb.getSettings().setJavaScriptEnabled(true);
        mWeb.getSettings().setUserAgentString(RxUtils.UA);
        mWeb.getSettings().setUseWideViewPort(true);
        mWeb.getSettings().setLoadWithOverviewMode(true);
        /*
         * fixed issues #12
         * http://stackoverflow.com/questions/9476151/webview-flashing-with-white-background-if-hardware-acceleration-is-enabled-an
         */
        if (Build.VERSION.SDK_INT >= 11)
            mWeb.setBackgroundColor(Color.argb(1, 0, 0, 0));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        }
        initView(savedInstanceState);
        initData();
    }

    protected DialogInterface.OnClickListener mErrorDialogListener = (dialog, which) -> {
        dialog.dismiss();
        if (which == DialogInterface.BUTTON_POSITIVE) {
            initData();
        } else {
            finish();
        }
    };

    protected void showErrorDialog() {
        try {
            Drawable icon = Drawable.createFromStream(getAssets().open("emotion/ais/27.gif"), "27.gif");
            icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
            new AlertDialog.Builder(this).setTitle("加载失败！").setIcon(icon).setMessage("是否重试？").setPositiveButton("重试", mErrorDialogListener)
                    .setNegativeButton("算了", mErrorDialogListener).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void initView(Bundle savedInstanceState) {
    }

    protected void initData() {
        setSupportProgressBarIndeterminateVisibility(true);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setSupportProgressBarIndeterminateVisibility(boolean visible) {
        mProgress.setVisibility(visible ? View.VISIBLE : View.GONE);
        mWeb.setVisibility(visible ? View.GONE : View.VISIBLE);
    }

    /**
     * @param script         the JavaScript to execute.
     * @param resultCallback A callback to be invoked when the script execution completes
     *                       with the result of the execution (if any). May be null if no
     *                       notificaion of the result is required.
     */
    public void evaluateJavascript(String script, ValueCallback<String> resultCallback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                mWeb.evaluateJavascript(script, resultCallback);
                return;
            } catch (Exception ignored) {
            }
        }
        mWeb.loadUrl(script);
    }
}
