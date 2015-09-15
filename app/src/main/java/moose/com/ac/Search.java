package moose.com.ac;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import moose.com.ac.common.Config;
import moose.com.ac.ui.SearchFragment;

/**
 * Created by dell on 2015/9/15.
 * Search - View
 */
public class Search extends AppCompatActivity {
    private static final String TAG = "Search";
    private SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        //noinspection ConstantConditions
        ab.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        ab.setDisplayHomeAsUpEnabled(true);
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(R.string.search);
        initData();
    }

    private void initData() {
        String key = getIntent().getStringExtra(Config.SEARCH_KEY);
        Bundle bundle = new Bundle();
        bundle.putString(Config.SEARCH_KEY, key);
        searchFragment = new SearchFragment();
        searchFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.search_framelayout, searchFragment)
                .commit();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Search.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
