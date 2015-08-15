package moose.com.ac;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;

import moose.com.ac.common.Config;
import moose.com.ac.retrofit.Api;
import moose.com.ac.retrofit.article.Article;
import moose.com.ac.retrofit.article.ArticleList;
import moose.com.ac.ui.ListFragment;
import moose.com.ac.util.RxUtils;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Farble on 2015/8/15 17.
 */
@SuppressLint("ValidFragment")
public class ArticleFragment extends ListFragment {
    private static final int channelId = 74;
    private CompositeSubscription subscription = new CompositeSubscription();
    private Api api;

    @Override
    protected void init() {
        api = RxUtils.createApi(Api.class, Config.ARTICLE_URL);
        subscription.add(api.getArticleList(0,channelId,1,Config.PAGESIZE)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArticleList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Snack(getString(R.string.network_exception));
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ArticleList articleList) {
                        List<Article> articles = new ArrayList<Article>();
                        articles = articleList.getData().getPage().getList();
                        lists.addAll(articles);
                        articles.clear();
                        adapter.notifyDataSetChanged();
                    }
                }));
    }

    @Override
    protected void loadMore() {

    }

    @Override
    protected void doSwapeRefresh() {

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

}
