/*
 * Copyright (C) 2014 The Android Open Source Project
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
package recycleview.src.main.java.com.twotoasters.android.support.v7.widget;

import android.content.Context;

/**
 * Interface for platform specific CardView implementations.
 */
interface CardViewImpl {
    void initialize(CardViewDelegate cardView, Context context, int backgroundColor, float radius);

    void setRadius(CardViewDelegate cardView, float radius);

    float getRadius(CardViewDelegate cardView);

    void initStatic();
}
