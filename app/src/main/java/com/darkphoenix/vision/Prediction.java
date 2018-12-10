package com.darkphoenix.vision;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Prediction extends AppCompatActivity {

    TextView txt;
    TextView pred;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);
        pred = (TextView)findViewById(R.id.result);
        txt = (TextView)findViewById(R.id.txt);
        String s=getIntent().getStringExtra("pred");
        System.out.println(s);
        try{
            JSONObject predJson = new JSONObject(s);
            float f = Float.valueOf(predJson.getString("risk"));
            DecimalFormat df = new DecimalFormat("##.##");
            df.setRoundingMode(RoundingMode.CEILING);
            pred.setText(String.valueOf(df.format(f)));
            String head = "This person is at ";
            String tail = " risk of Glaucoma.";
            String res ="";
            if(f<=30)
                res = head + "low" +tail;
            else if(f>30 || f<=65)
                res = head + "mild" + tail;
            else
                res = head + "severe" + tail;
            txt.setText(res);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }
}
