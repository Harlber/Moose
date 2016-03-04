package moose.com.ac.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import moose.com.ac.R;
import moose.com.ac.retrofit.comment.CommentDetail;
import moose.com.ac.ui.widget.FloorsView;
import moose.com.ac.util.CommonUtil;
import moose.com.ac.util.DisplayUtil;
import moose.com.ac.util.SparseArrayCompatSerializable;
import moose.com.ac.util.TextViewUtils;

/**
 * Created by dell on 2015/8/20.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private static final String TAG = "CommentAdapter";

    protected LayoutInflater mInflater;
    private SparseArrayCompatSerializable<CommentDetail> data;
    private List<Integer> commentIdList;
    private Context mContext;
    private int maxNumOfFloor;
    private int frameId = R.id.floor;//?

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.comments_listitem, parent, false);
        CommentViewHolder holder = new CommentViewHolder(v);
        holder.user = (TextView) v.findViewById(R.id.user_name);
        holder.content = (TextView) v.findViewById(R.id.comments_content);
        holder.quoteImage = v.findViewById(R.id.quote_img);
        holder.ll_quote = (RelativeLayout) v.findViewById(R.id.ll_quote);
        return holder;
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        CommentDetail c = data.get(commentIdList.get(position));
        holder.user.setText(String.format("#%d %s", c.getCount(), c.getUserName()));
        TextViewUtils.setCommentContent(holder.content, c);
        int quoteId = Integer.valueOf(c.getQuoteId() + "");
        holder.hasQuote = quoteId > 0;
        List<View> quoteList = new ArrayList<>();
        handleQuoteList(position, holder.rootView, holder, quoteId, quoteList);
        holder.quoteFrame.setQuoteList(quoteList);
        //修复引用楼层重叠问题.由于item会复用,所以每次设置内容前,清空ll_quote里的view.
        holder.ll_quote.removeAllViews();
        if (!quoteList.isEmpty()) {
            RelativeLayout.LayoutParams floorsLayoutParams = new RelativeLayout.LayoutParams(-1, -2);
            int margin = DisplayUtil.dip2px(mContext, 4);
            floorsLayoutParams.setMargins(margin, 0, margin, margin);
            //floorsLayoutParams.addRule(RelativeLayout.BELOW, R.id.requote);
            holder.ll_quote.addView(holder.quoteFrame, floorsLayoutParams);
        }
//        RelativeLayout.LayoutParams userLayoutParams = (RelativeLayout.LayoutParams) holder.user.getLayoutParams();
//        userLayoutParams.addRule(RelativeLayout.BELOW, holder.quoteFrame.getChildCount() > 0 ? frameId : R.id.requote);
//        holder.user.setLayoutParams(userLayoutParams);
//        handlePadding(position, holder.rootView);
    }

    @Override
    public int getItemCount() {
        if (commentIdList == null) return 0;
        return commentIdList.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        private View rootView;
        public TextView user;
        public TextView content;
        public View quoteImage;
        public boolean hasQuote;
        public FloorsView quoteFrame;
        public RelativeLayout ll_quote;

        public CommentViewHolder(View itemView) {
            super(itemView);
            this.rootView = itemView;
        }
    }

    public CommentAdapter(Context context, SparseArrayCompatSerializable<CommentDetail> data, List<Integer> commentIdList) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.data = data;
        this.commentIdList = commentIdList;
        maxNumOfFloor = CommonUtil.getMaxLine();
        if (maxNumOfFloor == 0)
            maxNumOfFloor = 10;
    }

    public void setData(SparseArrayCompatSerializable<CommentDetail> data, List<Integer> commentIdList) {
        this.data = data;
        this.commentIdList = commentIdList;
    }

    private RelativeLayout generateQuoteFrame(CommentDetail quote) {
        RelativeLayout quoteFrame = (RelativeLayout) mInflater.inflate(R.layout.comments_quote_item, null);
        TextView username = (TextView) quoteFrame.findViewById(R.id.user_name);
        username.setText(String.format("#%d %s", quote.getCount(), quote.getUserName()));
        TextView content = (TextView) quoteFrame.findViewById(R.id.comments_content);
        TextViewUtils.setCommentContent(content, quote);

        return quoteFrame;
    }

    private void handleQuoteList(int position, View convertView, CommentViewHolder holder, int quoteId,
                                 List<View> quoteList) {
        if (holder.hasQuote || holder.quoteFrame == null) {
            FloorsView floors = new FloorsView(mContext);
            floors.setId(frameId);
            holder.quoteFrame = floors;
        }

        int num = 0;
        for (CommentDetail quote = data.get(quoteId); quote != null && num < maxNumOfFloor;
             num++, quoteId = Integer.valueOf(quote.getQuoteId() + ""), quote = data.get(quoteId)) {

            if (quote.isQuoted) {
                if (quote.beQuotedPosition == position) {
                    quoteList.add(generateQuoteFrame(quote));
                } else {
                    //convertView.findViewById(R.id.requote).setVisibility(View.VISIBLE);
                }
            } else {
                quote.isQuoted = true;
                quote.beQuotedPosition = position;
                quoteList.add(generateQuoteFrame(quote));
            }
        }
    }

    private void handlePadding(int position, View convertView) {
        int padding = DisplayUtil.dip2px(mContext, 8);
        if (position == 0) {
            int paddingTop = mInflater.getContext().getResources()
                    .getDimensionPixelSize(R.dimen.abc_action_bar_default_height);
            convertView.setPadding(padding, paddingTop + padding, padding, padding * 2);
        } else
            convertView.setPadding(padding, padding * 2, padding, padding * 2);
    }
}
