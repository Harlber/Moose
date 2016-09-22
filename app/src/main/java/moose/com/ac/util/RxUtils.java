package moose.com.ac.util;

import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import moose.com.ac.AppApplication;
import moose.com.ac.data.DbHelper;
import moose.com.ac.data.LocalCookie;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Farble on 2015/8/15 12.
 * see http://mattlogan.me/notes-on-updating-to-retrofit-2.html
 * see https://github.com/square/retrofit/blob/master/CHANGELOG.md
 */
public final class RxUtils {
    private static final String TAG = "RxUtils";
    /*public static String UA = "acfun/1.0 (Linux; U; Android " + Build.VERSION.RELEASE + "; " +
            Build.MODEL + "; " + Locale.getDefault().getLanguage() + "-" +
            Locale.getDefault().getCountry().toLowerCase() +
            ") AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 ";*/
    public static String UA = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.109 Safari/537.36";

    private RxUtils() {
        throw new AssertionError("No instances");
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
        OkHttpClient client = OkHttpClientProvider.get(); //create OKHTTPClient
        client.interceptors().add(ACFUN_TOKEN_INTERCEPTOR);
        client.interceptors().add(REWRITE_CACHE_CONTROL_INTERCEPTOR);
        client.interceptors().add(new LoggingInterceptor());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(c);

    }

    public static <T> T createShadowApi(Class<T> c) {
        OkHttpClient client = OkHttpClientProvider.get();
        client.interceptors().add(SHADOW_PORTAL_INTERCEPTOR);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.aixifan.com")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(c);

    }

    public static <T> T createLoginApi(Class<T> c, String url) {
        OkHttpClient client = OkHttpClientProvider.get(); //create OKHTTPClient
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client.setCookieHandler(cookieManager); //finally set the cookie handler on client
        client.interceptors().add(new ReceivedCookiesInterceptor());
        client.interceptors().add(new LoggingInterceptor());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(c);
    }

    public static <T> T createCookieApi(Class<T> c, String url) {
        OkHttpClient client = OkHttpClientProvider.get(); //create OKHTTPClient
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client.setCookieHandler(cookieManager); //finally set the cookie handler on client
        client.interceptors().add(new AddCookiesInterceptor());
        client.interceptors().add(new LoggingInterceptor());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(c);
    }

    public static <T> T createCookieTextApi(Class<T> c, String url) {
        OkHttpClient client = OkHttpClientProvider.get(); //create OKHTTPClient
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client.setCookieHandler(cookieManager); //finally set the cookie handler on client
        client.interceptors().add(new AddCookiesInterceptor());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(c);
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
                DbHelper dbHelper = AppApplication.getDbHelper();
                dbHelper.clearCookies();
                HashSet<String> cookies = new HashSet<>();

                for (String header : originalResponse.headers("Set-Cookie")) {
                    cookies.add(header);
                    dbHelper.insertCookies(header);
                    Log.i(TAG, "get cookies:" + header);

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
            DbHelper dbHelper = new DbHelper(AppApplication.getContext());
            List<LocalCookie> cookies = new ArrayList<>();
            cookies = dbHelper.getDbCookies();
            builder.addHeader("User-Agent", UA);
            builder.addHeader("Accept", "application/json; q=0.5");
            for (LocalCookie cookie : cookies) {
                builder.addHeader("Cookie", cookie.getCookie());
                Log.i(TAG, "set cookies:" + cookie.getCookie());
            }
            Log.v("OkHttp", "Adding Header: "); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp

            return chain.proceed(builder.build());
        }
    }

    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = chain -> {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .header("User-Agent", UA)
                .header("Accept", "application/json; q=0.5")
                .build();
    };

    private static final Headers.Builder builder = new Headers.Builder()
            .add("market", "xiaomi")
            .add("appVersion", "4.3.0")
            .add("deviceType", "1");

    private static final Interceptor ACFUN_TOKEN_INTERCEPTOR = chain -> {
        Request request = chain.request();
        Request newRequest = request.newBuilder().headers(builder.build()).build();
        return chain.proceed(newRequest);
    };


    private static final Interceptor SHADOW_PORTAL_INTERCEPTOR = chain -> {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .header("appVersion", "4.1.2")
                .header("user-agent", "acvideo core")
                .header("market", "portal")
                .header("productId", "2000")
                .header("deviceType", "1")
                .header("uid", "0")
                .header("resolution", "720x1184")
                .header("udid", "66987f52-1573-381f-b199-99f14f45e8bc")
                .header("If-None-Match", "e80e4941-9f9b-4c47-aac3-05b02890d7f0")
                .header("Host", "api.aixifan.com")
                .header("Connection", "Keep-Alive")
                .header("Accept-Encoding", "gzip")
                .build();
    };

    /**
     * see http://stackoverflow.com/questions/24952199/okhttp-enable-logs
     */
    static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Log.v("OkHttp", String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Log.v("OkHttp", String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }
}
