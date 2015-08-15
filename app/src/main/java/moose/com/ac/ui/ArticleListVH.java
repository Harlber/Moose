package moose.com.ac.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Farble on 2015/8/15 17.
 */
public class ArticleListVH extends RecyclerView.ViewHolder {
    private static final String TAG = "ArticleListVH";
    public  View rootView;
    public  TextView title;
    public  TextView num;
    public  TextView user;
    public  TextView time;
    public  TextView comment;

    public ArticleListVH(View itemView) {
        super(itemView);
        this.rootView = itemView;
    }
}
