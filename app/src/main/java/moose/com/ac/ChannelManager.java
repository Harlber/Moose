package moose.com.ac;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
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
public interface ChannelManager {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            HOT,
            COMPLEX,
            WORK_EMOTION,
            ANIMATION_CULTURE,
            COMIC_FICTION,
            GAME
    })
    @interface ChannelMode {
    }

    int HOT = 63;
    int COMPLEX = 110;
    int WORK_EMOTION = 73;
    int ANIMATION_CULTURE = 74;
    int COMIC_FICTION = 75;
    int GAME = 164;

}
