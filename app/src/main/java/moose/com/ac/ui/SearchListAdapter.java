package moose.com.ac.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import moose.com.ac.ArticleViewActivity;
import moose.com.ac.R;
import moose.com.ac.common.Config;
import moose.com.ac.data.ArticleCollects;
import moose.com.ac.data.DbHelper;
import moose.com.ac.retrofit.article.Article;
import moose.com.ac.retrofit.search.SearchList;
import moose.com.ac.util.AppUtils;

/**
 * Created by dell on 2015/10/9.
 * SearchFragment
 */
public class SearchListAdapter extends RecyclerView.Adapter<SearchListVH> implements ArticleListVH.ArticleItemClickListener {
    private static final String TAG = "SearchListAdapter";
    private String TAB_NAME = ArticleCollects.ArticleEntry.TABLE_NAME;
    private List<SearchList> lists = new ArrayList<>();
    private Activity mActivity;
    private ArticleListVH.ArticleItemClickListener listener;
    private DbHelper dbHelper;

    public SearchListAdapter(List<SearchList> lists) {
        this.lists = lists;
    }

    public SearchListAdapter(List<SearchList> lists, Activity mActivity) {
        this.lists = lists;
        this.mActivity = mActivity;
        dbHelper = new DbHelper(mActivity);
        setListener(this);
    }

    @Override
    public SearchListVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search, parent, false);
        SearchListVH vh = new SearchListVH(v, listener);
        vh.num = (TextView) v.findViewById(R.id.srank);
        vh.title = (TextView) v.findViewById(R.id.stitle);
        vh.user = (TextView) v.findViewById(R.id.ssource);
        vh.time = (TextView) v.findViewById(R.id.sposted);
        vh.mark = (ImageView) v.findViewById(R.id.sbookmarked);
        return vh;
    }

    @Override
    public void onBindViewHolder(SearchListVH holder, int position) {
        final SearchList searchList = lists.get(position);
        holder.num.setText(R.string.total);
        holder.title.setText(searchList.getTitle());
        holder.user.setText("UP主:"+searchList.getUsername()+"       "+
                AppUtils.formatDateByLongTime(String.valueOf(searchList.getReleaseDate()), mActivity.getString(R.string.format_date)).substring(5,16));
        holder.mark.setVisibility(dbHelper.isExits(TAB_NAME, String.valueOf(searchList.getContentId())) ? View.VISIBLE : View.INVISIBLE);
        holder.time.setText("围观:" + searchList.getViews() + "       评论:" + searchList.getComments());
        holder.rootView.setOnClickListener(v -> {
            Article article = new Article();
            article.setContentId(Integer.valueOf(searchList.getContentId().replace("ac","")));
            Intent intent = new Intent(mActivity,ArticleViewActivity.class);
            intent.putExtra(Config.ARTICLE,article);
            mActivity.startActivity(intent);
            mActivity.finish();
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public ArticleListVH.ArticleItemClickListener getListener() {
        return listener;
    }

    public void setListener(ArticleListVH.ArticleItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(View view, int postion) {
        /*Article article = lists.get(postion);
        article.setSavedate(String.valueOf(System.currentTimeMillis()));
        if (!App.isVistor()) {
            App.getDbHelper().insertArticle(article, ArticleCollects.ArticleHistoryEntry.TABLE_NAME, article.getChannelId());
        }
        Intent intent = new Intent(mActivity, ArticleViewActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(Config.ARTICLE, article);
        intent.putExtras(mBundle);
        new Handler().postDelayed(() -> mActivity.startActivity(intent), 50);//make sure db insert done before intent*/
    }

    @Override
    public void onItemLongClick(View view, int postion) {
       /* Article article = lists.get(postion);
        if (dbHelper.isExits(TAB_NAME, String.valueOf(article.getContentId()))) {//exits
            article.setIsfav(Config.NO_ST);//set not fav
            dbHelper.deleteArticle(TAB_NAME, String.valueOf(article.getContentId()));//remove from db
        } else {
            article.setIsfav(Config.STORE);//set not fav
            article.setSavedate(String.valueOf(System.currentTimeMillis()));//set save date
            dbHelper.insertArticle(article, TAB_NAME, channnel);//remove from db
        }
        notifyDataSetChanged();*/
    }
}
