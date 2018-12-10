package com.darkphoenix.vision;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class StepsInfo extends AppCompatActivity {
    static final int frameCount = 25;
    static final int REQUEST_VIDEO_CAPTURE = 1;
    private Button rec;
    ProgressDialog progressDialog;
    ArrayList<Uri> imgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_info);
        rec = (Button) findViewById(R.id.rec);
        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/video.mp4");
                System.out.println(mediaFile.toURI().toString());
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                Uri videoUri = FileProvider.getUriForFile(StepsInfo.this, "com.darkphoenix.vision.provider", mediaFile);
                intent.setClipData(ClipData.newRawUri("", videoUri));
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Video saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
                Uri videoUri = data.getData();
                try
                {
                    File imageFile = new File(getPath(videoUri));
                    Decoder d = new Decoder();
                    d.execute(imageFile);
                }
                catch (NullPointerException e)
                {
                    e.getStackTrace();
                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video",
                        Toast.LENGTH_LONG).show();
            }
        }

    }
    private class Decoder extends AsyncTask<File, Integer, Integer> {
        private static final String TAG = "DECODER";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(StepsInfo.this);
            progressDialog.setMessage("Processing...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(File... params) {
            try {
                File f = params[0];
                System.out.println(params[0].toURI().toString());
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(f.getAbsolutePath());
                long sec = 2000000;
                imgs = new ArrayList<>();
                for (int i = 0; i<frameCount; i++) {
                    try {
                        Bitmap bitmap = retriever.getFrameAtTime(sec, MediaMetadataRetriever.OPTION_CLOSEST);
                        OutputStream os = null;
                        try {
                            f = new File(params[0].getParentFile(), String.format("img%08d.jpg", i));
                            Uri uri = Uri.fromFile(f);
                            imgs.add(uri);
                            os = new BufferedOutputStream(new FileOutputStream(f));
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, os);
                        } finally {
                            if (os != null)
                                os.close();
                        }
                    }
                    catch (RuntimeException ex) {
                        ex.printStackTrace();
                        break;
                    }
                    sec+=500000;
                }
                try {
                    retriever.release();
                } catch (RuntimeException ex) {
                    ex.printStackTrace();
                }
            }
            catch (IOException e) {
                Log.e(TAG, "IO", e);
                return -1;
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result)
        {
            progressDialog.dismiss();
            if (result == -1) {
                Toast.makeText(getApplicationContext(),"Error Occurred",Toast.LENGTH_LONG).show();
                StepsInfo.this.finish();
            } else {
                    Toast.makeText(getApplicationContext(), "Done processing", Toast.LENGTH_LONG).show();
                    Intent intentmove = new Intent(StepsInfo.this, VideoCaptureActivity.class);
                    intentmove.putParcelableArrayListExtra("files", imgs);
                    startActivity(intentmove);
            }
        }
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
