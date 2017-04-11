package com.dipcore.tvimagegallery.items.image;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import com.dipcore.tvimagegallery.widget.rendererrecyclerviewadapter.ItemModel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageItemModel implements ItemModel, Serializable {

    public static final int TYPE = 1;

    private String data;
    private String name;
    private String bucket;
    private long id;
    private long dateTaken;
    private long dateModified;
    private String mimeType;
    private int orientation;
    private int height;
    private int width;
    private long bytes;

    public ImageItemModel(final String name) {
        this.name = name;
    }

    public ImageItemModel(String data, String name, String bucket, long id, long dateTaken, long dateModified, String mimeType, int orientation, int height, int width, long bytes) {
        this.data = data;
        this.name = name;
        this.bucket = bucket;
        this.id = id;
        this.dateTaken = dateTaken;
        this.dateModified = dateModified;
        this.mimeType = mimeType;
        this.orientation = orientation;
        this.height = height;
        this.width = width;
        this.bytes = bytes;
    }

    public int getType() {
        return TYPE;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDateTaken() {
        return dateTaken;
    }

    public String getDateTaken(String format) {
        // milliseconds since 1970
        return dateFormat(dateTaken, format);
    }

    public void setDateTaken(long dateTaken) {
        this.dateTaken = dateTaken;
    }

    public long getDateModified() {
        return dateModified;
    }

    public String getDateModified(String format) {
        // seconds since 1970
        return dateFormat(dateModified * 1000, format);
    }

    public void setDateModified(long dateModified) {
        this.dateModified = dateModified;
    }


    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public long getBytes() {
        return bytes;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public String getSizeHumanReadable() {
        return humanReadableByteCount(bytes, true);
    }

    public Uri getUri() {
        Uri baseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        return Uri.withAppendedPath(baseUri, Long.toString(id));
    }

    ///////

    private String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    private String dateFormat(long date, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(date));
    }

}
