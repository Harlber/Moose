package ac.moose.com.robolectric;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.res.Fs;

/**
 * Created by dell on 2015/12/10.
 * see http://stackoverflow.com/questions/26512839/android-lolipop-appcompat-problems-running-with-robolectric
 */
public class MooseRobolectricTestRunner extends RobolectricTestRunner{
    private static final int MAX_SDK_SUPPORTED_BY_ROBOLECTRIC = 21;
    /**
     * Creates a runner to run {@code testClass}. Looks in your working directory for your AndroidManifest.xml file
     * and res directory by default. Use the {@link Config} annotation to configure.
     *
     * @param testClass the test class to be run
     * @throws InitializationError if junit says so
     */
    public MooseRobolectricTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected AndroidManifest getAppManifest(Config config) {
        String manifestProperty = "../app/src/main/AndroidManifest.xml";
        String resProperty = "../app/src/main/res";
        String assectsProperty = "../app/src/main/assets";
        String packageName = "moose.com.ac";
        return new AndroidManifest(Fs.fileFromPath(manifestProperty),Fs.fileFromPath(resProperty),Fs.fileFromPath(assectsProperty),packageName) {
            @Override
            public int getTargetSdkVersion() {
                return MAX_SDK_SUPPORTED_BY_ROBOLECTRIC;
            }
        };
    }
}
