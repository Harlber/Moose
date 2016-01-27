package moose.com.ac.ui;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.squareup.leakcanary.RefWatcher;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FlipInTopXAnimator;
import moose.com.ac.AppApplication;
import moose.com.ac.R;
import moose.com.ac.retrofit.article.Article;
import moose.com.ac.ui.widget.DividerItemDecoration;
import moose.com.ac.ui.widget.MultiSwipeRefreshLayout;
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
 * Created by Farble on 2015/8/13 23.
 */
public abstract class ArticleListFragment extends RxFragment {
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

        mSwipeRefreshLayout.setColorSchemeResources(R.color.md_white);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimary);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.recycler_view);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
            doSwipeRefresh();
        });
    }


    protected void initRecyclerView() {
        adapter = new ArticleListAdapter(lists, getActivity());
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

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

    protected abstract void doSwipeRefresh();

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = AppApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    @Deprecated
    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }
}
