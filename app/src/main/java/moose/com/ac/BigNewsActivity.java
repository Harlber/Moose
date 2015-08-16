package moose.com.ac;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_news);

        Toolbar toolbar = (Toolbar) findViewById(R.id.news_toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.news_viewpager);
        adapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        if (viewPager != null) {
            adapter.addFragment(new CommentListFragment());
            adapter.addFragment(new SubmitCommentFragment());
            viewPager.setAdapter(adapter);
        }else {
            Log.e(TAG,"adapter is null");
        }
    }

}
