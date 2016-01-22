package moose.com.ac.ui;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;

import moose.com.ac.AppApplication;
import moose.com.ac.R;
import moose.com.ac.common.Config;
import moose.com.ac.retrofit.Api;
import moose.com.ac.retrofit.comment.CommentDetail;
import moose.com.ac.ui.widget.DividerItemDecoration;
import moose.com.ac.ui.widget.MultiSwipeRefreshLayout;
import moose.com.ac.util.RxUtils;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
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
 * Created by Farble on 2015/8/16 15.
 * Comment-List-Fragment
 */
public class CommentListFragment extends Fragment {
    private static final String TAG = "CommentListFragment";
    private View rootView;
    private CompositeSubscription subscription = new CompositeSubscription();
    private Api api;
    private int contentId;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MultiSwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isRequest = false;//request data status
    private boolean isScroll = false;//is RecyclerView scrolling
    private int page = 1;//default

    private CommentAdapter adapter;
    private SparseArray<CommentDetail> data = new SparseArray<>();
    private List<Integer> commentIdList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(
                R.layout.fragment_comment_list, container, false);
        api = RxUtils.createApi(Api.class, Config.BASE_URL);
        contentId = getArguments().getInt(Config.CHANNEL_ID);
        adapter = new CommentAdapter(getActivity(), data, commentIdList);
        initRecyclerView();
        initRefreshLayout();
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));
        new Handler().postDelayed(() -> {
            loadData(page);
        }, Config.TIME_LATE);
        return rootView;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = AppApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    private void initRefreshLayout() {

        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);

        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.md_orange_700, R.color.md_red_500,
                R.color.md_indigo_900, R.color.md_green_700);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.recycler_view);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
            doSwipeRefresh();
        });
    }

    private void initRecyclerView() {

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
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

    private void doSwipeRefresh() {
        loadData(page);
    }

    private void loadMore() {
        loadData(page);
    }

    private void loadData(int pg) {
        subscription.add(api.getCommentList(contentId, pg)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        e.printStackTrace();
                        snack(e.getMessage());
                    }

                    @Override
                    public void onNext(JsonObject response) {
                        JsonArray jsonElements = response.getAsJsonObject("data").getAsJsonArray("commentList");
                        JsonObject comlists = response.getAsJsonObject("data").getAsJsonObject("commentContentArr");
                        for (int i = 0; i < jsonElements.size(); i++) {
                            int listid = jsonElements.get(i).getAsInt();
                            commentIdList.add(listid);
                            data.put(listid, convertToObject(comlists.getAsJsonObject("c" + listid)));
                        }
                        if (data.size() == 0) {
                            snack(getString(R.string.no_comment_here));
                        }
                        adapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                        isRequest = false;//refresh request status
                        Log.i(TAG, "get comments response:" + response.toString());
                        page++;//超出范围未做处理
                    }
                }));
    }

    private void snack(String msg) {
        Snackbar.make(mRecyclerView, msg, Snackbar.LENGTH_SHORT).show();
    }

    private CommentDetail convertToObject(JsonObject object) {
        CommentDetail detail = new CommentDetail();
        detail.setCid(object.get("cid").getAsLong());
        detail.setQuoteId(object.get("quoteId").getAsLong());
        detail.setContent(object.get("content").getAsString());
        detail.setPostDate(object.get("postDate").getAsString());

        detail.setUserID(object.get("userID").getAsLong());
        detail.setUserName(object.get("userName").getAsString());
        //detail.setUserImg(object.get("userImg").getAsString());

        detail.setCount(object.get("count").getAsLong());
        detail.setDeep(object.get("deep").getAsLong());
        detail.setRefCount(object.get("refCount").getAsLong());
        detail.setUps(object.get("ups").getAsLong());
        detail.setDowns(object.get("downs").getAsLong());
        detail.setNameRed(object.get("nameRed").getAsLong());
        detail.setAvatarFrame(object.get("avatarFrame").getAsLong());
        return detail;
    }
}
