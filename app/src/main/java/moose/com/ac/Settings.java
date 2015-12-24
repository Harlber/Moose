package moose.com.ac;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import moose.com.ac.ui.SettingsFragment;

/**
 * Created by dell on 2015/9/1.
 * Setting
 */
public class Settings extends AppCompatActivity {

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.set_all);
        getFragmentManager().beginTransaction()
                .replace(R.id.set_content, new SettingsFragment())/*android.R.id.content*/
                .commit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Settings.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
