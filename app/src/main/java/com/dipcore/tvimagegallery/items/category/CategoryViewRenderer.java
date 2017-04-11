package com.dipcore.tvimagegallery.items.category;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.dipcore.tvimagegallery.R;
import com.dipcore.tvimagegallery.widget.rendererrecyclerviewadapter.ViewRenderer;

public class CategoryViewRenderer extends ViewRenderer<CategoryItemModel, CategoryViewHolder> {

    @NonNull
    private final Listener mListener;

    public CategoryViewRenderer(final int type, final Context context, @NonNull final Listener listener) {
        super(type, context);
        mListener = listener;
    }

    @Override
    public void bindView(@NonNull final CategoryItemModel model, @NonNull final CategoryViewHolder holder) {
        holder.mViewAll.setFocusable(true);
        holder.mViewAll.setDuplicateParentStateEnabled(true);
        holder.mTitle.setText(model.getName());
        holder.mCount.setText(String.valueOf(model.getCount()));
        holder.mViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mListener.onItemClicked(model, v);
            }
        });
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
    public CategoryViewHolder createViewHolder(@Nullable final ViewGroup parent) {
        return new CategoryViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.category_item, parent, false));
    }

    public interface Listener {
        void onItemClicked(@NonNull CategoryItemModel model, @NonNull View view);
        void onItemFocusChange(@NonNull CategoryItemModel model, @NonNull View view, @NonNull boolean focus);
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