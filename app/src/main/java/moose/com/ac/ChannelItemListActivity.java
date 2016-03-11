package moose.com.ac;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.trello.rxlifecycle.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import moose.com.ac.common.Config;
import moose.com.ac.retrofit.Api;
import moose.com.ac.retrofit.article.Article;
import moose.com.ac.retrofit.article.ArticleList;
import moose.com.ac.ui.ArticleListAdapter;
import moose.com.ac.ui.BaseActivity;
import moose.com.ac.ui.widget.DividerItemDecoration;
import moose.com.ac.ui.widget.MultiSwipeRefreshLayout;
import moose.com.ac.util.RxUtils;
import moose.com.ac.util.SettingPreferences;
import moose.com.ac.util.chrome.CustomTabActivityHelper;
import moose.com.ac.util.chrome.WebviewFallback;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/*
 * Copyright 2016 Farble Dast. All rights reserved.
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
public class ChannelItemListActivity extends BaseActivity implements ChannelManager {
    private static final String TAG = "ChannelItemListActivity";
    private FloatingActionButton fab;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MultiSwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isScroll = false;//is RecyclerView scrolling
    private boolean isRequest = false;//request status

    private List<Article> lists = new ArrayList<>();
    private CompositeSubscription subscription = new CompositeSubscription();
    private Api api = RxUtils.createApi(Api.class, Config.ARTICLE_URL);
    private ArticleListAdapter adapter;
    private int type = 3;//default

    private
    @ChannelMode
    int channel;

    private int mPage = 1;//default

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fab:
                    if (mRecyclerView != null) {
                        mRecyclerView.smoothScrollToPosition(0);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_channel_item_list);
        //noinspection WrongConstant
        channel = getIntent().getIntExtra(Config.CHANNEL_ID, ChannelManager.DEFAULT);
        initView();
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                load(type, mPage, true);
            }
        }, Config.TIME_LATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /**see http://stackoverflow.com/questions/27556623/creating-a-searchview-that-looks-like-the-material-design-guidelines*/
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_fetch:
                fetchDialog();
                return true;

            case android.R.id.home:
                ChannelItemListActivity.this.finish();
                return true;
            case R.id.action_filter:
                showFilterDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("WrongConstant")
    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChannelItemListActivity.this);
        //noinspection RedundantCast
        builder.setTitle(getString(R.string.article_select))
                .setItems(R.array.select_channel_array, (DialogInterface.OnClickListener) (dialog, which) -> {
                    type = which;
                    getSupportActionBar().setTitle(filterTitle(type));
                    getSupportActionBar().setSubtitle(getToolBarSubTitle());
                    doSwipeRefresh();
                });
        builder.create().show();
    }

    private void fetchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChannelItemListActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_fetch, null);
        builder.setView(rootView)
                // Add action buttons
                .setTitle(R.string.fetch)
                .setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        AppCompatEditText input = (AppCompatEditText) rootView.findViewById(R.id.dialog_fetch_input);
                        if (!input.getText().toString().equals("")) {
                            if (!SettingPreferences.externalBrowserEnabled(ChannelItemListActivity.this)) {
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                Uri content_url = Uri.parse(Config.WEB_URL + input.getText().toString() + "/");
                                intent.setData(content_url);
                                startActivity(intent);
                            } else {
                                String url = Config.WAP_URL + "v#ac=" + input.getText().toString() + ";type=article";
                                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
                                CustomTabActivityHelper.openCustomTab(
                                        ChannelItemListActivity.this, customTabsIntent, Uri.parse(url), new WebviewFallback());
                            }
                        }
                    }
                });
        builder.show();
    }

    private String getToolBarSubTitle() {
        if (type == 0) {
            return getString(R.string.last_comment);
        } else if (type == 1) {
            return getString(R.string.most_views);
        } else {
            return getString(R.string.most_comment);
        }
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(filterTitle(channel));
        getSupportActionBar().setSubtitle(getString(R.string.last_comment));

        adapter = new ArticleListAdapter(lists, this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(listener);
        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.md_white);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimary);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.recycler_view);
        mSwipeRefreshLayout.setOnRefreshListener(this::doSwipeRefresh);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));

        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

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
                if (!recyclerView.canScrollVertically(1) && !isRequest) {
                    loadMore();
                }
            }
        });
    }

    private String filterTitle(@ChannelMode int i) {
        switch (i) {
            case HOT:
                return getString(R.string.hot);
            case COMPLEX:
                return getString(R.string.complex);
            case WORK_EMOTION:
                return getString(R.string.work);
            case ANIMATION_CULTURE:
                return getString(R.string.animation);
            case COMIC_FICTION:
                return getString(R.string.cartoon);
            case GAME:
                return getString(R.string.game);
            default:
                return getString(R.string.complex);
        }
    }

    private void doSwipeRefresh() {
        load(type, mPage, false);
    }

    private void loadMore() {
        load(type, mPage, true);
    }

    private void load(int tp, int pg, boolean isSave) {
        mSwipeRefreshLayout.setRefreshing(true);//show progressbar
        isRequest = true;
        subscription.add(api.getArticleList(tp, channel, pg, Config.PAGESIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<ArticleList>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Observer<ArticleList>() {
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
                    public void onNext(ArticleList articleList) {
                        if (isSave) {
                            mPage++;//false : new request
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                        List<Article> articles = new ArrayList<Article>();
                        articles = articleList.getData().getPage().getList();
                        if (isSave) {//add data into local
                            lists.addAll(articles);
                        } else {
                            lists.clear();//clear local data
                            lists.addAll(articles);
                        }
                        articles.clear();
                        adapter.notifyDataSetChanged();
                        isRequest = false;//refresh request status
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
}
