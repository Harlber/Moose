package moose.com.ac.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import moose.com.ac.R;
import moose.com.ac.common.Config;
import moose.com.ac.retrofit.Api;
import moose.com.ac.retrofit.article.Article;
import moose.com.ac.retrofit.article.ArticleList;
import moose.com.ac.util.RxUtils;
import retrofit.RetrofitError;
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
    private CompositeSubscription subscription = new CompositeSubscription();
    private Api api;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(
                R.layout.fragment_article_list, container, false);
        mChannelId = getArguments().getInt(Config.CHANNEL_ID);
        type = Integer.valueOf(getArguments().getString(Config.CHANNEL_TYPE));
        initRecyclerView();
        initRefreshLayout();
        init();
        return rootView;
    }

    @Override
    protected void init() {
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
    }

    private void loadData(int tp, int pg, boolean isSave) {
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));//show progressbar
        isRequest = true;
        subscription.add(api.getArticleList(tp, mChannelId, pg, Config.PAGESIZE)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArticleList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        e.printStackTrace();
                        if (e instanceof RetrofitError) {
                            if (((RetrofitError) e).getResponse() != null) {
                                Snack(getString(R.string.net_work) + ((RetrofitError) e).getResponse().getStatus());
                            } else {
                                Snack(getString(R.string.no_network));
                            }

                        }

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

}
