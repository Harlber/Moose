package moose.com.ac.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Farble on 2015/8/15 17.
 */
public class ArticleListVH extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "ArticleListVH";
    public  View rootView;
    public  TextView title;
    public  TextView num;
    public  TextView user;
    public  TextView time;
    public  TextView comment;
    public ArticleItemClickListener listener;

    public ArticleListVH(View itemView,ArticleItemClickListener listener) {
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
    public interface ArticleItemClickListener {
         void onItemClick(View view,int postion);
         void onItemLongClick(View view,int postion);
    }
}
