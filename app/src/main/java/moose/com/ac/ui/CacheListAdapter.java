package moose.com.ac.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import moose.com.ac.AppApplication;
import moose.com.ac.ArticleViewActivity;
import moose.com.ac.BigNewsActivity;
import moose.com.ac.R;
import moose.com.ac.common.Config;
import moose.com.ac.data.ArticleCollects;
import moose.com.ac.data.DbHelper;
import moose.com.ac.retrofit.article.Article;
import moose.com.ac.util.AppUtils;
import moose.com.ac.util.CommonUtil;
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
 * Created by dell on 2016/2/1.
 * CacheListAdapter
 */
public class CacheListAdapter extends RecyclerView.Adapter<ArticleListVH> {

    private String TAB_NAME = Config.TABLE_NAME_STORY;
    private List<Article> lists = new ArrayList<>();
    private Activity mActivity;
    private ArticleListVH.ArticleItemClickListener listener;
    private DbHelper dbHelper;
    private ArticleListVH.ArticleItemClickListener articleItemClickListener = new ArticleListVH.ArticleItemClickListener() {

        @Override
        public void onItemClick(View view, int position) {
            Article article = lists.get(position);
            article.setSavedate(String.valueOf(System.currentTimeMillis()));
            if (!CommonUtil.isVisistor()) {
                AppApplication.getDbHelper().insertArticle(article, ArticleCollects.ArticleHistoryEntry.TABLE_NAME, article.getChannelId());
            }
            Intent intent = new Intent(mActivity, ArticleViewActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putSerializable(Config.ARTICLE, article);
            intent.putExtras(mBundle);
            mActivity.startActivity(intent);
        }

        @Override
        public void onItemLongClick(View view, int position) {
            Article article = lists.get(position);
            if (dbHelper.isExits(TAB_NAME, String.valueOf(article.getContentId()))) {
                article.setIsfav(Config.NO_ST);
                dbHelper.deleteArticle(TAB_NAME, String.valueOf(article.getContentId()));
            } else {
                article.setIsfav(Config.STORE);
                article.setSavedate(String.valueOf(System.currentTimeMillis()));
                int channel = Config.COMPLEX;
                dbHelper.insertArticle(article, TAB_NAME, channel);
            }
            notifyDataSetChanged();
        }
    };

    public CacheListAdapter(List<Article> lists) {
        this.lists = lists;
    }

    public CacheListAdapter(List<Article> lists, Activity mActivity) {
        this(lists);
        this.mActivity = mActivity;
        dbHelper = new DbHelper(mActivity);
        setListener(articleItemClickListener);
    }

    @Override
    public ArticleListVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_article_list, parent, false);
        return new ArticleListVH(v, listener);
    }

    @Override
    public void onBindViewHolder(ArticleListVH holder, int position) {
        final Article article = lists.get(position);
        holder.num.setText(String.valueOf(position + 1));
        holder.title.setText(article.getTitle());
        String TAB_HISTORY = Config.TABLE_NAME_HISTORY;
        if (dbHelper.isExits(TAB_HISTORY, String.valueOf(article.getContentId()))) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.title.setTextAppearance(R.style.textTitleGrayStyle);
            } else {
                holder.title.setTextAppearance(mActivity, R.style.textTitleGrayStyle);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.title.setTextAppearance(R.style.textTitleStyle);
            } else {
                holder.title.setTextAppearance(mActivity, R.style.textTitleStyle);
            }
        }
        holder.views.setText(String.valueOf(article.getViews()));
        holder.user.setText(String.format(mActivity.getString(R.string.ups), article.getUser().name));
        holder.time.setText(AppUtils.formatDateByLongTime(String.valueOf(article.getReleaseDate()), mActivity.getString(R.string.format_date)).substring(5));
        holder.comment.setText(MessageFormat.format(mActivity.getResources().getText(R.string.comment).toString(), article.getComments()));
        holder.comment.setOnClickListener(view -> {
            Intent intent = new Intent(mActivity, BigNewsActivity.class);
            intent.putExtra(Config.CONTENTID, article.getContentId());
            intent.putExtra(Config.TITLE, article.getTitle());
            mActivity.startActivity(intent);
        });
        holder.mark.setVisibility(dbHelper.isExits(TAB_NAME, String.valueOf(article.getContentId())) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void setListener(ArticleListVH.ArticleItemClickListener listener) {
        this.listener = listener;
    }

}
