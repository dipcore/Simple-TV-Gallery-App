package com.dipcore.tvimagegallery.items.category;

import android.view.View;
import android.widget.TextView;

import com.dipcore.tvimagegallery.R;

public class CategoryViewHolder extends com.dipcore.tvimagegallery.widget.RecyclerView.ViewHolder {

    public final TextView mTitle;
    public final TextView mCount;
    public final View mViewAll;

    public CategoryViewHolder(final View itemView) {
        super(itemView);
        mTitle = (TextView) itemView.findViewById(R.id.title);
        mCount = (TextView) itemView.findViewById(R.id.count);
        mViewAll = itemView;
    }

}
