package ac.moose.com.robolectric;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;

/**
 * Created by dell on 2015/12/10.
 * see http://stackoverflow.com/questions/26512839/android-lolipop-appcompat-problems-running-with-robolectric
 */
public class MooseCustomRobolectricGradleTestRunner extends RobolectricGradleTestRunner {

    public MooseCustomRobolectricGradleTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected AndroidManifest getAppManifest(Config config) {
        AndroidManifest androidManifest = super.getAppManifest(config);
        androidManifest.setPackageName("moose.com.ac");
        return androidManifest;
    }
}
