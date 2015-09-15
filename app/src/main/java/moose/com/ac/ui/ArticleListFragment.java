package moose.com.ac.ui;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FlipInTopXAnimator;
import moose.com.ac.R;
import moose.com.ac.retrofit.article.Article;
import moose.com.ac.ui.widget.MultiSwipeRefreshLayout;

/**
 * Created by Farble on 2015/8/13 23.
 */
public abstract class ArticleListFragment extends Fragment {
    private static final String TAG = "ArticleListFragment";
    private static final int ANIMATION_DURATION = 2000;
    protected View rootView;
    protected RecyclerView mRecyclerView;
    protected LinearLayoutManager mLayoutManager;
    protected MultiSwipeRefreshLayout mSwipeRefreshLayout;

    protected List<Article> lists = new ArrayList<>();
    protected ArticleListAdapter adapter;
    protected FlipInTopXAnimator animator;

    protected boolean isScroll = false;//is RecyclerView scrolling

    protected int mChannelId = 74;
    protected int type = 3;//default
    protected int mPage = 1;//default


    protected void initRefreshLayout() {
        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);

        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.md_orange_700, R.color.md_red_500,
                R.color.md_indigo_900, R.color.md_green_700);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.recycler_view);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
            doSwapeRefresh();
        });
    }


    protected void initRecyclerView() {
        adapter = new ArticleListAdapter(lists, getActivity());
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adapter);
        animator = new FlipInTopXAnimator();
        animator.setAddDuration(ANIMATION_DURATION);
        animator.setRemoveDuration(ANIMATION_DURATION);
        mRecyclerView.setItemAnimator(animator);

    }

    protected abstract void init();

    protected abstract void loadMore();

    protected abstract void doSwapeRefresh();

    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }
}
