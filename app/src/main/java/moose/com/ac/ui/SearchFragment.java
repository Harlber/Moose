package moose.com.ac.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FlipInTopXAnimator;
import moose.com.ac.R;
import moose.com.ac.common.Config;
import moose.com.ac.retrofit.Api;
import moose.com.ac.retrofit.search.SearchBody;
import moose.com.ac.retrofit.search.SearchList;
import moose.com.ac.ui.widget.MultiSwipeRefreshLayout;
import moose.com.ac.util.RxUtils;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by dell on 2015/9/15.
 * SearchFragment
 */
public class SearchFragment extends RxFragment {
    private static final String TAG = "SearchFragment";
    private static final int ANIMATION_DURATION = 2000;
    private View rootView;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MultiSwipeRefreshLayout mSwipeRefreshLayout;
    private FlipInTopXAnimator animator;
    private TextView result;

    private List<SearchList> lists = new ArrayList<>();
    private SearchListAdapter adapter;
    private boolean isRequest = false;//request data status
    private String key;
    private CompositeSubscription subscription = new CompositeSubscription();
    private Api api;
    private int page = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(
                R.layout.fragment_search, container, false);
        key = getArguments().getString(Config.SEARCH_KEY);
        result = (TextView) rootView.findViewById(R.id.search_result);
        initRecyclerView();
        initRefreshLayout();
        new Handler().postDelayed(() -> loadData(1), Config.TIME_LATE);
        return rootView;
    }

    private void loadData(int flag) {
        api = RxUtils.createApi(Api.class, Config.SEARCH_URL);
        mSwipeRefreshLayout.setRefreshing(true);
        subscription.add(api.getSearch(key, page, Config.PAGESIZE)
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
                        if (searchBody.getData().getPage() != null && searchBody.getData().getPage().getList().size() != 0) {
                            result.setText("共" + searchBody.getData().getPage().getTotalCount().toString() + "个相关结果");
                            if (flag != 1) {
                                lists.clear();
                            }
                            lists.addAll(searchBody.getData().getPage().getList());
                            adapter.notifyDataSetChanged();
                            ++page;
                        }
                    }
                }));
    }

    protected void initRefreshLayout() {
        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);

        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.md_orange_700, R.color.md_red_500,
                R.color.md_indigo_900, R.color.md_green_700);
        mSwipeRefreshLayout.setSwipeableChildren(R.id.recycler_view);
        mSwipeRefreshLayout.setOnRefreshListener(this::doSwapeRefresh);
    }

    private void doSwapeRefresh() {
        loadData(0);
    }


    protected void initRecyclerView() {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        animator = new FlipInTopXAnimator();
        animator.setAddDuration(ANIMATION_DURATION);
        animator.setRemoveDuration(ANIMATION_DURATION);
        mRecyclerView.setItemAnimator(animator);
        adapter = new SearchListAdapter(lists, getActivity());
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void loadMore() {
        loadData(1);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
