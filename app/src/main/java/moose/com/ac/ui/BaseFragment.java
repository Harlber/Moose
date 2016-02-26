package moose.com.ac.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.trello.rxlifecycle.components.support.RxFragment;
/*
 * Copyright Farble Dast. All rights reserved.
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
 * Created by Farble on 2016/2/25 22.
 */
public abstract class BaseFragment extends RxFragment{
    protected boolean isAttach = false;
    protected boolean isViewCreated = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        isAttach = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onReceiveData();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        onInitView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        onInitData();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public abstract void onReceiveData();

    public abstract void onInitView();

    public abstract void onInitData();

    public View getRootView(){
        if (!isAttach) {
            throw new NullPointerException("Fragment is not attached to its context!");
        }
        if (!isViewCreated) {
            throw new NullPointerException("#onInitView() must be called after #onViewCreated(View view, Bundle savedInstanceState)!");
        }
        return getView();
    }
}
