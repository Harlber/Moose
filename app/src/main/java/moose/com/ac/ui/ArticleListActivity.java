package moose.com.ac.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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

/**
 * Created by dell on 2015/8/25.
 */
public abstract class ArticleListActivity extends AppCompatActivity {
    private static final String TAG = "ArticleListActivity";
    protected DbHelper dbHelper;
    protected RecyclerView mRecyclerView;
    protected LinearLayoutManager mLayoutManager;
    protected MultiSwipeRefreshLayout mSwipeRefreshLayout;

    protected List<Article> lists = new ArrayList<>();
    protected ArticleListAdapter adapter;
    protected boolean isRequest = false;//request data status
    protected boolean isScroll = false;//is RecyclerView scrolling

    protected RxDataBase rxDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abs_article_list);
        initToolBar();
        dbHelper = new DbHelper(this);
        initRecyclerView();
        initRefreshLayout();
        init();

    }

    protected void initRefreshLayout() {
        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) findViewById(R.id.swiperefresh);

        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.md_orange_700, R.color.md_red_500,
                R.color.md_indigo_900, R.color.md_green_700);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.recycler_view);
        mSwipeRefreshLayout.setOnRefreshListener(() -> mSwipeRefreshLayout.setRefreshing(false));
    }


    protected void initRecyclerView() {
        adapter = new ArticleListAdapter(lists, this);
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
}
