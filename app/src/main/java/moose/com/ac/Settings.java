package moose.com.ac;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import moose.com.ac.ui.SettingsFragment;

/**
 * Created by dell on 2015/9/1.
 */
public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
