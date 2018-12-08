package com.darkphoenix.vision;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class VideoCaptureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_capture);

        final ArrayList<Parcelable> uris =
                getIntent().getParcelableArrayListExtra("files");
        int len = uris.size(),i=0;
        Uri[] arr = new Uri[len];
        for (Parcelable p : uris) {
            Uri uri = (Uri) p;
            arr[i++] = uri;
        }
        GridView gridView = (GridView)findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter(this,arr));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id)
            {

                Intent intent = new Intent(VideoCaptureActivity.this, ImageViewerActivity.class);
                intent.putExtra("path", uris.get(position));
                startActivity(intent);
            }
        });
        gridView.invalidateViews();
    }
}
