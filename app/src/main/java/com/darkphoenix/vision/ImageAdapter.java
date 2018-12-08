package com.darkphoenix.vision;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class ImageAdapter extends BaseAdapter {

    private Uri[] mThumbIds;
    private Context mContext;
    //public Uri imageUri;
    // Constructor
    public ImageAdapter(Context c, Uri[] thumbs) {
        mContext = c;
        this.mThumbIds=thumbs;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Uri getItem(int position) {
        return mThumbIds[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(250, 250));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5, 5, 5, 5);
        }
        else
        {
            imageView = (ImageView) convertView;
        }
        File fileLocation = new File(mThumbIds[position].getPath());//file path, which can be String, or Uri
        Picasso.with(mContext).load(fileLocation).fit().centerCrop().into(imageView);
        //imageView.setImageBitmap(getbitpam(mThumbIds[position].getPath()));
        return imageView;
    }
    private Bitmap getbitpam(String path){
        Bitmap imgthumBitmap=null;
        try
        {

            final int THUMBNAIL_SIZE = 64;

            FileInputStream fis = new FileInputStream(path);
            imgthumBitmap = BitmapFactory.decodeStream(fis);

            imgthumBitmap = Bitmap.createScaledBitmap(imgthumBitmap,
                    THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);

            ByteArrayOutputStream bytearroutstream = new ByteArrayOutputStream();
            imgthumBitmap.compress(Bitmap.CompressFormat.JPEG, 100,bytearroutstream);


        }
        catch(Exception ex) {

        }
        return imgthumBitmap;
    }
}
