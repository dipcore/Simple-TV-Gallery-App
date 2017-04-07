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
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.DATE_MODIFIED,
                MediaStore.Images.ImageColumns.ORIENTATION,
                MediaStore.Images.ImageColumns.MIME_TYPE
        };

        String order = MediaStore.Images.ImageColumns.DATE_MODIFIED + " DESC";

        Cursor cur = context.getContentResolver().query(baseUri,
                projection, // Which columns to return
                null,       // Which rows to return (all rows)
                null,       // Selection arguments (none)
                order);     // Ordering

        if (cur.moveToFirst()) {
            long id;
            String bucket;
            long dateTaken;
            long dateModified;
            int orientation;
            String mimeType;
            String data;

            int idColumn = cur.getColumnIndex(
                    MediaStore.Images.Media._ID);

            int bucketColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            int dateTakenColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATE_TAKEN);

            int dateModifiedColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATE_MODIFIED);

            int orientationColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.ORIENTATION);

            int mimeTypeColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.MIME_TYPE);

            int dataColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATA);

            do {
                // Get the field values
                id = cur.getLong(idColumn);
                bucket = cur.getString(bucketColumn);
                dateTaken = cur.getLong(dateTakenColumn);
                dateModified = cur.getLong(dateModifiedColumn);
                orientation = cur.getInt(orientationColumn);
                mimeType = cur.getString(mimeTypeColumn);
                data = cur.getString(dataColumn);

                // Add imageItem
                ArrayList<ItemModel> imageList = categoryMap.get(bucket);
                if (imageList == null) {
                    imageList = new ArrayList<>();
                    categoryMap.put(bucket, imageList);
                }
                imageList.add(new ImageItemModel(data, bucket, id, dateTaken, dateModified, mimeType, orientation));
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
