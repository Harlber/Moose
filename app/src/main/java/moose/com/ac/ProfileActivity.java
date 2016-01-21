package moose.com.ac;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import moose.com.ac.common.Config;
import moose.com.ac.data.ArticleCollects;
import moose.com.ac.data.DbHelper;
import moose.com.ac.retrofit.Api;
import moose.com.ac.retrofit.Profile;
import moose.com.ac.retrofit.login.CheckIn;
import moose.com.ac.ui.widget.MultiSwipeRefreshLayout;
import moose.com.ac.util.CommonUtil;
import moose.com.ac.util.PreferenceUtil;
import moose.com.ac.util.RxUtils;
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
 * Created by dell on 2015/9/1.
 * ProfileActivity
 */
public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private final Api api = RxUtils.createCookieApi(Api.class, Config.BASE_URL);
    private CompositeSubscription subscription = new CompositeSubscription();
    private MultiSwipeRefreshLayout mSwipeRefreshLayout;
    private AppCompatButton registButton;
    private AppCompatTextView uid;
    private AppCompatTextView signature;
    private AppCompatTextView date;
    private AppCompatTextView gender;
    private View snakView;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.profile_chenkin:
                    mSwipeRefreshLayout.setRefreshing(true);
                    subscription.add(api.chenkin()
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<CheckIn>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    mSwipeRefreshLayout.setEnabled(false);
                                    e.printStackTrace();
                                    Snack(getString(R.string.no_network));
                                }

                                @Override
                                public void onNext(CheckIn checkIn) {
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    mSwipeRefreshLayout.setEnabled(false);
                                    if (checkIn.isSuccess()) {
                                        Snack(getString(R.string.reg_success));
                                    } else {
                                        Snack(checkIn.getInfo());
                                    }
                                }
                            }));
                    break;
                case R.id.profile_logout:
                    CommonUtil.setLoginStatus(Config.LOGIN_OUT);
                    DbHelper dbHelper = new DbHelper(ProfileActivity.this);
                    dbHelper.dropSql(ArticleCollects.ArticleCookies.TABLE_NAME);//clear cookies
                    dbHelper.createTab(ArticleCollects.SQL_CREATE_COOKIES);//cookie
                    PreferenceUtil.setStringValue(Config.USERNAME, getString(R.string.un_login));
                    PreferenceUtil.setStringValue(Config.USER_LOG, getString(R.string.default_user_logo));
                    Snack(getString(R.string.logout_success));
                    new Handler().postDelayed(ProfileActivity.this::finish, Config.TIME_LATE);
                    break;
                default:
                    break;
            }
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.user_info));
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        //noinspection ConstantConditions
        ab.setDisplayHomeAsUpEnabled(true);
        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) findViewById(R.id.profile_swipe);

        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.md_orange_700, R.color.md_red_500,
                R.color.md_indigo_900, R.color.md_green_700);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.profile_swipe_child);

        ImageView logo = (ImageView) findViewById(R.id.profile_logo);
        registButton = (AppCompatButton) findViewById(R.id.profile_chenkin);
        AppCompatButton logoutButton = (AppCompatButton) findViewById(R.id.profile_logout);
        registButton.setOnClickListener(mOnClickListener);
        logoutButton.setOnClickListener(mOnClickListener);
        uid = (AppCompatTextView) findViewById(R.id.profile_uid);
        signature = (AppCompatTextView) findViewById(R.id.profile_qian);
        date = (AppCompatTextView) findViewById(R.id.profile_date);
        gender = (AppCompatTextView) findViewById(R.id.profile_gender);
        snakView = findViewById(R.id.snak_view);
        if (CommonUtil.hasRegis()) {
            registButton.setText(R.string.checked_in);
            signature.setText(getString(R.string.user_sign) + CommonUtil.getSignatrue());
            date.setText(CommonUtil.toDate(CommonUtil.getRegDate()));
            gender.setText(getString(R.string.gender) + CommonUtil.getGender(CommonUtil.getGender()));
        } else {
            mSwipeRefreshLayout.setRefreshing(true);
            subscription.add(api.getUserProfile()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Profile>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            mSwipeRefreshLayout.setRefreshing(false);
                            mSwipeRefreshLayout.setEnabled(false);
                            e.printStackTrace();
                            Snack(getString(R.string.no_network));
                        }

                        @Override
                        public void onNext(Profile profile) {
                            mSwipeRefreshLayout.setRefreshing(false);
                            mSwipeRefreshLayout.setEnabled(false);
                            if (profile.isSuccess()) {
                                CommonUtil.setSignatrue(profile.getSign());
                                CommonUtil.setRegDate(profile.getRegTime());
                                CommonUtil.setGender(profile.getGender());
                                CommonUtil.setRegistDate();//签到
                                registButton.setText(getString(R.string.checked_in));

                                uid.setText(profile.getUsername());
                                signature.setText(getString(R.string.user_sign) + profile.getSign());
                                date.setText(CommonUtil.toDate(profile.getRegTime()));
                                gender.setText(getString(R.string.gender) + CommonUtil.getGender(profile.getGender()));

                            } else {
                                Snack(profile.getInfo());
                            }
                        }
                    }));

        }

        Glide.with(ProfileActivity.this)
                .load(CommonUtil.getUserLogo())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(logo);
        uid.setText(CommonUtil.getUserName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ProfileActivity.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        subscription = RxUtils.getNewCompositeSubIfUnsubscribed(subscription);
    }

    @Override
    public void onPause() {
        super.onPause();
        RxUtils.unsubscribeIfNotNull(subscription);
    }

    private void Snack(String msg) {
        Snackbar snackBar = Snackbar.make(snakView, msg, Snackbar.LENGTH_SHORT);
        snackBar.setAction(R.string.snackbar_action, v -> {
            snackBar.dismiss();
        });
        snackBar.getView().setBackgroundResource(R.color.colorPrimary);
        snackBar.show();
    }

}
