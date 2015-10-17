package moose.com.ac.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by dell on 2015/10/17.
 * CloudArticleVH
 */
public class CloudArticleVH extends RecyclerView.ViewHolder {
    private static final String TAG = "CloudArticleVH";
    public View rootView;
    public ImageView imageView;/*cloud_thumb*/
    public TextView title;/*cloud_title*/
    public TextView date;/*cloud_date*/
    public TextView tag;/*cloud_tag*/
    public TextView user;/*cloud_user*/
    public CloudArticleVH(View itemView) {
        super(itemView);
        this.rootView = itemView;
    }
}
