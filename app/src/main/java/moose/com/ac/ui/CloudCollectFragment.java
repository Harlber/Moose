package moose.com.ac.ui;
/*
 * Copyright Farble Dast. All rights reserved.
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

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.leakcanary.RefWatcher;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.ArrayList;
import java.util.List;

import moose.com.ac.AppApplication;
import moose.com.ac.R;
import moose.com.ac.common.Config;
import moose.com.ac.retrofit.Api;
import moose.com.ac.retrofit.collect.ArticleCloud;
import moose.com.ac.retrofit.collect.ArticleContent;
import moose.com.ac.ui.widget.DividerItemDecoration;
import moose.com.ac.ui.widget.MultiSwipeRefreshLayout;
import moose.com.ac.util.CommonUtil;
import moose.com.ac.util.RxUtils;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dell on 2015/10/16.
 * CloudCollectFragment
 */
public class CloudCollectFragment extends BaseFragment {
    private static final String TAG = "CloudCollectFragment";

    private CompositeSubscription subscription = new CompositeSubscription();
    private Api api = RxUtils.createCookieApi(Api.class, Config.BASE_URL);
    private List<ArticleContent> list = new ArrayList<>();

    private View rootView;
    private RecyclerView mRecyclerView;
    private TextView showStatus;
    private LinearLayoutManager mLayoutManager;
    private MultiSwipeRefreshLayout mSwipeRefreshLayout;
    private CloudArticleAdapter adapter;

    private boolean isRequest = false;//request data status
    private boolean isScroll = false;//is RecyclerView scrolling
    private int page = 1;//default

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(
                R.layout.fragment_article_list, container, false);
        return rootView;
    }

    @Override
    public void initData() {
        init();
    }

    @Override
    public void initView() {
        showStatus = (TextView) rootView.findViewById(R.id.tv_no);
        initRecyclerView();
        initRefreshLayout();
    }

    protected void initRefreshLayout() {
        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.md_white);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimary);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.recycler_view);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            page = 1;
            load();
        });
    }

    protected void initRecyclerView() {
        adapter = new CloudArticleAdapter(getActivity(), list);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adapter);

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
                if (isScroll && !recyclerView.canScrollVertically(1) && !isRequest) {
                    loadMore();
                }
            }
        });
    }

    private void loadMore() {
        load();
    }

    private void init() {
        if (!CommonUtil.getLoginStatus().equals(Config.LOGIN_IN)) {//not login
            showStatus.setText(R.string.toast_please_login);
            showStatus.setVisibility(View.VISIBLE);
        } else {
            load();
        }
    }

    private void load() {
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setRefreshing(true);//show progressbar
        isRequest = true;
        subscription.add(api.getArticleCloudList(10, page, "63")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<ArticleCloud>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new Observer<ArticleCloud>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        isRequest = false;//refresh request status
                        snack(e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ArticleCloud articleCloud) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        isRequest = false;//refresh request status
                        if (articleCloud.isSuccess()) {
                            list.addAll(articleCloud.getContents());
                            if (articleCloud.getContents().size() == 0) {
                                showStatus.setText(getString(R.string.no_local_data));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            snack(articleCloud.isSuccess() + "");
                        }
                    }
                }));
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //AppApplication.getRefWatcher().watch(this);
    }

    private void snack(String msg) {
        Snackbar snackBar = Snackbar.make(mRecyclerView, msg, Snackbar.LENGTH_SHORT);
        snackBar.setAction(R.string.snackbar_action, v -> {
            snackBar.dismiss();
        });
        snackBar.getView().setBackgroundResource(R.color.colorPrimary);
        snackBar.show();
    }
}
