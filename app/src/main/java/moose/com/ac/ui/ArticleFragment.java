package moose.com.ac.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import moose.com.ac.MainActivity;
import moose.com.ac.R;
import moose.com.ac.common.Config;
import moose.com.ac.retrofit.Api;
import moose.com.ac.retrofit.article.Article;
import moose.com.ac.retrofit.article.ArticleList;
import moose.com.ac.util.RxUtils;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Farble on 2015/8/15 17.
 * 网络请求超时
 */
@SuppressLint("ValidFragment")
public class ArticleFragment extends ArticleListFragment {
    private static final String TAG = "ArticleFragment";
    private static final int MIN_SCROLL_TO_HIDE = 10;
    private CompositeSubscription subscription = new CompositeSubscription();
    private MainActivity mainActivity;
    private Api api;
    private boolean isRequest = false;//request data status
    private boolean isHide = false;
    private int accummulatedDy = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,@Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(
                R.layout.fragment_article_list, container, false);
        mChannelId = getArguments().getInt(Config.CHANNEL_ID);
        //noinspection ConstantConditions
        type = Integer.valueOf(getArguments().getString(Config.CHANNEL_TYPE));
        initRecyclerView();
        initRefreshLayout();

        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));//show progressbar
        new Handler().postDelayed(this::init, Config.TIME_LATE);
        return rootView;
    }

    @Override
    protected void init() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                isScroll = newState == RecyclerView.SCROLL_STATE_SETTLING;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mSwipeRefreshLayout.setEnabled(mLayoutManager
                        .findFirstCompletelyVisibleItemPosition() == 0);//fix bug while scroll RecyclerView & SwipeRefreshLayout shows also
                if (!recyclerView.canScrollVertically(1) && !isRequest) {
                    loadMore();
                }
                if (dy > 0) {
                    accummulatedDy = accummulatedDy > 0 ? accummulatedDy + dy : dy;
                    if (accummulatedDy > MIN_SCROLL_TO_HIDE && !isHide) {
                        mainActivity.animateOut();
                        isHide = true;
                    }
                } else if (dy < 0) {
                    accummulatedDy = accummulatedDy < 0 ? accummulatedDy + dy : dy;
                    if (accummulatedDy < (0 - MIN_SCROLL_TO_HIDE) && isHide) {
                        mainActivity.animateIn();
                        isHide = false;
                    }
                }
            }
        });
        //refactor recyclerview scroll
        api = RxUtils.createApi(Api.class, Config.ARTICLE_URL);
        loadData(type, mPage, true);
    }

    @Override
    protected void loadMore() {
        loadData(type, mPage, true);
    }

    @Override
    protected void doSwapeRefresh() {
        loadData(type, 1, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        subscription = RxUtils.getNewCompositeSubIfUnsubscribed(subscription);
    }

    @Override
    public void onPause() {
        super.onPause();
        RxUtils.unsubscribeIfNotNull(subscription);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void loadData(int tp, int pg, boolean isSave) {
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setRefreshing(true);//show progressbar
        isRequest = true;
        subscription.add(api.getArticleList(tp, mChannelId, pg, Config.PAGESIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArticleList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        isRequest = false;//refresh request status
                        mainActivity.snack(e.getMessage());
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(ArticleList articleList) {
                        if (isSave) {
                            mPage++;//false : new request
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                        List<Article> articles = new ArrayList<Article>();
                        articles = articleList.getData().getPage().getList();
                        if (isSave) {//add data into local
                            lists.addAll(articles);
                        } else {
                            lists.clear();//clear local data
                            lists.addAll(articles);
                        }
                        articles.clear();
                        adapter.notifyDataSetChanged();
                        isRequest = false;//refresh request status
                    }
                }));
    }

    public void loadChannel(int change) {
        type = change;
        mPage = 1;
        loadData(type, mPage, false);
    }

    public void scrollToTop(){
        if (mRecyclerView!=null) {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

}
