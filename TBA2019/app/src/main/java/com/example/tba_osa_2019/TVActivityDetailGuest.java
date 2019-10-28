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

import static com.example.tba_osa_2019.LoginActivity.onlineCustomer;

public class TVActivityDetailGuest extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static String KEY_CUSTOMER_ID = "customer_ID";
    private static final String KEY_PRODUCT_ID = "product_ID";
    private static final String KEY_NAME = "name";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_SCREEN_SIZE = "screen_size";
    private static final String KEY_RESOLUTION = "resolution";
    private static final String KEY_PRICE = "price";

    private static final String BASE_URL = "http://10.225.44.152/tba-db/";

    private ProgressDialog pDialog;
    private int success;

    private TextView Brandname;
    private TextView TVname;
    private TextView SSname;
    private TextView Resname;
    private TextView pricename;



    private String brand;
    private String name;
    private String screenS;
    private String resolution;
    private String price;

    private Integer productID;
    private String productIDstr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvdetail_guest);

        Intent intent = getIntent();
        Brandname = findViewById(R.id.brandTextTV);
        TVname = findViewById(R.id.nameTextTV);
        SSname = findViewById(R.id.screenTextTV);
        Resname = findViewById(R.id.resTextTV);
        pricename = findViewById(R.id.priceTextTV);
        productIDstr = intent.getStringExtra(KEY_PRODUCT_ID);

        new TVActivityDetailGuest.TVDetailAsyncTask().execute();


    }
    private class TVDetailAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display proggress bar
            pDialog = new ProgressDialog(TVActivityDetailGuest.this);
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
                    BASE_URL + "products/tv/get_tv_details.php", "GET", httpParams);

            try {
                success = jsonObject.getInt(KEY_SUCCESS);

                JSONObject productTV;

                if (success == 1) {
                    productTV = jsonObject.getJSONObject(KEY_DATA);

                    productID = productTV.getInt(KEY_PRODUCT_ID);
                    Log.w("customer1",productID+"" );
                    brand = productTV.getString(KEY_BRAND);
                    Log.w("customer1",brand+"" );
                    name = productTV.getString(KEY_NAME);
                    Log.w("products.length()2",name+"" );
                    screenS = productTV.getString(KEY_SCREEN_SIZE);
                    Log.w("products.length()3",screenS+"" );
                    resolution = productTV.getString(KEY_RESOLUTION);
                    Log.w("products.length()6",resolution+"" );
                    price = productTV.getString(KEY_PRICE);
                    Log.w("products.length()6",price+"" );




                    //Parse the JSON response
                   /*
                    product_name = productTV.getString(KEY_PRODUCT_NAME);
                    product_brand = productTV.getString(KEY_PRODUCT_BRAND);
                    product_price = productTV.getInt(KEY_PRODUCT_PRICE);*/

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
                        TVname.setText(name);
                        SSname.setText(screenS);
                        Resname.setText(resolution);
                        pricename.setText(price);

                        Intent i = getIntent();
                        //send result code 20 to notify about movie update
                        setResult(20, i);
                        //Finish ths activity and go back to listing activity
                        //finish();

                    } else {
                        Toast.makeText(TVActivityDetailGuest.this,
                                "Some error occurred while logging in :(",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }


    }


}
