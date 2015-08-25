package moose.com.ac;

import android.app.Application;
import android.content.Context;

import moose.com.ac.util.PreferenceUtil;

/**
 * Created by Farble on 2015/8/15 11.
 * application
 */
public class App extends Application {
    private static final String TAG = "App";
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        new PreferenceUtil(mContext);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public static Context getmContext() {
        return mContext;
    }
}
