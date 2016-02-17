package moose.com.ac.crash;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import moose.com.ac.MainActivity;
import moose.com.ac.R;
import moose.com.ac.common.Config;


public class CrashActivity extends AppCompatActivity {
    private static final String TAG = "CrashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        //noinspection ConstantConditions
        ab.setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackBar = Snackbar.make(view, getString(R.string.snackbar_report_to_github), Snackbar.LENGTH_SHORT);
                snackBar.setAction(R.string.snackbar_action, v -> {
                    snackBar.dismiss();
                });
                snackBar.getView().setBackgroundResource(R.color.colorPrimary);
                snackBar.show();
            }
        });
        AppCompatTextView compatTextView = (AppCompatTextView) findViewById(R.id.crash_message);
        String crashInfo = getIntent().getStringExtra(Config.CRASH);
        compatTextView.setText(crashInfo);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigateToHome();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        navigateToHome();
    }

    private void navigateToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
