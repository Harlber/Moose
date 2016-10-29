package moose.com.ac.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import moose.com.ac.R;
import moose.com.ac.retrofit.comment.CommentListWrapper;
import moose.com.ac.ui.widget.FloorsView;
import moose.com.ac.util.CommonUtil;
import moose.com.ac.util.DisplayUtil;
import moose.com.ac.util.TextViewUtils;

/*
 * Copyright 2015,2016 Farble Dast
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    protected LayoutInflater mInflater;
    private SparseArrayCompat<CommentListWrapper.Comment> data;
    private List<Integer> commentIdList;
    private Context mContext;
    private int maxNumOfFloor;
    private OnItemClickListener onItemClickListener;

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.comments_listitem, parent, false);
        CommentViewHolder holder = new CommentViewHolder(v);
        holder.user = (TextView) v.findViewById(R.id.user_name);
        holder.content = (TextView) v.findViewById(R.id.comments_content);
        holder.quoteImage = v.findViewById(R.id.quote_img);
        holder.ll_quote = (RelativeLayout) v.findViewById(R.id.ll_quote);
        holder.commentLayout = (RelativeLayout) v.findViewById(R.id.comment_layout);
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        CommentListWrapper.Comment c = data.get(commentIdList.get(position));
        holder.user.setText(String.format("#%d %s", c.floor, c.username));
        TextViewUtils.setCommentContent(holder.content, c);
        int quoteId = c.quoteId;
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
            holder.ll_quote.addView(holder.quoteFrame, floorsLayoutParams);
        }
        if (onItemClickListener != null) {
            holder.commentLayout.setOnClickListener(v -> onItemClickListener.onItemClick(null, v, holder.getAdapterPosition(), holder.getItemId()));
        }
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
        public RelativeLayout commentLayout;

        public CommentViewHolder(View itemView) {
            super(itemView);
            this.rootView = itemView;
        }
    }

    public CommentAdapter(Context context, SparseArrayCompat<CommentListWrapper.Comment> data, List<Integer> commentIdList) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.data = data;
        this.commentIdList = commentIdList;
        maxNumOfFloor = CommonUtil.getMaxLine();
        if (maxNumOfFloor == 0)
            maxNumOfFloor = 10;
    }

    public void setData(SparseArrayCompat<CommentListWrapper.Comment> data, List<Integer> commentIdList) {
        this.data = data;
        this.commentIdList = commentIdList;
    }

    private RelativeLayout generateQuoteFrame(CommentListWrapper.Comment quote) {
        RelativeLayout quoteFrame = (RelativeLayout) mInflater.inflate(R.layout.comments_quote_item, null);
        TextView username = (TextView) quoteFrame.findViewById(R.id.user_name);
        username.setText(String.format(Locale.getDefault(), "#%d %s", quote.floor, quote.username));
        TextView content = (TextView) quoteFrame.findViewById(R.id.comments_content);
        TextViewUtils.setCommentContent(content, quote);
        if (onItemClickListener != null) {
            quoteFrame.setOnClickListener(v -> onItemClickListener.onItemClick(null, v, quote));
        }
        return quoteFrame;
    }

    private void handleQuoteList(int position, View convertView, CommentViewHolder holder, int quoteId,
                                 List<View> quoteList) {
        if (holder.hasQuote || holder.quoteFrame == null) {
            FloorsView floors = new FloorsView(mContext);
            int frameId = R.id.floor;
            floors.setId(frameId);
            holder.quoteFrame = floors;
        }

        int num = 0;
        for (CommentListWrapper.Comment quote = data.get(quoteId); quote != null && num < maxNumOfFloor;
             num++, quoteId = quote.quoteId, quote = data.get(quoteId)) {

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

    interface OnItemClickListener {

        void onItemClick(AdapterView<?> parent, View view, int position, long id);

        void onItemClick(AdapterView<?> parent, View view, CommentListWrapper.Comment comment);
    }
}
