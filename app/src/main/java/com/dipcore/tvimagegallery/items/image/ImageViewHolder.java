package com.dipcore.tvimagegallery.items.image;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dipcore.tvimagegallery.R;

public class ImageViewHolder extends com.dipcore.tvimagegallery.widget.RecyclerView.ViewHolder {

    public final TextView mTitle;
    public final View mViewAll;
    public final ImageView mImageView;

    public ImageViewHolder(final View itemView) {
        super(itemView);
        mTitle = (TextView) itemView.findViewById(R.id.title);
        mViewAll = itemView;
        mImageView = (ImageView) itemView.findViewById(R.id.imageView);
    }

}
