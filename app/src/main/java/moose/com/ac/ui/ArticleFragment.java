package moose.com.ac.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.FragmentEvent;

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
/*
 * Copyright 2015,2016 Farble Dast
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Created by Farble on 2015/8/15 17.
 * 网络请求超时
 */
@SuppressLint("ValidFragment")
public class ArticleFragment extends BaseListFragment {
    private CompositeSubscription subscription = new CompositeSubscription();
    private MainActivity mainActivity;
    private List<Article> lists = new ArrayList<>();
    private Api api = RxUtils.createApi(Api.class, Config.ARTICLE_URL);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.abs_list_fragment, container, false);
    }

    @Override
    protected void loadMore() {
        loadData(type, mPage, true);
    }

    @Override
    protected void doSwipeRefresh() {
        lists.clear();
        adapter.notifyDataSetChanged();
        mPage = 1;
        loadData(type, mPage, false);
    }

    @Override
    protected void initRecyclerViewAdapter() {
        adapter = new ArticleListAdapter(lists, getActivity());
    }


    @Override
    public void onPause() {
        super.onPause();
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
    public void onReceiveData() {
        mChannelId = getArguments().getInt(Config.CHANNEL_ID);
        //noinspection ConstantConditions
        type = Integer.valueOf(getArguments().getString(Config.CHANNEL_TYPE));
    }

    @Override
    public void onInitData() {
        mSwipeRefreshLayout.postDelayed(() -> loadData(type, mPage, true), Config.TIME_LATE);
    }

    private void loadData(int tp, int pg, boolean isSave) {
        mSwipeRefreshLayout.setRefreshing(true);//show progressbar
        isRequest = true;
        subscription.add(api.getArticleList(tp, mChannelId, Config.PAGESIZE, pg)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<ArticleList>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
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
                        List<Article> articles = articleList.getPage().getList();
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

    /*public void loadChannel(int change) {
        type = change;
        mPage = 1;
        loadData(type, mPage, false);
    }

    public void scrollToTop() {
        if (mRecyclerView != null) {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }*/

}
