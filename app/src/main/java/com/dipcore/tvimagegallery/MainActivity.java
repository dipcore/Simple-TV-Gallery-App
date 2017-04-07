package com.dipcore.tvimagegallery;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.dipcore.tvimagegallery.decorator.EqualSpacesItemDecoration;

import com.dipcore.tvimagegallery.factory.MediaStoreFactory;
import com.dipcore.tvimagegallery.items.category.CategoryItemModel;
import com.dipcore.tvimagegallery.items.category.CategoryViewRenderer;

import com.dipcore.tvimagegallery.items.image.ImageItemModel;
import com.dipcore.tvimagegallery.items.image.ImageViewRenderer;
import com.dipcore.tvimagegallery.widget.RecyclerView;
import com.dipcore.tvimagegallery.widget.rendererrecyclerviewadapter.ItemModel;
import com.dipcore.tvimagegallery.widget.rendererrecyclerviewadapter.RendererRecyclerViewAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int COLS = 4;

    private boolean headerVisible = true;
    private View headerView;
    private View headerContentView;

    private RendererRecyclerViewAdapter mRendererRecyclerViewAdapter;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private MediaStoreFactory mMediaStoreFactory;

    private int mRecyclerViewScrollPositionY;
    private String categoryName;
    private boolean focused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        headerView = findViewById(R.id.header);
        headerContentView = findViewById(R.id.headerContent);

        mRendererRecyclerViewAdapter = new RendererRecyclerViewAdapter();
        mRendererRecyclerViewAdapter.registerRenderer(new CategoryViewRenderer(CategoryItemModel.TYPE, this, mCategoryListener));
        mRendererRecyclerViewAdapter.registerRenderer(new ImageViewRenderer(ImageItemModel.TYPE, this, mImageListener));


        mGridLayoutManager = new GridLayoutManager(this, COLS);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {
            @Override
            public
            int getSpanSize(final int position) {
               return 1;
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mRendererRecyclerViewAdapter);
        mRecyclerView.addItemDecoration(new EqualSpacesItemDecoration(20));
        mRecyclerView.addOnLayoutChangeListener(mRecyclerViewLayoutChangeListener);
        mRecyclerView.addOnScrollListener(mRecyclerViewOnScrollListener);
        mRecyclerViewScrollPositionY = mRecyclerView.getPaddingTop();

        mMediaStoreFactory = new MediaStoreFactory(this);
        mMediaStoreFactory.scan();

        renderCategoryItems();
    }

    @Override
    public void onBackPressed() {
        if (categoryName == null) {
            super.onBackPressed();
        } else {
            renderCategoryItems();
        }
    }

    private void renderCategoryItems() {
        categoryName = null;
        focused = false;
        mRecyclerViewScrollPositionY = mRecyclerView.getPaddingTop();

        ArrayList<ItemModel> list = new ArrayList<>();
        list.addAll(mMediaStoreFactory.getCategoryList());
        mRendererRecyclerViewAdapter.setItems(list);
        mRendererRecyclerViewAdapter.notifyDataSetChanged();

        String title = getResources().getString(R.string.categoryListTitle);
        String subTitle = getResources().getString(R.string.categoryListSubTitle) + " " + mMediaStoreFactory.getTotalImagesCount();
        setHeader(title, subTitle);

        headerDropIn();
    }

    private void renderImageItems(String name) {
        categoryName = name;
        focused = false;
        mRecyclerViewScrollPositionY = mRecyclerView.getPaddingTop();

        ArrayList<ItemModel> imageList = mMediaStoreFactory.getImageList(name);
        ArrayList<ItemModel> list = new ArrayList<>();
        //list.add(new CategoryItemModel("Back"));
        list.addAll(imageList);

        mRendererRecyclerViewAdapter.setItems(list);
        mRendererRecyclerViewAdapter.notifyDataSetChanged();

        String title = name;
        String subTitle = getResources().getString(R.string.imageListSubTitle) + " " + imageList.size();
        setHeader(title, subTitle);

        headerDropIn();
    }

    private void setHeader(String title, String subtitle) {
        TextView titleView = (TextView) headerView.findViewById(R.id.title);
        TextView subtitleView = (TextView) headerView.findViewById(R.id.subtitle);
        titleView.setText(title);
        subtitleView.setText(subtitle);
    }


    @NonNull
    private final CategoryViewRenderer.Listener mCategoryListener = new CategoryViewRenderer.Listener() {
        @Override
        public void onItemClicked(@NonNull final CategoryItemModel model, @NonNull View view) {
            String name = model.getName();
            if  (mMediaStoreFactory.isCategoryExists(name)) {
                renderImageItems(name);
            } else {
                renderCategoryItems();
            }
        }

        @Override
        public void onItemFocusChange(@NonNull CategoryItemModel model, @NonNull View view, @NonNull boolean focus) {
            if (focus) {
                // http://stackoverflow.com/questions/29487382/scale-up-item-in-recyclerview-to-overlaps-2-adjacent-items-android
                mRecyclerView.invalidate();
            }
        }
    };

    @NonNull
    private final ImageViewRenderer.Listener mImageListener = new ImageViewRenderer.Listener() {
        @Override
        public void onItemClicked(@NonNull final ImageItemModel model, @NonNull View view) {

            ArrayList<ItemModel> imageList = mMediaStoreFactory.getImageList(categoryName);
            int imageIndex = mRecyclerView.getChildAdapterPosition(view);

            Intent intent = new Intent(getApplicationContext(), ImageViewerActivity.class);
            intent.putExtra("imageList", imageList);
            intent.putExtra("imageIndex", imageIndex);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }

        @Override
        public void onItemFocusChange(@NonNull ImageItemModel model, @NonNull View view, @NonNull boolean focus) {
            if (focus) {
                // http://stackoverflow.com/questions/29487382/scale-up-item-in-recyclerview-to-overlaps-2-adjacent-items-android
                mRecyclerView.invalidate();
            }
        }
    };

    @NonNull
    private final android.support.v7.widget.RecyclerView.OnScrollListener mRecyclerViewOnScrollListener = new android.support.v7.widget.RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(android.support.v7.widget.RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(android.support.v7.widget.RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int headerHeight = headerView.getMeasuredHeight();
            int headerContentHeight = headerContentView.getMeasuredHeight();
            int delta = (headerHeight - headerContentHeight)/2;

            mRecyclerViewScrollPositionY += dy;

            if (mRecyclerViewScrollPositionY - delta <= headerHeight && !headerVisible) {
                headerAnimIn();
            }

            if (mRecyclerViewScrollPositionY - delta > headerHeight && headerVisible) {
                headerAnimOut();
            }

        }
    };

    @NonNull
    private final View.OnLayoutChangeListener mRecyclerViewLayoutChangeListener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
            // Request focus after render ended
            if (!focused) {
                mRecyclerView.requestFocus();
                focused = true;
            }
        }
    };

    private void headerDropIn() {
        System.out.println("headerDropIn");
        headerVisible = true;
        ///int headerHeight = headerView.getMeasuredHeight();
        headerView.setTranslationY(0);
        ///ViewCompat.animate(headerView).translationY(0).setDuration(200).setInterpolator(new DecelerateInterpolator()).start();
    }

    private void headerAnimOut(){
        System.out.println("headerAnimOut");
        headerVisible = false;
        int headerHeight = headerView.getMeasuredHeight();
        ViewCompat.animate(headerView).translationY(-headerHeight).setDuration(200).setInterpolator(new DecelerateInterpolator()).start();
    }

    private void headerAnimIn(){
        System.out.println("headerAnimIn");
        headerVisible = true;
        ViewCompat.animate(headerView).translationY(0).setDuration(200).setInterpolator(new DecelerateInterpolator()).start();
    }

}
