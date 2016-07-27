package moose.com.ac.util;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import moose.com.ac.common.Config;

/**
 * Created by Farble on 2015/8/15 12.
 */
public enum OkHttpClientProvider {
    NETPPROVIDER, OkHttpClientProvider;

    private final OkHttpClient okHttpClient;

    /**
     * Used for HTTP POST requests in order to avoid retrying requests.
     */
    OkHttpClientProvider() {
        okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(Config.OKHTTP_CLIENT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(Config.OKHTTP_CLIENT_WRITE_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(Config.OKHTTP_CLIENT_READ_TIMEOUT, TimeUnit.SECONDS);

        okHttpClient.networkInterceptors().add(new StethoInterceptor());
    }

    public static OkHttpClient get() {
        return NETPPROVIDER.okHttpClient;
    }

}
