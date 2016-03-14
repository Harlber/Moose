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
 * Created by dell on 2015/10/17.
 * CloudArticleVH
 */
public class CloudArticleVH extends RecyclerView.ViewHolder {
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
