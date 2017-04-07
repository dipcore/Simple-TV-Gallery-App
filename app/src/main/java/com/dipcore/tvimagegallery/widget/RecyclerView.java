package com.dipcore.tvimagegallery.widget;

/*
 * http://stackoverflow.com/questions/29487382/scale-up-item-in-recyclerview-to-overlaps-2-adjacent-items-android
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class RecyclerView extends android.support.v7.widget.RecyclerView {

    public RecyclerView(Context context) {
        super(context);
        setChildrenDrawingOrderEnabled(true);
    }

    public RecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setChildrenDrawingOrderEnabled(true);
    }

    public RecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setChildrenDrawingOrderEnabled(true);
        setClipToPadding(false);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //setFocusable(true);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        View view = getLayoutManager().getFocusedChild();
        if (null == view) {
            return super.getChildDrawingOrder(childCount, i);
        }
        int position = indexOfChild(view);

        if (position < 0) {
            return super.getChildDrawingOrder(childCount, i);
        }
        if (i == childCount - 1) {
            return position;
        }
        if (i == position) {
            return childCount - 1;
        }
        return super.getChildDrawingOrder(childCount, i);
    }

}