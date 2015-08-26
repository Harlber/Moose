package moose.com.ac;

import android.app.AlertDialog;
import android.app.Dialog;
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

/**
 * Created by dell on 2015/8/25.
 */
public final class Collects extends ArticleListActivity {
    private static final String TAG = "Collects";
    private RxDataBase rxDataBase;
    private Subscriber<List<Article>> listSubscriber;
    private Subscriber<Integer> deleteSubscriber;

    @Override
    protected void initToolBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.news_toolbar);
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
                Collects.this.finish();
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
                }
            }
        };
    }

    private Subscriber<Integer> newDeleteInstance() {
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
                if (integer>1) {
                    lists.clear();
                    adapter.notifyDataSetChanged();
                    Snack(getString(R.string.data_clear_already));
                }else {
                    Snack(getString(R.string.delete_data_dalse));
                }
                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setEnabled(false);
            }
        };
    }

    private Dialog clearDialog(@StringRes int str) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(Collects.this);
        builder.setMessage(str)
                .setPositiveButton(R.string.positive, (dialog, id) -> {
                    mSwipeRefreshLayout.setEnabled(true);
                    mSwipeRefreshLayout.setRefreshing(true);
                    rxDataBase.dropTable
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(deleteSubscriber);
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {

                });
        return builder.create();
    }
}
