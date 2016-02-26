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

import jp.wasabeef.recyclerview.animators.FlipInTopXAnimator;
import moose.com.ac.R;
import moose.com.ac.common.Config;
import moose.com.ac.retrofit.Api;
import moose.com.ac.retrofit.search.SearchBody;
import moose.com.ac.retrofit.search.SearchList;
import moose.com.ac.ui.widget.MultiSwipeRefreshLayout;
import moose.com.ac.util.RxUtils;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
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
 * Created by dell on 2015/9/15.
 * SearchFragment
 */
public class SearchFragment extends BaseFragment {
    private static final String TAG = "SearchFragment";
    private static final int ANIMATION_DURATION = 2000;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MultiSwipeRefreshLayout mSwipeRefreshLayout;
    private FlipInTopXAnimator animator;
    private TextView result;

    private List<SearchList> lists = new ArrayList<>();
    private SearchListAdapter adapter;
    private boolean isRequest = false;//request data status
    private String key;
    private CompositeSubscription subscription = new CompositeSubscription();
    private Api api;
    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_search, container, false);
    }

    @Override
    public void onReceiveData() {
        key = getArguments().getString(Config.SEARCH_KEY);
    }

    @Override
    public void onInitView() {
        result = (TextView) getRootView().findViewById(R.id.search_result);
        initRecyclerView();
        initRefreshLayout();
    }

    @Override
    public void onInitData() {
        loadData(1);
    }

    private void loadData(int flag) {
        api = RxUtils.createApi(Api.class, Config.SEARCH_URL);
        mSwipeRefreshLayout.setRefreshing(true);
        subscription.add(api.getSearch(key, page, Config.PAGESIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<SearchBody>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new Observer<SearchBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(mRecyclerView, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(SearchBody searchBody) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        if (searchBody.getData().getPage() != null &&
                                searchBody.getData().getPage().getList().size() != 0) {
                            result.setText(String.format(getActivity().getString(R.string.total_result),
                                    searchBody.getData().getPage().getTotalCount().toString()));
                            if (flag != 1) {
                                lists.clear();
                            }
                            lists.addAll(searchBody.getData().getPage().getList());
                            adapter.notifyDataSetChanged();
                            ++page;
                        }
                    }
                }));
    }

    protected void initRefreshLayout() {
        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) getRootView().findViewById(R.id.swiperefresh);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.md_white);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimary);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.recycler_view);
        mSwipeRefreshLayout.setOnRefreshListener(this::doSwipeRefresh);
    }

    private void doSwipeRefresh() {
        loadData(0);
    }


    protected void initRecyclerView() {
        mRecyclerView = (RecyclerView) getRootView().findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        animator = new FlipInTopXAnimator();
        animator.setAddDuration(ANIMATION_DURATION);
        animator.setRemoveDuration(ANIMATION_DURATION);
        mRecyclerView.setItemAnimator(animator);
        adapter = new SearchListAdapter(lists, getActivity());
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mSwipeRefreshLayout.setEnabled(mLayoutManager
                        .findFirstCompletelyVisibleItemPosition() == 0);//fix bug while scroll RecyclerView & SwipeRefreshLayout shows also
                if (!recyclerView.canScrollVertically(1) && !isRequest) {
                    loadMore();
                }
            }
        });

    }

    private void loadMore() {
        loadData(1);
    }

    public List<SearchList> getLists() {
        return lists;
    }

    @Override
    public void onPause() {
        super.onPause();
        mSwipeRefreshLayout.setRefreshing(false);
    }

}
