package moose.com.ac;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

import moose.com.ac.data.DbHelper;
import moose.com.ac.util.CommonUtil;
import moose.com.ac.util.PreferenceUtil;

/**
 * Created by Farble on 2015/8/15 11.
 * application
 */
public class App extends Application {
    private static final String TAG = "App";
    private static Context mContext;
    private static DbHelper dbHelper;
    private static boolean isVistor;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        new PreferenceUtil(mContext);
        dbHelper = new DbHelper(this);
        isVistor = CommonUtil.isVisistor();
        LeakCanary.install(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public static Context getmContext() {
        return mContext;
    }

    public static DbHelper getDbHelper() {
        return dbHelper;
    }

    public static boolean isVistor() {
        return isVistor;
    }

    public static void setIsVistor(boolean isVistor) {
        App.isVistor = isVistor;
    }
}
