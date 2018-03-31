/**
 * © Robert Williamson 2014-2017.
 * This program is distributed under the terms of the GNU General Public License.
 */
package com.robwilliamson.healthyesther.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class MultiRangePicker extends android.support.v7.widget.LinearLayoutCompat {
    private static final Rect BASE_SIZE = new Rect(0, 0, 400, 40);

    private AttributeSet mAttrs;

    public MultiRangePicker(Context context) {
        super(context);
    }

    public MultiRangePicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mAttrs = attrs;
    }

    public MultiRangePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mAttrs = attrs;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getOrientation() == VERTICAL) {
            verticalMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            horizontalMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void horizontalMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    private void verticalMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }
/*
    @Override
    protected int getSuggestedMinimumHeight() {
        int minimumHeight = Math.max(super.getSuggestedMinimumHeight(), mSize.height());

        for(int i = 0; i < this.getChildCount(); i++) {
            final View child = this.getChildAt(i);

            if (minimumHeight < child.getMinimumHeight()) {
                minimumHeight = child.getMinimumHeight();
            }
        }

        return minimumHeight;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        int minimumWidth = Math.max(super.getSuggestedMinimumWidth(), mSize.width());

        for(int i = 0; i < this.getChildCount(); i++) {
            final View child = this.getChildAt(i);

            if (minimumWidth < child.getMinimumWidth()) {
                minimumWidth = child.getMinimumWidth();
            }
        }

        return minimumWidth;
    }*/
}
