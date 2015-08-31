package moose.com.ac;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import moose.com.ac.common.Config;
import moose.com.ac.retrofit.Api;
import moose.com.ac.retrofit.login.CheckIn;
import moose.com.ac.retrofit.login.LoginEntry;
import moose.com.ac.util.RxUtils;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dell on 2015/8/31.
 * Date: Mon, 31 Aug 2015 09:04:43 GMT
 * D/Retrofit﹕ Server: Tengine/2.0.3
 * D/Retrofit﹕ Content-Type: application/json;charset=UTF-8
 * D/Retrofit﹕ Transfer-Encoding: chunked
 * D/Retrofit﹕ Set-Cookie: clientlanguage=zh_CN; Path=/
 * D/Retrofit﹕ Set-Cookie: JSESSIONID=98a1558e0b3442219c85eb7df7ae507f; Domain=acfun.tv; Expires=Wed, 30-Sep-2015 09:07:51 GMT; Path=/; HttpOnly
 * D/Retrofit﹕ Set-Cookie: _error_remaining=""; Domain=acfun.tv; Expires=Thu, 01-Jan-1970 00:00:10 GMT; Path=/
 * D/Retrofit﹕ Set-Cookie: _error_remaining=""; Expires=Thu, 01-Jan-1970 00:00:10 GMT; Path=/
 * D/Retrofit﹕ Set-Cookie: auth_key=880780; Domain=acfun.tv; Expires=Wed, 30-Sep-2015 09:07:51 GMT; Path=/
 * D/Retrofit﹕ Set-Cookie: auth_key_ac_sha1=951032386; Domain=acfun.tv; Expires=Wed, 30-Sep-2015 09:07:51 GMT; Path=/
 * D/Retrofit﹕ Set-Cookie: auth_key_ac_sha1_="ztIcYzNqIEj83ubn5KNLzO5cK10="; Version=1; Domain=acfun.tv; Max-Age=2592000; Expires=Wed, 30-Sep-2015 09:07:51 GMT; Path=/
 * D/Retrofit﹕ Set-Cookie: ac_username=%E6%88%91%E6%9C%89%E8%8F%87%E5%87%89%E6%9E%9C%E7%85%A7; Domain=acfun.tv; Expires=Wed, 30-Sep-2015 09:07:51 GMT; Path=/
 * D/Retrofit﹕ Set-Cookie: ac_time=""; Domain=acfun.tv; Expires=Thu, 01-Jan-1970 00:00:10 GMT; Path=/
 * D/Retrofit﹕ Set-Cookie: ac_time=""; Expires=Thu, 01-Jan-1970 00:00:10 GMT; Path=/
 * D/Retrofit﹕ Set-Cookie: ac_userimg=http%3A%2F%2Fcdn.aixifan.com%2Fdotnet%2Fartemis%2Fu%2Fcms%2Fwww%2F201505%2F1413200735rj.jpg; Domain=acfun.tv; Expires=Wed, 30-Sep-2015 09:07:51 GMT; Path=/
 * D/Retrofit﹕ Pragma: No-cache
 * D/Retrofit﹕ Cache-Control: no-cache
 * D/Retrofit﹕ Expires: Thu, 01 Jan 1970 00:00:00 GMT
 * D/Retrofit﹕ X-Via: 1.1 lsj114:1 (Cdn Cache Server V2.0)
 * D/Retrofit﹕ Connection: keep-alive
 * D/Retrofit﹕ OkHttp-Selected-Protocol: http/1.1
 * D/Retrofit﹕ OkHttp-Sent-Millis: 1441011882473
 * D/Retrofit﹕ OkHttp-Received-Millis: 1441011883127
 * D/Retrofit﹕ {"img":"http://cdn.aixifan.com/dotnet/artemis/u/cms/www/201505/1413200735rj.jpg","success":true,"username":"我有菇凉果照"}
 */
public class Login extends AppCompatActivity {
    private static final String TAG = "Login";
    private Api api;
    private Api apiqiandao;
    private CompositeSubscription subscription = new CompositeSubscription();
    private CompositeSubscription subscriptionll = new CompositeSubscription();
    private EditText name;
    private EditText pwd;
    private Button login;
    private Button checkin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        api = RxUtils.createLoginApi(Api.class, Config.BASE_URL);
        apiqiandao = RxUtils.createCookieApi(Api.class, Config.BASE_URL);
        initView();
    }

    private void initView() {
        name = (EditText) findViewById(R.id.login_name);
        pwd = (EditText) findViewById(R.id.login_pwd);
        login = (Button) findViewById(R.id.login_submit);
        checkin = (Button) findViewById(R.id.qiandao);
        checkin.setOnClickListener(view -> {
            subscriptionll.add(apiqiandao.chenkin()
                    .subscribe(new Observer<CheckIn>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(CheckIn response) {
                            Log.e(TAG, "  apiqiandao.chenkin():" + response.getResult());
                        }
                    }));
        });
        login.setOnClickListener(view -> {
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
                        }

                        @Override
                        public void onNext(LoginEntry response) {
                            Log.i(TAG, "onnext:" + response.getResult());

                        }
                    }));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        subscription = RxUtils.getNewCompositeSubIfUnsubscribed(subscription);
        subscriptionll = RxUtils.getNewCompositeSubIfUnsubscribed(subscriptionll);
    }

    @Override
    public void onPause() {
        super.onPause();
        RxUtils.unsubscribeIfNotNull(subscription);
        RxUtils.unsubscribeIfNotNull(subscriptionll);
    }
}
