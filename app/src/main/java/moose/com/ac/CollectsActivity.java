package moose.com.ac;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Collections;
import java.util.List;

import moose.com.ac.data.ArticleCollects;
import moose.com.ac.data.RxDataBase;
import moose.com.ac.retrofit.article.Article;
import moose.com.ac.ui.ArticleListActivity;
import moose.com.ac.util.SortUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
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
 * local collection
 */
public final class CollectsActivity extends ArticleListActivity {
    private static final String TAG = "CollectsActivity";
    private Subscriber<List<Article>> listSubscriber;
    private Subscriber<Integer> deleteSubscriber;

    @Override
    protected void initToolBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.my_store));
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        //noinspection ConstantConditions
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void init() {
        rxDataBase = new RxDataBase(ArticleCollects.ArticleEntry.TABLE_NAME);
        listSubscriber = newInstance();
        deleteSubscriber = newDeleteInstance();
        mSwipeRefreshLayout.setRefreshing(true);
        rxDataBase.favLists
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listSubscriber);
    }

    @Override
    protected void loadMore() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_collect, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                CollectsActivity.this.finish();
                return true;
            case R.id.action_time_store://sort by store date
                SortUtil.StoreComparator comparator = new SortUtil.StoreComparator();
                Collections.sort(lists, comparator);
                adapter.notifyDataSetChanged();
                return true;
            case R.id.action_time_push://push time
                SortUtil.PushComparator pushComparator = new SortUtil.PushComparator();
                Collections.sort(lists, pushComparator);
                adapter.notifyDataSetChanged();
                return true;
            case R.id.action_clear_store:
                clearDialog(R.string.clear_store_all).show();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listSubscriber == null || listSubscriber.isUnsubscribed()) {
            listSubscriber = newInstance();
        }
        if (deleteSubscriber == null || deleteSubscriber.isUnsubscribed()) {
            deleteSubscriber = newDeleteInstance();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (listSubscriber != null) {
            listSubscriber.unsubscribe();
        }
        if (deleteSubscriber != null) {
            deleteSubscriber.unsubscribe();
        }
    }

    private Subscriber<List<Article>> newInstance() {
        return new Subscriber<List<Article>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setEnabled(false);
                Snack(getString(R.string.db_error));
            }

            @Override
            public void onNext(List<Article> articles) {
                lists.addAll(articles);
                adapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setEnabled(false);
                if (lists.size() == 0) {
                    Snack(getString(R.string.no_collects_now));
                    new Handler().postDelayed(CollectsActivity.this::finish, 2000);
                }
            }
        };
    }

    private Dialog clearDialog(@StringRes int str) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(CollectsActivity.this);
        builder.setMessage(str)
                .setPositiveButton(R.string.positive, (dialog, id) -> {
                    if (lists.size()==0) {
                        Snack(getString(R.string.no_data_to_clear));
                    }else {
                        mSwipeRefreshLayout.setEnabled(true);
                        mSwipeRefreshLayout.setRefreshing(true);
                        rxDataBase.dropTable
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(deleteSubscriber);
                    }
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {

                });
        return builder.create();
    }
}
