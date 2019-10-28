package com.example.tba_osa_2019;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tba_osa_2019.helper.HttpJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GuestHomePage extends AppCompatActivity {

    private Button go;
    private RadioGroup rgp;
    private RadioButton phoneBtn;
    private RadioButton headphoneBtn;
    private RadioButton tvBtn;

    private int success;
    private ProgressDialog pDialog;
    private Integer product_ID;
    private String productName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_home_page);

        rgp = (RadioGroup) findViewById(R.id.radioGroup2);
        phoneBtn=(RadioButton) findViewById(R.id.phoneButton);
        headphoneBtn=(RadioButton) findViewById(R.id.headphoneButton);
        tvBtn=(RadioButton) findViewById(R.id.tvButton);

        go = (Button) findViewById(R.id.goBtn);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper();
            }
        });
    }

    public void Helper(){
        if (rgp.getCheckedRadioButtonId() == R.id.phoneButton){
            Intent intent = new Intent(this, PhonesActivityGuest.class);
            startActivity(intent);
        }
        else if (rgp.getCheckedRadioButtonId() == R.id.headphoneButton) {
            Intent intent = new Intent( this, HeadphonesActivityGuest.class);
            startActivity(intent);
        }
        else if (rgp.getCheckedRadioButtonId() == R.id.tvButton) {
            Intent intent = new Intent( this, TVsActivityGuest.class);
            startActivity(intent);
        }
    }




}
