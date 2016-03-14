package moose.com.ac.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import moose.com.ac.R;
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
/**
 * Created by Farble on 2015/8/15 17.
 * ArticleListVH
 */
public class ArticleListVH extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    public View rootView;
    public TextView title;
    public TextView num;
    public TextView user;
    public TextView time;
    public TextView comment;
    public TextView views;
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
        this.views = (TextView) v.findViewById(R.id.view_number);
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

    public interface ArticleItemClickListener {
        void onItemClick(View view, int postion);

        void onItemLongClick(View view, int postion);
    }
}
