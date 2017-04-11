package com.dipcore.tvimagegallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dipcore.tvimagegallery.items.image.ImageItemModel;
import com.dipcore.tvimagegallery.widget.rendererrecyclerviewadapter.ItemModel;

import java.util.ArrayList;

public class ImageViewerActivity extends AppCompatActivity {

    private ImageSwitcher imageSwitcher;

    ArrayList<ImageItemModel> imageList = new ArrayList<>();
    int imageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        // Image list
        Intent intent = getIntent();
        imageList = (ArrayList<ImageItemModel>)intent.getSerializableExtra("imageList");
        imageIndex = (int)intent.getSerializableExtra("imageIndex");

        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {

                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                ViewGroup.LayoutParams params = new ImageSwitcher.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                imageView.setLayoutParams(params);
                return imageView;
            }
        });

        imageSwitcher.setImageURI(imageList.get(imageIndex).getUri());
    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT: {
                showPrevImage();
                break;
            }
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                showNextImage();
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    /////

    private void showCurrentImage(){
        ImageView imageView = (ImageView) imageSwitcher.getNextView();
        Glide.with(getApplicationContext())
                .fromMediaStore()
                .load(imageList.get(imageIndex).getUri())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate()
                .into(imageView);

    }

    private void showNextImage(){
        imageIndex++;
        if (imageIndex > imageList.size() - 1) {
            imageIndex = 0;
        }

        Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);

        imageSwitcher.setOutAnimation(animationOut);
        imageSwitcher.setInAnimation(animationIn);


        showCurrentImage();
        imageSwitcher.showNext();
    }

    private void showPrevImage(){
        imageIndex--;
        if (imageIndex < 0) {
            imageIndex = imageList.size() - 1;
        }

        Animation animationOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
        Animation animationIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);

        imageSwitcher.setOutAnimation(animationOut);
        imageSwitcher.setInAnimation(animationIn);

        showCurrentImage();
        imageSwitcher.showPrevious();
    }



}
