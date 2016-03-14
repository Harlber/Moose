package moose.com.ac.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.trello.rxlifecycle.FragmentEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import moose.com.ac.R;
import moose.com.ac.common.Config;
import moose.com.ac.retrofit.Api;
import moose.com.ac.retrofit.comment.CommentDetail;
import moose.com.ac.util.RxUtils;
import moose.com.ac.util.SparseArrayCompatSerializable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
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
 * Created by Farble on 2015/8/16 15.
 * Comment-List-Fragment
 */
public class CommentListFragment extends BaseListFragment {
    private static final String TAG = "CommentListFragment";

    private CompositeSubscription subscription = new CompositeSubscription();
    private Api api = RxUtils.createApi(Api.class, Config.BASE_URL);
    private int contentId;

    private SparseArrayCompatSerializable<CommentDetail> data = new SparseArrayCompatSerializable<>();
    private List<Integer> commentIdList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.abs_list_fragment, container, false);
    }

    /**
     * see http://stackoverflow.com/questions/5412746/android-fragment-onrestoreinstancestate
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            SaveInstance saveInstance = (SaveInstance) savedInstanceState.getSerializable(TAG);
            commentIdList.addAll(saveInstance != null ? saveInstance.getCommentIdList() : new ArrayList<>());
            mPage = saveInstance != null ? saveInstance.getPage() : 1;
            data = saveInstance != null ? saveInstance.getData() : new SparseArrayCompatSerializable<>();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onReceiveData() {
        contentId = getArguments().getInt(Config.CHANNEL_ID);
    }

    @Override
    public void onInitView() {
        initRecyclerView();
        initRefreshLayout();
    }

    @Override
    protected void loadMore() {
        loadData(mPage);
    }

    @Override
    protected void doSwipeRefresh() {
        mPage = 1;
        data.clear();
        commentIdList.clear();
        adapter.notifyDataSetChanged();
        loadData(mPage);
    }

    @Override
    protected void initRecyclerViewAdapter() {
        adapter = new CommentAdapter(getActivity(), data, commentIdList);
    }

    @Override
    public void onInitData() {
        //todo https://code.google.com/p/android/issues/detail?id=77712&can=1&q=SwipeRefreshLayout&sort=-stars&colspec=ID%20Status%20Priority%20Owner%20Summary%20Reporter%20Opened
        mSwipeRefreshLayout.post(() -> loadData(mPage));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        SaveInstance saveInstance = new SaveInstance();
        saveInstance.setCommentIdList(commentIdList);
        saveInstance.setData(data);
        saveInstance.setPage(mPage);
        outState.putSerializable(TAG, saveInstance);
        super.onSaveInstanceState(outState);
    }

    private void loadData(int pg) {
        mSwipeRefreshLayout.setRefreshing(true);
        subscription.add(api.getCommentList(contentId, pg)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<JsonObject>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onCompleted() {
                        mSwipeRefreshLayout.setRefreshing(false);
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
                        JsonObject lists = response.getAsJsonObject("data").getAsJsonObject("commentContentArr");
                        for (int i = 0; i < jsonElements.size(); i++) {
                            int listId = jsonElements.get(i).getAsInt();
                            commentIdList.add(listId);
                            data.put(listId, convertToObject(lists.getAsJsonObject("c" + listId)));
                        }
                        if (data.size() == 0) {
                            snack(getString(R.string.no_comment_here));
                        }
                        adapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                        isRequest = false;
                        Log.i(TAG, "get comments response:" + response.toString());
                        mPage++;
                    }
                }));
    }

    private void snack(String msg) {
        Snackbar snackBar = Snackbar.make(mRecyclerView, msg, Snackbar.LENGTH_SHORT);
        snackBar.setAction(R.string.snackbar_action, v -> {
            snackBar.dismiss();
        });
        snackBar.getView().setBackgroundResource(R.color.colorPrimary);
        snackBar.show();
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

    public static class SaveInstance implements Serializable {

        private static final long serialVersionUID = -3563014084844531564L;
        private SparseArrayCompatSerializable<CommentDetail> data = new SparseArrayCompatSerializable<>();
        private List<Integer> commentIdList = new ArrayList<>();
        private int page;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public SparseArrayCompatSerializable<CommentDetail> getData() {
            return data;
        }

        public void setData(SparseArrayCompatSerializable<CommentDetail> data) {
            this.data = data;
        }

        public List<Integer> getCommentIdList() {
            return commentIdList;
        }

        public void setCommentIdList(List<Integer> commentIdList) {
            this.commentIdList = commentIdList;
        }
    }
}
