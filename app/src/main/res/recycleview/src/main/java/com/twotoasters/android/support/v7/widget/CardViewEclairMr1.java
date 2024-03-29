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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

class CardViewEclairMr1 implements CardViewImpl {

    final RectF sCornerRect = new RectF();

    @Override
    public void initStatic() {
        // Draws a round rect using 7 draw operations. This is faster than using
        // canvas.drawRoundRect before JBMR1 because API 11-16 used alpha mask textures to draw
        // shapes.
        RoundRectDrawableWithShadow.sRoundRectHelper
                = new RoundRectDrawableWithShadow.RoundRectHelper() {
            @Override
            public void drawRoundRect(Canvas canvas, RectF bounds, float cornerRadius,
                    Paint paint) {
                final float twoRadius = cornerRadius * 2;
                final float innerWidth = bounds.width() - twoRadius;
                final float innerHeight = bounds.height() - twoRadius;
                sCornerRect.set(bounds.left, bounds.top,
                        bounds.left + cornerRadius * 2, bounds.top + cornerRadius * 2);

                canvas.drawArc(sCornerRect, 180, 90, true, paint);
                sCornerRect.offset(innerWidth, 0);
                canvas.drawArc(sCornerRect, 270, 90, true, paint);
                sCornerRect.offset(0, innerHeight);
                canvas.drawArc(sCornerRect, 0, 90, true, paint);
                sCornerRect.offset(-innerWidth, 0);
                canvas.drawArc(sCornerRect, 90, 90, true, paint);

                //draw top and bottom pieces
                canvas.drawRect(bounds.left + cornerRadius, bounds.top,
                        bounds.right - cornerRadius, bounds.top + cornerRadius,
                        paint);
                canvas.drawRect(bounds.left + cornerRadius,
                        bounds.bottom - cornerRadius, bounds.right - cornerRadius,
                        bounds.bottom, paint);

                //center
                canvas.drawRect(bounds.left, bounds.top + cornerRadius,
                        bounds.right, bounds.bottom - cornerRadius, paint);
            }
        };
    }

    @Override
    public void initialize(CardViewDelegate cardView, Context context, int backgroundColor,
            float radius) {
        RoundRectDrawableWithShadow background = new RoundRectDrawableWithShadow(
                context.getResources(), backgroundColor, radius);
        cardView.setBackgroundDrawable(background);
    }

    @Override
    public void setRadius(CardViewDelegate cardView, float radius) {
        ((RoundRectDrawableWithShadow) cardView.getBackground()).setCornerRadius(radius);
    }

    @Override
    public float getRadius(CardViewDelegate cardView) {
        return ((RoundRectDrawableWithShadow) cardView.getBackground()).getCornerRadius();
    }
}