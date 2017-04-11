package com.dipcore.tvimagegallery.factory;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import com.dipcore.tvimagegallery.items.category.CategoryItemModel;
import com.dipcore.tvimagegallery.items.image.ImageItemModel;
import com.dipcore.tvimagegallery.widget.rendererrecyclerviewadapter.ItemModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MediaStoreFactory {

    int totalImages;
    Map<String, ArrayList<ItemModel>> categoryMap;
    private Context context;

    public MediaStoreFactory(Context context) {
        this.context = context;
        categoryMap = new HashMap<>();
        //categoryMap = new TreeMap(Collections.reverseOrder());
    }

    public void scan() {

        totalImages = 0;

        Uri baseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = new String[] {
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.DATE_MODIFIED,
                MediaStore.Images.ImageColumns.ORIENTATION,
                MediaStore.Images.ImageColumns.MIME_TYPE,
                MediaStore.Images.ImageColumns.HEIGHT,
                MediaStore.Images.ImageColumns.WIDTH,
                MediaStore.Images.ImageColumns.SIZE
        };

        String order = MediaStore.Images.ImageColumns.DATE_MODIFIED + " DESC";

        Cursor cur = context.getContentResolver().query(baseUri,
                projection, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                order);     // Ordering

        if (cur.moveToFirst()) {
            long id;
            String name;
            String bucket;
            long dateTaken;
            long dateModified;
            int orientation;
            String mimeType;
            String data;
            int height;
            int width;
            long size;
            long thumbnailId;


            do {
                // Get the field values
                id = cur.getLong(cur.getColumnIndex(MediaStore.Images.Media._ID));
                data = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA));
                name = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                bucket = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                dateTaken = cur.getLong(cur.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
                dateModified = cur.getLong(cur.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
                orientation = cur.getInt(cur.getColumnIndex(MediaStore.Images.Media.ORIENTATION));
                mimeType = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
                height = cur.getInt(cur.getColumnIndex(MediaStore.Images.Media.HEIGHT));
                width = cur.getInt(cur.getColumnIndex(MediaStore.Images.Media.WIDTH));
                size = cur.getLong(cur.getColumnIndex(MediaStore.Images.Media.SIZE));

                // Add imageItem
                ArrayList<ItemModel> imageList = categoryMap.get(bucket);
                if (imageList == null) {
                    imageList = new ArrayList<>();
                    categoryMap.put(bucket, imageList);
                }
                imageList.add(new ImageItemModel(data, name, bucket, id, dateTaken, dateModified, mimeType, orientation, height, width, size));
                totalImages ++;

            } while (cur.moveToNext());
        }
    }

    public ArrayList<ItemModel> getCategoryList() {
        ArrayList<ItemModel> categoryList = new ArrayList<>();
        for (String key : categoryMap.keySet()){
            categoryList.add(new CategoryItemModel(key, getImageList(key).size()));
        }
        return categoryList;
    }

    public ArrayList<ItemModel> getImageList(String name) {
        return categoryMap.get(name);
    }

    public int getTotalImagesCount(){
        return totalImages;
    }

     public boolean isCategoryExists(String name) {
         return getImageList(name) != null;
    }

}
