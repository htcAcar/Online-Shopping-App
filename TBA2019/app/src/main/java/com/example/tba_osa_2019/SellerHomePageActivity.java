package com.example.tba_osa_2019;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tba_osa_2019.helper.CheckNetworkStatus;

public class SellerHomePageActivity extends AppCompatActivity {
    private ImageView addPhone;
    private ImageView addHeadphone;
    private ImageView addTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home_page);

        addPhone = (ImageView) findViewById(R.id.AddPhoneButton);
        addPhone.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){
            if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                goAddPhone();
            }
            else {
                Toast.makeText(SellerHomePageActivity.this, "Unable to connect to internet", Toast.LENGTH_LONG).show();
            }

        }

        });

        addHeadphone = (ImageView) findViewById(R.id.AddHeadphoneButton);
        addHeadphone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    goAddHeadphone();
                }
                else {
                    Toast.makeText(SellerHomePageActivity.this, "Unable to connect to internet", Toast.LENGTH_LONG).show();
                }

            }

        });

        addTv = (ImageView) findViewById(R.id.AddTVButton);
        addTv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    goAddTv();
                }
                else {
                    Toast.makeText(SellerHomePageActivity.this, "Unable to connect to internet", Toast.LENGTH_LONG).show();
                }

            }

        });

    }

    public void goAddPhone(){
        Intent intent = new Intent(SellerHomePageActivity.this, AddPhoneActivity.class);
        startActivity(intent);
    }

    public void goAddHeadphone(){
        Intent intent = new Intent(this, AddHeadphoneActivity.class);
        startActivity(intent);
    }

    public void goAddTv(){
        Intent intent = new Intent(this, AddTvActivity.class);
        startActivity(intent);
    }

}
