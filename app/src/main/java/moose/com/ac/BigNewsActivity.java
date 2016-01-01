package moose.com.ac;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import moose.com.ac.common.Config;
import moose.com.ac.ui.CommentListFragment;
import moose.com.ac.ui.SubmitCommentFragment;
import moose.com.ac.ui.ViewPageAdapter;
import moose.com.ac.util.ZoomOutPageTransformer;


/**
 * Created by Farble on 2015/8/16 13.
 * 整天搞个大新闻
 */
public class BigNewsActivity extends AppCompatActivity {
    private static final String TAG = "BigNewsActivity";
    private ViewPager viewPager;
    private ViewPageAdapter adapter;
    private int contendid;
    private String title;
    private CommentListFragment commentListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_news);
        contendid = getIntent().getIntExtra(Config.CONTENTID, 1);
        title = getIntent().getStringExtra(Config.TITLE);
        commentListFragment = new CommentListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Config.CHANNEL_ID,contendid);
        commentListFragment.setArguments(bundle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("ac" + contendid);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.news_viewpager);
        adapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        if (viewPager != null) {
            adapter.addFragment(commentListFragment);
            adapter.addFragment(new SubmitCommentFragment());
            viewPager.setAdapter(adapter);
        }else {
            Log.e(TAG,"adapter is null");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.big_news, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                BigNewsActivity.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
