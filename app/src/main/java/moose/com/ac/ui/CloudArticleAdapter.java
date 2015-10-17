package moose.com.ac.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import moose.com.ac.R;
import moose.com.ac.retrofit.collect.ArticleContent;
import moose.com.ac.util.AppUtils;

/**
 * Created by dell on 2015/10/17.
 * CloudArticleAdapter
 */
public class CloudArticleAdapter extends RecyclerView.Adapter<CloudArticleVH> {
    private static final String TAG = "CloudArticleAdapter";
    private List<ArticleContent> list = new ArrayList<>();
    private Context context;

    public CloudArticleAdapter(Context context, List<ArticleContent> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public CloudArticleVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cloud_clooect, parent, false);
        CloudArticleVH vh = new CloudArticleVH(v);
        vh.title = (TextView) v.findViewById(R.id.cloud_title);
        vh.date = (TextView) v.findViewById(R.id.cloud_date);
        vh.tag = (TextView) v.findViewById(R.id.cloud_tag);
        vh.imageView = (ImageView) v.findViewById(R.id.cloud_thumb);
        vh.user = (TextView)v.findViewById(R.id.cloud_user);
        return vh;
    }

    @Override
    public void onBindViewHolder(CloudArticleVH holder, int position) {
        final ArticleContent content = list.get(position);
        Glide.with(context)
                .load(content.getTitleImg())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
        holder.title.setText(content.getTitle());
        holder.tag.setText(content.getTags());
        holder.user.setText("up主 "+content.getAuthor());
        holder.date.setText(AppUtils.formatDateByLongTime(String.valueOf(content.getReleaseDate()), context.getString(R.string.format_date)).substring(5));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
