package moose.com.ac.util;

import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashSet;
import java.util.Locale;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Farble on 2015/8/15 12.
 */
public class RxUtils {
    private static final String TAG = "RxUtils";
    public static final String UA = "acfun/1.0 (Linux; U; Android "+ Build.VERSION.RELEASE+"; "+
            Build.MODEL+"; "+ Locale.getDefault().getLanguage()+"-"+
            Locale.getDefault().getCountry().toLowerCase()+
            ") AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 ";
    private RxUtils(){

    }
    public static void unsubscribeIfNotNull(Subscription subscription) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public static CompositeSubscription getNewCompositeSubIfUnsubscribed(CompositeSubscription subscription) {
        if (subscription == null || subscription.isUnsubscribed()) {
            return new CompositeSubscription();
        }

        return subscription;
    }


    public static <T> T createApi(Class<T> c, String url) {
        RequestInterceptor requestInterceptor = request -> {
            request.addHeader("User-Agent", UA);
            request.addHeader("Accept", "application/json; q=0.5");
        };
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(url)//.setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(requestInterceptor)
                .setClient(new OkClient(OkHttpClientProvider.get()))
                .setConverter(new GsonConverter(new Gson()))//.setErrorHandler(new FarbleError())
                .build();
        return restAdapter.create(c);
    }
    public static <T> T createLoginApi(Class<T> c, String url) {
        OkHttpClient client = OkHttpClientProvider.get(); //create OKHTTPClient
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client.setCookieHandler(cookieManager); //finally set the cookie handler on client
        client.interceptors().add(new ReceivedCookiesInterceptor());

                OkClient serviceClient = new OkClient(client);
        RequestInterceptor requestInterceptor = request -> {
            request.addHeader("User-Agent", UA);
            request.addHeader("Accept", "application/json; q=0.5");
        };
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)//
                .setRequestInterceptor(requestInterceptor)
                .setClient(serviceClient)
                .setConverter(new GsonConverter(new Gson()))//.setErrorHandler(new FarbleError())
                .build();
        return restAdapter.create(c);
    }

    public static <T> T createCookieApi(Class<T> c, String url) {
        OkHttpClient client = OkHttpClientProvider.get(); //create OKHTTPClient
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client.setCookieHandler(cookieManager); //finally set the cookie handler on client
        client.interceptors().add(new AddCookiesInterceptor());

        OkClient serviceClient = new OkClient(client);
        RequestInterceptor requestInterceptor = request -> {
            request.addHeader("User-Agent", UA);
            request.addHeader("Accept", "application/json; q=0.5");
        };
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)//
                .setRequestInterceptor(requestInterceptor)
                .setClient(serviceClient)
                .setConverter(new GsonConverter(new Gson()))//.setErrorHandler(new FarbleError())
                .build();
        return restAdapter.create(c);
    }
    public static <T> T createCookieTextApi(Class<T> c, String url) {
        OkHttpClient client = OkHttpClientProvider.get(); //create OKHTTPClient
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client.setCookieHandler(cookieManager); //finally set the cookie handler on client
        client.interceptors().add(new AddCookiesInterceptor());

        OkClient serviceClient = new OkClient(client);
        RequestInterceptor requestInterceptor = request -> {
            request.addHeader("User-Agent", UA);
            request.addHeader("Accept", "text/html;charset=UTF-8");
        };
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)//
                .setRequestInterceptor(requestInterceptor)
                .setClient(serviceClient)
                .setConverter(new GsonConverter(new Gson()))//.setErrorHandler(new FarbleError())
                .build();
        return restAdapter.create(c);
    }
    /**
     * This Interceptor add all received Cookies to the app DefaultPreferences.
     * Your implementation on how to save the Cookies on the Preferences MAY VARY.
     * <p>
     * Created by tsuharesu on 4/1/15.
     */
    public static class ReceivedCookiesInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());

            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                HashSet<String> cookies = new HashSet<>();

                for (String header : originalResponse.headers("Set-Cookie")) {
                    cookies.add(header);
                    Log.i(TAG, "cookies:" + header);
                }

            }

            return originalResponse;
        }
    }

    /**
     * This interceptor put all the Cookies in Preferences in the Request.
     * Your implementation on how to get the Preferences MAY VARY.
     * <p>
     * Created by tsuharesu on 4/1/15.
     */
    public static class AddCookiesInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            //HashSet<String> preferences = (HashSet) Preferences.getDefaultPreferences().getStringSet(Preferences.PREF_COOKIES, new HashSet<>());
                builder.addHeader("Cookie", "clientlanguage=zh_CN; Path=/");
                builder.addHeader("Cookie", "JSESSIONID=3d849fe0fb0a418e9b97e2e5f8a7f25d; Domain=acfun.tv; Expires=Wed, 30-Sep-2015 09:37:13 GMT; Path=/; HttpOnly");
                builder.addHeader("Cookie", "_error_remaining=\"\"; Domain=acfun.tv; Expires=Thu, 01-Jan-1970 00:00:10 GMT; Path=/");
                builder.addHeader("Cookie", "_error_remaining=\"\"; Expires=Thu, 01-Jan-1970 00:00:10 GMT; Path=/");
                builder.addHeader("Cookie", "auth_key=880780; Domain=acfun.tv; Expires=Wed, 30-Sep-2015 09:37:13 GMT; Path=/");
                builder.addHeader("Cookie", "auth_key_ac_sha1=951032386; Domain=acfun.tv; Expires=Wed, 30-Sep-2015 09:37:13 GMT; Path=/");
                builder.addHeader("Cookie", "auth_key_ac_sha1_=\"ztIcYzNqIEj83ubn5KNLzO5cK10=\"; Version=1; Domain=acfun.tv; Max-Age=2592000; Expires=Wed, 30-Sep-2015 09:37:13 GMT; Path=/");
                builder.addHeader("Cookie", "ac_username=%E6%88%91%E6%9C%89%E8%8F%87%E5%87%89%E6%9E%9C%E7%85%A7; Domain=acfun.tv; Expires=Wed, 30-Sep-2015 09:37:13 GMT; Path=/");
                builder.addHeader("Cookie", "ac_time=\"\"; Domain=acfun.tv; Expires=Thu, 01-Jan-1970 00:00:10 GMT; Path=/");
                builder.addHeader("Cookie", "ac_time=\"\"; Expires=Thu, 01-Jan-1970 00:00:10 GMT; Path=/");
                builder.addHeader("Cookie", "ac_userimg=http%3A%2F%2Fcdn.aixifan.com%2Fdotnet%2Fartemis%2Fu%2Fcms%2Fwww%2F201505%2F1413200735rj.jpg; Domain=acfun.tv; Expires=Wed, 30-Sep-2015 09:37:13 GMT; Path=/");
                Log.v("OkHttp", "Adding Header: " ); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp

            return chain.proceed(builder.build());
        }
    }
}
