package moose.com.ac.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.io.PrintWriter;
import java.io.StringWriter;

import moose.com.ac.common.Config;
import moose.com.ac.crash.CrashActivity;
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
 * Copyright 2015 Farble Dast
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Created by Farble on 2016/3/3 22.
 */
public abstract class BaseActivity extends RxAppCompatActivity {

    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        onInitView(savedInstanceState);
    }

    protected abstract void onInitView(Bundle savedInstanceState);
}
