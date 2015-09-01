package moose.com.ac.ui;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import moose.com.ac.R;

/**
 * Created by dell on 2015/9/1.
 * SettingsFragment
 * see {http://developer.android.com/guide/topics/ui/settings.html#Fragment}
 */
public class SettingsFragment extends PreferenceFragment {
    private static final String TAG = "SettingsFragment";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
