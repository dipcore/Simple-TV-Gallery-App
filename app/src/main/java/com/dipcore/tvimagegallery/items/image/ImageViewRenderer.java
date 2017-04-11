package com.dipcore.tvimagegallery.items.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dipcore.tvimagegallery.R;
import com.dipcore.tvimagegallery.widget.rendererrecyclerviewadapter.ViewRenderer;


public class ImageViewRenderer extends ViewRenderer<ImageItemModel, ImageViewHolder> {

    @NonNull
    private final Listener mListener;

    public ImageViewRenderer(final int type, final Context context, @NonNull final Listener listener) {
        super(type, context);
        mListener = listener;
    }

    @Override
    public void bindView(@NonNull final ImageItemModel model, @NonNull final ImageViewHolder holder) {
        holder.mViewAll.setFocusable(true);
        holder.mViewAll.setDuplicateParentStateEnabled(true);

        // Set title
        holder.mTitle.setText(model.getName());

        // Set size
        holder.mSizeTextView.setText(String.format("%dx%d (%s)",
                model.getWidth(), model.getHeight(),
                model.getSizeHumanReadable()
        ));


        Glide.with(getContext())
                .fromMediaStore()
                .load(model.getUri())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.mImageView);

        // On click
        holder.mViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mListener.onItemClicked(model, v);
            }
        });

        // On focus
        holder.mViewAll.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                mListener.onItemFocusChange(model, view, b);
                if (b) {
                    animIn(view);
                } else {
                    animOut(view);
                }
            }
        });
    }

    @NonNull
    @Override
    public ImageViewHolder createViewHolder(@Nullable final ViewGroup parent) {
        return new ImageViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.image_item, parent, false));
    }

    public interface Listener {
        void onItemClicked(@NonNull ImageItemModel model, View view);
        void onItemFocusChange(@NonNull ImageItemModel model, View view, @NonNull boolean focus);
    }

    private void animOut(View view) {
        final float n = 1.0f;
        ViewCompat.animate(view).scaleX(n).scaleY(n).setDuration(150).setInterpolator(new DecelerateInterpolator()).start();
        ViewCompat.animate(view.findViewById(R.id.overlay)).alpha(1f).setDuration(150).setInterpolator(new DecelerateInterpolator()).start();
    }

    private void animIn(View view) {
        final float n = 1.2f;
        ViewCompat.animate(view).scaleX(n).scaleY(n).setDuration(150).setInterpolator(new DecelerateInterpolator()).start();
        ViewCompat.animate(view.findViewById(R.id.overlay)).alpha(0f).setDuration(150).setInterpolator(new DecelerateInterpolator()).start();
    }
}