package com.darkphoenix.vision;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ImageReady extends AppCompatActivity {

    Service service;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_ready);
        progressDialog = new ProgressDialog(ImageReady.this);
        progressDialog.setMessage("Getting Predictions...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        File file = (File)getIntent().getExtras().get("data");
        //HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().build(); //addInterceptor(interceptor).build();
        service = new Retrofit.Builder().baseUrl("http://apivision.azurewebsites.net").client(client).build().create(Service.class);

        //File file = new File(uri.getPath());
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "name");

//            Log.d("THIS", data.getData().getPath());

        retrofit2.Call<okhttp3.ResponseBody> req = service.postImage(body, name);
        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s="";
                try
                {
                    s = response.body().string();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                System.out.println(s);
                Intent intent = new Intent(ImageReady.this,Prediction.class);
                intent.putExtra("pred",s);
                progressDialog.dismiss();
                Toast.makeText(ImageReady.this, "Successfully Uploaded", Toast.LENGTH_LONG).show();
                startActivity(intent);
                ImageReady.this.finish();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                Toast.makeText(ImageReady.this, "Upload Failed", Toast.LENGTH_LONG).show();
                ImageReady.this.finish();
            }
        });

    }
}
