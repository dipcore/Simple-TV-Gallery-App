package com.dipcore.tvimagegallery.items.image;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.dipcore.tvimagegallery.widget.rendererrecyclerviewadapter.ItemModel;

import java.io.Serializable;

public class ImageItemModel implements ItemModel, Serializable {

    public static final int TYPE = 1;

    @NonNull
    private String title;
    @NonNull
    private String bucket;
    @NonNull
    private long id;
    @NonNull
    private long dateTaken;
    @NonNull
    private long dateModified;
    @NonNull
    private String mimeType;
    @NonNull
    private int orientation;

    public ImageItemModel(@NonNull final String title) {
        this.title = title;
    }

    public ImageItemModel(@NonNull final String title, String bucket, long id, long dateTaken, long dateModified, String mimeType, int orientation) {
        this.title = title;
        this.bucket = bucket;
        this.id = id;
        this.dateTaken = dateTaken;
        this.dateModified = dateModified;
        this.mimeType = mimeType;
        this.orientation = orientation;
    }

    public int getType() {
        return TYPE;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getBucket() {
        return bucket;
    }

    public void setBucket(@NonNull String bucket) {
        this.bucket = bucket;
    }

    @NonNull
    public long getId() {
        return id;
    }

    public void setId(@NonNull long id) {
        this.id = id;
    }

    @NonNull
    public long getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(@NonNull long dateTaken) {
        this.dateTaken = dateTaken;
    }

    @NonNull
    public long getDateModified() {
        return dateModified;
    }

    public void setDateModified(@NonNull long dateModified) {
        this.dateModified = dateModified;
    }

    @NonNull
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(@NonNull String mimeType) {
        this.mimeType = mimeType;
    }

    @NonNull
    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(@NonNull int orientation) {
        this.orientation = orientation;
    }

    @NonNull
    public Uri getUri() {
        Uri baseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        return Uri.withAppendedPath(baseUri, Long.toString(id));
    }

}
