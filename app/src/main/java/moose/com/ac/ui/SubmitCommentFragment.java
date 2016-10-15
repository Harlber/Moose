package moose.com.ac.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trello.rxlifecycle.FragmentEvent;

import java.util.HashMap;

import moose.com.ac.LoginActivity;
import moose.com.ac.R;
import moose.com.ac.common.Config;
import moose.com.ac.retrofit.Api;
import moose.com.ac.retrofit.comment.CommentSend;
import moose.com.ac.ui.widget.MultiSwipeRefreshLayout;
import moose.com.ac.util.CommonUtil;
import moose.com.ac.util.RxUtils;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
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
public class SubmitCommentFragment extends BaseFragment {
    private CompositeSubscription subscription = new CompositeSubscription();
    private Api api = RxUtils.createCookieApi(Api.class, Config.BASE_URL);
    private MultiSwipeRefreshLayout mSwipeRefreshLayout;
    private AppCompatEditText editTextComment;
    private AppCompatButton buttonSend;

    private RelativeLayout layout;
    private LinearLayout commentLayout;
    private TextView login;

    private int contentId;

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(getString(R.string.login_action))) {
                commentLayout.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
            }
        }
    };

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.comment_send:
                    HashMap<String, String> map = new HashMap<>();
                    map.put("name", "sendComm()");
                    map.put("token", "mimiko");
                    map.put("quoteId", "0");
                    map.put("text", editTextComment.getText().toString());
                    map.put("cooldown", "5000");
                    map.put("contentId", String.valueOf(contentId));
                    map.put("quoteName", "");
                    doSend(map);
                    break;
                case R.id.tv_no:
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    break;
                default:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_submit_comment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onReceiveData() {
        contentId = getArguments().getInt(Config.CHANNEL_ID);
    }

    @Override
    public void onInitView() {
        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) getRootView().findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.md_white);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimary);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.recycler_view);

        editTextComment = (AppCompatEditText) getRootView().findViewById(R.id.comment_content);
        buttonSend = (AppCompatButton) getRootView().findViewById(R.id.comment_send);
        layout = (RelativeLayout) getRootView().findViewById(R.id.comment_no_layout);
        login = (TextView) getRootView().findViewById(R.id.tv_no);
        commentLayout = (LinearLayout) getRootView().findViewById(R.id.comment_layout);

        buttonSend.setOnClickListener(listener);
        login.setOnClickListener(listener);

        if (!CommonUtil.getLoginStatus().equals(Config.LOGIN_IN)) {
            commentLayout.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onInitData() {

    }

    private void doSend(HashMap<String, String> map) {
        mSwipeRefreshLayout.setRefreshing(true);//show progressbar
        subscription.add(api.sendComment(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<CommentSend>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new Observer<CommentSend>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        snack(e.getMessage());
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(CommentSend articleList) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        if (articleList.getStatus() == 0) {
                            doOnPostCommentSuccess();
                        }
                    }
                }));
    }

    /**
     * After successful submission of the reply message,need to refresh the list of comments
     */
    private void doOnPostCommentSuccess() {
        Intent intent = new Intent(getString(R.string.send_action));
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    public void snack(String msg) {
        Snackbar snackBar = Snackbar.make(buttonSend, msg, Snackbar.LENGTH_SHORT);
        snackBar.setAction(R.string.snackbar_action, v -> {
            snackBar.dismiss();
        });
        snackBar.getView().setBackgroundResource(R.color.colorPrimary);
        snackBar.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter(getString(R.string.login_action)));
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    public static SubmitCommentFragment newInstance(int contendId) {
        Bundle args = new Bundle();
        SubmitCommentFragment fragment = new SubmitCommentFragment();
        args.putInt(Config.CHANNEL_ID, contendId);
        fragment.setArguments(args);
        return fragment;
    }

}
