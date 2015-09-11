package moose.com.ac;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import moose.com.ac.common.Config;
import moose.com.ac.retrofit.Api;
import moose.com.ac.retrofit.login.LoginEntry;
import moose.com.ac.ui.view.EmailEditText;
import moose.com.ac.ui.view.MultiSwipeRefreshLayout;
import moose.com.ac.util.CommonUtil;
import moose.com.ac.util.RxUtils;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dell on 2015/8/31.
 * Date: Mon, 31 Aug 2015 09:04:43 GMT
 */
public class Login extends AppCompatActivity {
    private static final String TAG = "Login";
    private Api api;
    private CompositeSubscription subscription = new CompositeSubscription();
    protected MultiSwipeRefreshLayout mSwipeRefreshLayout;
    private EmailEditText name;
    private AppCompatEditText pwd;
    private AppCompatButton login;

    private boolean isRequest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme(R.style.DayTheme);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(getResources().getColor(R.color.md_black));
        } else {
            // Implement this feature without material design
        }
        setContentView(R.layout.activity_login);
        api = RxUtils.createLoginApi(Api.class, Config.BASE_URL);
        initView();
    }

    private void initView() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        toolbar.setTitle(getString(R.string.login));
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        //noinspection ConstantConditions
        ab.setDisplayHomeAsUpEnabled(true);

        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) findViewById(R.id.login_swipe);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.md_orange_700, R.color.md_red_500,
                R.color.md_indigo_900, R.color.md_green_700);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.login_swipe_child);
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setRefreshing(false);

        name = (EmailEditText) findViewById(R.id.login_name);
        pwd = (AppCompatEditText) findViewById(R.id.login_pwd);
        login = (AppCompatButton) findViewById(R.id.login_submit);

        if (!CommonUtil.getLoginEmail().equals(""))
            name.setText(CommonUtil.getLoginEmail());
        login.setOnClickListener(view -> {
            if (check()&&!isRequest){
                isRequest = true;
                mSwipeRefreshLayout.setEnabled(true);
                mSwipeRefreshLayout.setRefreshing(true);
                subscription.add(api.login(name.getText().toString(), pwd.getText().toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
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
                               /* if (e instanceof RetrofitError) {
                                    if (((RetrofitError) e).getResponse() != null) {
                                        Snack(getString(R.string.net_work) + ((RetrofitError) e).getResponse().getStatus());
                                    } else {
                                        Snack(getString(R.string.no_network));
                                    }

                                }*/
                            }

                            @Override
                            public void onNext(LoginEntry response) {
                                isRequest = true;
                                mSwipeRefreshLayout.setRefreshing(false);
                                mSwipeRefreshLayout.setEnabled(false);
                                if (response.isSuccess()) {
                                    CommonUtil.setUserName(response.getUsername());
                                    CommonUtil.setUserLogo(response.getImg());
                                    CommonUtil.setLoginStatus(Config.LOGIN_IN);
                                    CommonUtil.setLoginEmail(name.getText().toString());
                                    Snack(getString(R.string.login_success));
                                    new Handler().postDelayed(Login.this::finish, Config.TIME_LOGIN);
                                }else {
                                    Snack(response.getResult());
                                }
                            }
                        }));
        }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Login.this.finish();
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


    private boolean check() {
        if (name.getText().toString().equals("")) {
            name.setError(getString(R.string.empty_username));
            return false;
        }
        if (pwd.getText().toString().equals("")) {
            pwd.setError(getString(R.string.empty_pwd));
            return false;
        }
        return true;
    }

    private void Snack(String msg) {
        Snackbar.make(login, msg, Snackbar.LENGTH_SHORT).show();
    }
}
