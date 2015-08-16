package moose.com.ac;

import android.app.Application;
import android.content.Context;

import java.io.File;

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
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
