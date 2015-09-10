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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import moose.com.ac.App;
import moose.com.ac.data.DbHelper;
import moose.com.ac.data.LocalCookie;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Farble on 2015/8/15 12.
 */
public class RxUtils {
    private static final String TAG = "RxUtils";
    public static  String UA = "acfun/1.0 (Linux; U; Android " + Build.VERSION.RELEASE + "; " +
            Build.MODEL + "; " + Locale.getDefault().getLanguage() + "-" +
            Locale.getDefault().getCountry().toLowerCase() +
            ") AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 ";

    private RxUtils() {

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
       /* RequestInterceptor requestInterceptor = request -> {
            request.addHeader("User-Agent", UA);
            request.addHeader("Accept", "application/json; q=0.5");
        };
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(url)//.setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(requestInterceptor)
                .setClient(new OkClient(OkHttpClientProvider.get()))
                .setConverter(new GsonConverter(new Gson()))//.setErrorHandler(new FarbleError())
                .build();
        return restAdapter.create(c);*/
        OkHttpClient client = OkHttpClientProvider.get(); //create OKHTTPClient
        Retrofit retrofit  = new Retrofit.Builder()
                .baseUrl(url)
                .client(client )
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
        return retrofit.create(c);

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
                DbHelper dbHelper = App.getDbHelper();
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
            DbHelper dbHelper = new DbHelper(App.getmContext());
            List<LocalCookie> cookies = new ArrayList<>();
            cookies = dbHelper.getDbCookies();
            for (LocalCookie cookie : cookies) {
                builder.addHeader("Cookie", cookie.getCookie());
                Log.i(TAG, "set cookies:" + cookie.getCookie());
            }
            Log.v("OkHttp", "Adding Header: "); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp

            return chain.proceed(builder.build());
        }
    }
}
