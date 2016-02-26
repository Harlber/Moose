package moose.com.ac.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trello.rxlifecycle.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import moose.com.ac.R;
import moose.com.ac.data.ArticleCollects;
import moose.com.ac.data.DbHelper;
import moose.com.ac.data.RxDataBase;
import moose.com.ac.retrofit.article.Article;
import moose.com.ac.ui.widget.DividerItemDecoration;
import moose.com.ac.ui.widget.MultiSwipeRefreshLayout;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
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

/**
 * Created by dell on 2015/10/17.
 */
public class LocalCollectFragment extends BaseFragment {
    private static final String TAG = "LocalCollectFragment";
    private DbHelper dbHelper;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MultiSwipeRefreshLayout mSwipeRefreshLayout;
    private TextView textView;

    private List<Article> lists = new ArrayList<>();
    private CacheListAdapter adapter;
    private boolean isRequest = false;//request data status
    private boolean isScroll = false;//is RecyclerView scrolling

    protected RxDataBase rxDataBase;
    private Subscriber<List<Article>> listSubscriber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.abs_list_fragment, container, false);
    }

    @Override
    public void onReceiveData() {

    }

    @Override
    public void onInitView() {
        initRecyclerView();
        initRefreshLayout();
        textView = (TextView) getRootView().findViewById(R.id.tv_no);
    }

    @Override
    public void onInitData() {
        dbHelper = new DbHelper(getActivity());
        init();
    }

    private void init() {
        rxDataBase = new RxDataBase(ArticleCollects.ArticleEntry.TABLE_NAME);
        listSubscriber = newInstance();
        load();
    }

    private void load() {
        mSwipeRefreshLayout.setRefreshing(true);
        rxDataBase.favLists
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<List<Article>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(listSubscriber);
    }

    private void initRefreshLayout() {
        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) getRootView().findViewById(R.id.swiperefresh);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.md_white);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimary);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.recycler_view);
        mSwipeRefreshLayout.setOnRefreshListener(this::refresh);
    }

    private void refresh() {
        //todo refresh data from DB
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void initRecyclerView() {
        adapter = new CacheListAdapter(lists, getActivity());
        mRecyclerView = (RecyclerView) getRootView().findViewById(R.id.recycler_view);
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
            }
        });
    }

    private Subscriber<List<Article>> newInstance() {
        return new Subscriber<List<Article>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setEnabled(false);
                snack(getString(R.string.db_error));
            }

            @Override
            public void onNext(List<Article> articles) {
                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setEnabled(false);
                if (articles.size() == 0) {
                    textView.setVisibility(View.VISIBLE);
                } else {
                    textView.setVisibility(View.GONE);
                    lists.addAll(articles);
                    adapter.notifyDataSetChanged();
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listSubscriber == null || listSubscriber.isUnsubscribed()) {
            listSubscriber = newInstance();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (listSubscriber != null) {
            listSubscriber.unsubscribe();
        }
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
