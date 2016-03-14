package moose.com.ac.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
 * Created by dell on 2015/10/9.
 * SearchListVH
 */
public class SearchListVH extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
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
