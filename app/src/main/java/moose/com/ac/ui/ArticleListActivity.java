package moose.com.ac.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import moose.com.ac.R;
import moose.com.ac.data.DbHelper;
import moose.com.ac.data.RxDataBase;
import moose.com.ac.retrofit.article.Article;
import moose.com.ac.ui.widget.MultiSwipeRefreshLayout;
import rx.Subscriber;
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
 * Created by dell on 2015/8/25.
 */
public abstract class ArticleListActivity extends BaseActivity {
    private static final String TAG = "ArticleListActivity";
    protected DbHelper dbHelper;
    protected RecyclerView mRecyclerView;
    protected LinearLayoutManager mLayoutManager;
    protected MultiSwipeRefreshLayout mSwipeRefreshLayout;

    protected List<Article> lists = new ArrayList<>();
    protected CacheListAdapter adapter;
    protected boolean isRequest = false;//request data status
    protected boolean isScroll = false;//is RecyclerView scrolling

    protected RxDataBase rxDataBase;

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setContentView(R.layout.abs_article_list);
        initToolBar();
        dbHelper = new DbHelper(this);
        initRecyclerView();
        initRefreshLayout();
        init();
    }

    protected void initRefreshLayout() {
        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) findViewById(R.id.swiperefresh);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.md_white);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimary);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.recycler_view);
        mSwipeRefreshLayout.setOnRefreshListener(() -> mSwipeRefreshLayout.setRefreshing(false));
    }


    protected void initRecyclerView() {
        adapter = new CacheListAdapter(lists, this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
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

    protected abstract void init();

    protected abstract void loadMore();

    protected abstract void initToolBar();

    protected void Snack(String msg) {
        Snackbar.make(mRecyclerView, msg, Snackbar.LENGTH_SHORT).show();
    }

    protected Subscriber<Integer> newDeleteInstance() {
        return new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setEnabled(false);
                Snack(getString(R.string.remove_data_error));
            }

            @Override
            public void onNext(Integer integer) {
                if (integer > 1) {
                    lists.clear();
                    adapter.notifyDataSetChanged();
                    Snack(getString(R.string.data_clear_already));
                } else {
                    Snack(getString(R.string.delete_data_failed));
                }
                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setEnabled(false);
            }
        };
    }
}
