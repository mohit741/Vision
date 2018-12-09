package com.darkphoenix.vision;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class SplashScreen extends AppCompatActivity {
    AlertDialog.Builder build;
    AlertDialog dailog;
    private static boolean f = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
        NetworkChecker task = new NetworkChecker();
        task.execute(null, null, null);

        build = new AlertDialog.Builder(SplashScreen.this);
        build.setMessage("This application requires Internet connection.Would you connect to internet ?");
        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                finish();
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));

            }
        });
        build.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                build.setMessage("Are sure you want to exit?");
                build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        finish();
                    }
                });
                build.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        finish();
                        Intent intent = new Intent(SplashScreen.this,
                                SplashScreen.class);
                        startActivity(intent);

                        dialog.dismiss();

                    }
                });
                dailog = build.create();
                Window window = dailog.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dailog.show();
            }
        });

        dailog = build.create();
        Window window = dailog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    public static boolean hasInternet(Activity a) {
        boolean hasConnectedWifi = false;
        boolean hasConnectedMobile = false;
        try {

            ConnectivityManager cm = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getType() == ConnectivityManager.TYPE_WIFI)
                    if (ni.isConnected())
                        hasConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("mobile"))
                    if (ni.isConnected())
                        hasConnectedMobile = true;
            }
            return hasConnectedWifi || hasConnectedMobile;
        }
        catch (Exception ex) {
            Log.d("Network", ex.toString());
        }
        return false;
    }
    public class NetworkChecker extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog spinner;

        @Override
        protected void onPreExecute()
        {
            spinner = ProgressDialog.show(SplashScreen.this,"","Checking internet connection...",false);
            Window window = spinner.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        }

        @Override
        protected void onPostExecute(Void v)
        {
            checkFlag();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(hasInternet(SplashScreen.this))
            {
                Intent intent = new Intent(SplashScreen.this,
                        MainActivity.class);
                startActivity(intent);
                spinner.dismiss();
                finish();
            }
            else
            {
                Log.d("C","checked");
                spinner.dismiss();
                f = true;
            }
            return null;
        }
    }
    private void checkFlag()
    {
        if(f) dailog.show();
        f = false;
    }
}
