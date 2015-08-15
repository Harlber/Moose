package moose.com.ac;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import moose.com.ac.retrofit.article.Article;
import moose.com.ac.ui.ArticleListVH;

/**
 * Created by Farble on 2015/8/15 16.
 */
public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListVH> {
    private static final String TAG = "ArticleListAdapter";
    private List<Article> lists = new ArrayList<>();
    private Activity mActivity;

    public ArticleListAdapter(List<Article> lists) {
        this.lists = lists;
    }

    public ArticleListAdapter(List<Article> lists, Activity mActivity) {
        this(lists);
        this.mActivity = mActivity;
    }

    @Override
    public ArticleListVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        ArticleListVH vh = new ArticleListVH(v);
        vh.num = (TextView) v.findViewById(R.id.rank);
        vh.title = (TextView) v.findViewById(R.id.title);
        vh.user = (TextView) v.findViewById(R.id.source);
        vh.time = (TextView) v.findViewById(R.id.posted);
        vh.comment = (TextView) v.findViewById(R.id.text);
        return vh;
    }

    @Override
    public void onBindViewHolder(ArticleListVH holder, int position) {
        final Article article = lists.get(position);
        holder.num.setText(String.valueOf(position));
        holder.title.setText(article.getTitle());
        holder.user.setText("by "+article.getUser().getUsername());
        holder.time.setText(article.getReleaseDate().toString());
        holder.comment.setText(article.getViews().toString());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}
