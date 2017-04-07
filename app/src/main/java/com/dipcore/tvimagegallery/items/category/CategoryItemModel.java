package com.dipcore.tvimagegallery.items.category;

import android.support.annotation.NonNull;

import com.dipcore.tvimagegallery.widget.rendererrecyclerviewadapter.ItemModel;

import java.io.Serializable;

public class CategoryItemModel implements ItemModel {

    public static final int TYPE = 0;
    @NonNull
    private String mName;
    private int mCount;

    public CategoryItemModel(@NonNull final String name) {
        mName = name;
        mCount = 0;
    }

    public CategoryItemModel(@NonNull final String name, int count) {
        mName = name;
        mCount = count;
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @NonNull
    public String getName() {
        return mName;
    }

    public void setName(@NonNull String mName) {
        this.mName = mName;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int mCount) {
        this.mCount = mCount;
    }




}
