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
import moose.com.ac.retrofit.collect.ArticleCloud;
import moose.com.ac.retrofit.collect.ArticleContent;
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
public class CloudCollectFragment extends BaseListFragment {
    private static final String TAG = "CloudCollectFragment";

    private CompositeSubscription subscription = new CompositeSubscription();
    private Api api = RxUtils.createCookieApi(Api.class, Config.BASE_URL);
    private List<ArticleContent> list = new ArrayList<>();

    private TextView showStatus;

    private int page = 1;//default

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(
                R.layout.abs_list_fragment, container, false);
    }

    @Override
    public void onReceiveData() {

    }

    @Override
    public void onInitData() {
        init();
    }

    @Override
    public void onInitView() {
        showStatus = (TextView) getRootView().findViewById(R.id.tv_no);
        initRecyclerView();
        initRefreshLayout();
    }

    @Override
    protected void loadMore() {
        load();
    }


    @Override
    protected void doSwipeRefresh() {
        load();
    }

    @Override
    protected void initRecyclerViewAdapter() {
        adapter = new CloudArticleAdapter(getActivity(), list);
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

    private void snack(String msg) {
        Snackbar snackBar = Snackbar.make(mRecyclerView, msg, Snackbar.LENGTH_SHORT);
        snackBar.setAction(R.string.snackbar_action, v -> {
            snackBar.dismiss();
        });
        snackBar.getView().setBackgroundResource(R.color.colorPrimary);
        snackBar.show();
    }
}
