package com.darkphoenix.vision;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private EditText age,name,phone;
    private Button sbmt;
    ProgressDialog progressDialog;
    private static final String URL="";
    String T="Print";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        age=(EditText)findViewById(R.id.age);
        name=(EditText)findViewById(R.id.name);
        phone=(EditText)findViewById(R.id.phone);
        sbmt=(Button)findViewById(R.id.submit);
        sbmt.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty()) Toast.makeText(getApplicationContext(), "Enter Name", Toast.LENGTH_LONG).show();
                else if(age.getText().toString().isEmpty()) Toast.makeText(getApplicationContext(), "Enter Age", Toast.LENGTH_LONG).show();
                else if(phone.getText().toString().isEmpty()) Toast.makeText(getApplicationContext(), "Enter Phone", Toast.LENGTH_LONG).show();
                else {
                    Log.d(T, age.getText().toString());
                    Log.d(T, name.getText().toString());
                    Log.d(T, phone.getText().toString());
                    Intent in = new Intent(Register.this,StepsInfo.class);
                    startActivity(in);
                    //RegisterPatient reg = new RegisterPatient();
                    //reg.execute(name.getText().toString(), age.getText().toString(), phone.getText().toString());
                }
            }
        });

    }

    /*private class RegisterPatient extends AsyncTask<String,Void,JSONObject>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Register.this);
            progressDialog.setMessage("Registering...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            Window window = progressDialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            String name =args[0];
            String age =args[1];
            String phone =args[2];

            HashMap<String,String> params = new HashMap<>();
            params.put("Name", name);
            params.put("Age", age);
            params.put("Phone", phone);

            JSONObject js;//jsParser.makeHttpRequest(URL, "POST", params);

            return js;

        }

        @Override
        protected void onPostExecute(JSONObject result)
        {
            progressDialog.dismiss();
            try {
                if (result != null) {
                    Toast.makeText(getApplicationContext(),result.getString("message"),Toast.LENGTH_LONG).show();
                    if(result.getInt("success")==1){
                        Register.this.finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/
}
