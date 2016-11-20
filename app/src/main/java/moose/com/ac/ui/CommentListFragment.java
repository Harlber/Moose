package moose.com.ac.ui;


import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.trello.rxlifecycle.FragmentEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import moose.com.ac.R;
import moose.com.ac.common.Config;
import moose.com.ac.event.CommentEvent;
import moose.com.ac.retrofit.Api;
import moose.com.ac.retrofit.comment.CommentListWrapper;
import moose.com.ac.util.RxBus;
import moose.com.ac.util.RxUtils;
import moose.com.ac.util.SparseArrayCompatUtil;
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
    private Api api = RxUtils.createApi(Api.class, Config.COMMENT_URL);
    private int contentId;
    private FloatingActionButton mFloatingActionButton;

    private SparseArrayCompat<CommentListWrapper.Comment> data = new SparseArrayCompat<>();
    private List<Integer> commentIdList = new ArrayList<>();
    @NonNull
    private CommentAdapter.OnItemClickListener mOnItemClickListener = new CommentAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            CommentListWrapper.Comment comment = data.get(commentIdList.get(position));
            createPopupMenu(view, comment);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, CommentListWrapper.Comment comment) {
            createPopupMenu(view, comment);
        }
    };

    public static CommentListFragment newInstance(int contendId) {
        Bundle args = new Bundle();
        CommentListFragment fragment = new CommentListFragment();
        args.putInt(Config.CHANNEL_ID, contendId);
        fragment.setArguments(args);
        return fragment;
    }

    private void createPopupMenu(View view, @NonNull final CommentListWrapper.Comment comment) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        popupMenu.inflate(R.menu.menu_popup_comment);
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_quote:
                    QuoteCommentDialogFragment fragment = QuoteCommentDialogFragment.newInstance(comment, contentId);
                    fragment.show(getActivity().getSupportFragmentManager());
                    break;
            }
            return false;
        });
        popupMenu.show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.abs_list_fragment, container, false);
    }

    /**
     * see http://stackoverflow.com/questions/5412746/android-fragment-onrestoreinstancestate
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            SaveInstance saveInstance = savedInstanceState.getParcelable(TAG);
            commentIdList.addAll(saveInstance != null ? saveInstance.getCommentIdList() : new ArrayList<>());
            mPage = saveInstance != null ? saveInstance.getPage() : 1;
            data = saveInstance != null ? saveInstance.getData() : new SparseArrayCompat<>();
        }
    }

    @Override
    public void onReceiveData() {
        contentId = getArguments().getInt(Config.CHANNEL_ID);
    }

    @Override
    public void onResume() {
        super.onResume();
        RxBus.get().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        RxBus.get().unregister(this);
    }

    private int mLastVisibleItemPosition;
    @Override
    public void onInitView() {
        initRecyclerView();
        mRecyclerView.clearOnScrollListeners();//clear BaseListFragment data
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItemPosition + 1 == adapter.getItemCount()) {
                    loadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            }
        });
        initRefreshLayout();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.view_fab);
        mFloatingActionButton.setOnClickListener(v -> {
            QuoteCommentDialogFragment fragment = QuoteCommentDialogFragment.newInstance(null, contentId);
            fragment.show(getActivity().getSupportFragmentManager());
        });
    }

    @Subscribe
    public void refreshCommentData(CommentEvent event) {
        if (event.type == CommentEvent.TYPE_REFRESH_COMMENT) {
            doSwipeRefresh();
        }
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
        loadData(mPage);
    }

    @Override
    protected void initRecyclerViewAdapter() {
        adapter = new CommentAdapter(getActivity(), data, commentIdList);
        ((CommentAdapter) adapter).setOnItemClickListener(mOnItemClickListener);
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
        outState.putParcelable(TAG, saveInstance);
        super.onSaveInstanceState(outState);
    }

    private void loadData(int pg) {
        mSwipeRefreshLayout.setRefreshing(true);
        subscription.add(api.getCommentList(contentId, 50, pg)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new Observer<CommentListWrapper>() {
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
                    public void onNext(CommentListWrapper commentListWrapper) {
                        List<Integer> list = commentListWrapper.data.page.list;
                        Map<String, CommentListWrapper.Comment> map = commentListWrapper.data.page.map;
                        commentIdList.addAll(list);
                        for (int i = 0; i < list.size(); i++) {
                            int listId = list.get(i);
                            data.put(listId, map.get("c" + listId));
                        }
                        if (data.size() == 0) {
                            snack(getString(R.string.no_comment_here));
                        }
                        ((CommentAdapter) adapter).setData(data, commentIdList);
                        adapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                        isRequest = false;
                        mPage++;
                    }
                }));
    }

    private void snack(String msg) {
        Snackbar snackBar = Snackbar.make(mRecyclerView, msg, Snackbar.LENGTH_SHORT);
        snackBar.setAction(R.string.snackbar_action, v -> {});
        snackBar.getView().setBackgroundResource(R.color.colorPrimary);
        snackBar.show();
    }


    public static class SaveInstance implements Parcelable {

        public static final Creator<SaveInstance> CREATOR = new Creator<SaveInstance>() {
            @Override
            public SaveInstance createFromParcel(Parcel source) {
                return new SaveInstance(source);
            }

            @Override
            public SaveInstance[] newArray(int size) {
                return new SaveInstance[size];
            }
        };
        private SparseArrayCompat<CommentListWrapper.Comment> data = new SparseArrayCompat<>();
        private List<Integer> commentIdList = new ArrayList<>();
        private int page;

        public SaveInstance() {
        }

        protected SaveInstance(Parcel in) {
            this.data = SparseArrayCompatUtil.readSparseArrayCompat(in);
            this.commentIdList = new ArrayList<>();
            in.readList(this.commentIdList, Integer.class.getClassLoader());
            this.page = in.readInt();
        }

        public SparseArrayCompat<CommentListWrapper.Comment> getData() {
            return data;
        }

        public void setData(SparseArrayCompat<CommentListWrapper.Comment> data) {
            this.data = data;
        }

        public List<Integer> getCommentIdList() {
            return commentIdList;
        }

        public void setCommentIdList(List<Integer> commentIdList) {
            this.commentIdList = commentIdList;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            SparseArrayCompatUtil.writeSparseArrayCompat(dest, this.data);
            dest.writeList(this.commentIdList);
            dest.writeInt(this.page);
        }
    }
}
