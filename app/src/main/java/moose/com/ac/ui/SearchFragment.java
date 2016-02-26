package moose.com.ac.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trello.rxlifecycle.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import moose.com.ac.R;
import moose.com.ac.common.Config;
import moose.com.ac.retrofit.Api;
import moose.com.ac.retrofit.search.SearchBody;
import moose.com.ac.retrofit.search.SearchList;
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
public class SearchFragment extends BaseListFragment {
    private static final String TAG = "SearchFragment";
    private TextView result;

    private List<SearchList> lists = new ArrayList<>();
    private String key;
    private CompositeSubscription subscription = new CompositeSubscription();
    private Api api = RxUtils.createApi(Api.class, Config.SEARCH_URL);

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
    protected void loadMore() {
        loadData(1);
    }

    @Override
    protected void initRecyclerViewAdapter() {
        adapter = new SearchListAdapter(lists, getActivity());
    }

    @Override
    protected void doSwipeRefresh() {
        lists.clear();
        adapter.notifyDataSetChanged();
        loadData(0);
    }

    @Override
    public void onInitData() {
        loadData(1);
    }

    private void loadData(int flag) {
        mSwipeRefreshLayout.setRefreshing(true);
        subscription.add(api.getSearch(key, mPage, Config.PAGESIZE)
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
                            mPage++;
                        }
                    }
                }));
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
