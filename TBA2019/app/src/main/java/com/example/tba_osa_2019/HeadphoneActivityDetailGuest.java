package com.example.tba_osa_2019;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tba_osa_2019.helper.HttpJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HeadphoneActivityDetailGuest extends AppCompatActivity {

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_PRODUCT_ID = "product_ID";
    private static final String KEY_NAME = "name";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_COLOR = "color";
    private static final String KEY_CABLE_LENGTH = "cable_length";
    private static final String KEY_PRICE = "price";

    private static final String BASE_URL = "http://10.225.44.152/tba-db/";

    private ProgressDialog pDialog;
    private int success;

    private TextView Brandname;
    private TextView Headphonename;
    private TextView Colorname;
    private TextView Cablename;
    private TextView pricename;

    private String brand;
    private String name;
    private String color;
    private String cableLength;
    private String price;

    private Integer productID;
    private String productIDstr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headphone_detail_guest);

        Intent intent = getIntent();
        Brandname = findViewById(R.id.headBrandText);
        Headphonename = findViewById(R.id.nameTexthead);
        Colorname = findViewById(R.id.headColorText);
        Cablename = findViewById(R.id.headCableText);
        pricename = findViewById(R.id.headPriceText);
        productIDstr = intent.getStringExtra(KEY_PRODUCT_ID);
        new HeadphoneActivityDetailGuest.headphoneDetailAsyncTask().execute();
    }

    private class headphoneDetailAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display proggress bar
            pDialog = new ProgressDialog(HeadphoneActivityDetailGuest.this);
            pDialog.setMessage("Loading. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            httpParams.put(KEY_PRODUCT_ID, productIDstr);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "products/headphone/get_headphone_details.php", "GET", httpParams);

            try {
                success = jsonObject.getInt(KEY_SUCCESS);

                JSONObject productHead;

                if (success == 1) {
                    productHead = jsonObject.getJSONObject(KEY_DATA);

                    productID = productHead.getInt(KEY_PRODUCT_ID);
                    Log.w("customer1",productID+"" );
                    brand = productHead.getString(KEY_BRAND);
                    Log.w("customer1",brand+"" );
                    name = productHead.getString(KEY_NAME);
                    Log.w("products.length()2",name+"" );
                    color = productHead.getString(KEY_COLOR);
                    Log.w("products.length()3",color+"" );
                    cableLength = productHead.getString(KEY_CABLE_LENGTH);
                    Log.w("products.length()6",cableLength+"" );
                    price = productHead.getString(KEY_PRICE);
                    Log.w("products.length()6",price+"" );




                    //Parse the JSON response
                   /*
                    product_name = productHead.getString(KEY_PRODUCT_NAME);
                    product_brand = productHead.getString(KEY_PRODUCT_BRAND);
                    product_price = productHead.getInt(KEY_PRODUCT_PRICE);*/

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            Log.w("AfterGettin", "Inside onPost");
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 1) {
                        //Display success message
                        Brandname.setText(brand);
                        Headphonename.setText(name);
                        Colorname.setText(color);
                        Cablename.setText(cableLength);
                        pricename.setText(price);

                        Intent i = getIntent();
                        //send result code 20 to notify about movie update
                        setResult(20, i);
                        //Finish ths activity and go back to listing activity
                        //finish();

                    } else {
                        Toast.makeText(HeadphoneActivityDetailGuest.this,
                                "Some error occurred while logging in :(",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }


    }


}
