package moose.com.ac.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by dell on 2015/10/9.
 * SearchListVH
 */
public class SearchListVH extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    private static final String TAG = "SearchListVH";
    public View rootView;
    public TextView title;
    public TextView num;
    public TextView user;
    public TextView time;
    public ImageView mark;
    public ArticleListVH.ArticleItemClickListener listener;

    public SearchListVH(View itemView,  ArticleListVH.ArticleItemClickListener listener) {
        super(itemView);
        this.rootView = itemView;
        this.listener = listener;
        this.rootView.setOnClickListener(this);
        this.rootView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View view) {
        listener.onItemClick(view, getLayoutPosition());
    }

    @Override
    public boolean onLongClick(View view) {
        listener.onItemLongClick(view, getLayoutPosition());
        return true;
    }
}
