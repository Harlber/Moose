package moose.com.ac.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import moose.com.ac.R;

/**
 * Created by Farble on 2015/8/15 17.
 * ArticleListVH
 */
public class ArticleListVH extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "ArticleListVH";
    public View rootView;
    public TextView title;
    public TextView num;
    public TextView user;
    public TextView time;
    public TextView comment;
    public ImageView mark;
    public RelativeLayout root;
    public ArticleItemClickListener listener;

    public ArticleListVH(View v, ArticleItemClickListener listener) {
        super(v);
        this.rootView = v;
        this.listener = listener;
        this.num = (TextView) v.findViewById(R.id.rank);
        this.title = (TextView) v.findViewById(R.id.title);
        this.user = (TextView) v.findViewById(R.id.source);
        this.time = (TextView) v.findViewById(R.id.posted);
        this.comment = (TextView) v.findViewById(R.id.text);
        this.mark = (ImageView) v.findViewById(R.id.bookmarked);
        this.root = (RelativeLayout) v.findViewById(R.id.rl_root);
        this.root.setOnClickListener(this);
        this.root.setOnLongClickListener(this);
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

    public static interface ArticleItemClickListener {
        void onItemClick(View view, int postion);

        void onItemLongClick(View view, int postion);
    }
}
