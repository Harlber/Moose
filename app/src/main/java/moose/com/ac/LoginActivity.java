package moose.com.ac;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.trello.rxlifecycle.ActivityEvent;

import moose.com.ac.common.Config;
import moose.com.ac.retrofit.Api;
import moose.com.ac.retrofit.login.LoginEntry;
import moose.com.ac.ui.widget.EmailEditText;
import moose.com.ac.ui.widget.MultiSwipeRefreshLayout;
import moose.com.ac.ui.BaseActivity;
import moose.com.ac.util.CommonUtil;
import moose.com.ac.util.RxUtils;
import moose.com.ac.util.SettingPreferences;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
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
 * Created by dell on 2015/8/31.
 * Date: Mon, 31 Aug 2015 09:04:43 GMT
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private Api api;
    private CompositeSubscription subscription = new CompositeSubscription();
    protected MultiSwipeRefreshLayout mSwipeRefreshLayout;
    private EmailEditText name;
    private AppCompatEditText pwd;
    private TextInputLayout nameLayout;
    private TextInputLayout passwordLayout;
    private AppCompatButton login;

    AccountManager accountManager;

    private boolean isRequest = false;
    private boolean addAccount = false;

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        accountManager = AccountManager.get(this);
        addAccount = getIntent().getBooleanExtra(Config.EXTRA_ADD_ACCOUNT, false);
        api = RxUtils.createLoginApi(Api.class, Config.BASE_URL);
        initView();
        setListener();
    }

    private void setListener() {
        login.setOnClickListener(view -> {
            if (check() && !isRequest) {
                isRequest = true;
                mSwipeRefreshLayout.setEnabled(true);
                mSwipeRefreshLayout.setRefreshing(true);
                subscription.add(api.login(name.getText().toString(), pwd.getText().toString())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(this.<LoginEntry>bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(new Observer<LoginEntry>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                mSwipeRefreshLayout.setRefreshing(false);
                                mSwipeRefreshLayout.setEnabled(false);
                                isRequest = false;//refresh request status
                                e.printStackTrace();
                                Snack(getString(R.string.no_network));
                            }

                            @Override
                            public void onNext(LoginEntry response) {
                                isRequest = false;//fix button of LoginActivity enable after login-request
                                mSwipeRefreshLayout.setRefreshing(false);
                                mSwipeRefreshLayout.setEnabled(false);
                                if (response.isSuccess()) {
                                    CommonUtil.setUserName(response.getUsername());
                                    CommonUtil.setUserLogo(response.getImg());
                                    CommonUtil.setLoginStatus(Config.LOGIN_IN);
                                    CommonUtil.setLoginEmail(name.getText().toString());
                                    Snack(getString(R.string.login_success));
                                    if (addAccount) {
                                        addAccount(name.getText().toString(), pwd.getText().toString());
                                    }
                                    new Handler().postDelayed(LoginActivity.this::finish, Config.TIME_LOGIN);
                                } else {
                                    Snack(response.getResult());
                                }
                            }
                        }));
            }
        });
    }

    private void addAccount(String username, String password) {
        Account account = new Account(username, BuildConfig.APPLICATION_ID);
        accountManager.addAccountExplicitly(account, password, null);
        accountManager.setPassword(account, password); // for re-login with updated password
        Bundle bundle = new Bundle();
        bundle.putString(AccountManager.KEY_ACCOUNT_NAME, username);
        bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, BuildConfig.APPLICATION_ID);
        SettingPreferences.setUsername(this, username);
        finish();
    }

    private void initView() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.login));
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        //noinspection ConstantConditions
        ab.setDisplayHomeAsUpEnabled(true);

        nameLayout = (TextInputLayout) findViewById(R.id.input_layout_name);
        passwordLayout = (TextInputLayout) findViewById(R.id.input_layout_pwd);
        nameLayout.setHintAnimationEnabled(true);
        passwordLayout.setHintAnimationEnabled(true);

        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) findViewById(R.id.login_swipe);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.md_white);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimary);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.login_swipe_child);
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setRefreshing(false);

        name = (EmailEditText) findViewById(R.id.login_name);
        pwd = (AppCompatEditText) findViewById(R.id.login_pwd);
        login = (AppCompatButton) findViewById(R.id.login_submit);

        if (!CommonUtil.getLoginEmail().equals("")) {
            name.setText(CommonUtil.getLoginEmail());
        }
        Account[] accounts = AccountManager.get(this)
                .getAccountsByType(BuildConfig.APPLICATION_ID);
        if (accounts.length > 0) {
            //todo select account
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                LoginActivity.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean check() {
        if (name.getText().toString().equals("")) {
            nameLayout.setError(getString(R.string.empty_username));
            return false;
        }
        if (pwd.getText().toString().equals("")) {
            passwordLayout.setError(getString(R.string.empty_pwd));
            return false;
        }
        return true;
    }

    private void Snack(String msg) {
        Snackbar snackBar = Snackbar.make(login, msg, Snackbar.LENGTH_SHORT);
        snackBar.setAction(R.string.snackbar_action, v -> {
            snackBar.dismiss();
        });
        snackBar.getView().setBackgroundResource(R.color.colorPrimary);
        snackBar.show();
    }
}
