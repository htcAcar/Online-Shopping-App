package com.example.tba_osa_2019;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tba_osa_2019.helper.CheckNetworkStatus;
import com.example.tba_osa_2019.helper.HttpJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.tba_osa_2019.LoginActivity.onlineCustomer;

public class PhoneActivityDetailGuest extends AppCompatActivity {

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_PRODUCT_ID = "product_ID";
    private static final String KEY_NAME = "name";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_COLOR = "color";
    private static final String KEY_SCREEN_SIZE = "screen_size";
    private static final String KEY_MEMORY = "memory";
    private static final String KEY_CAMERA = "camera";
    private static final String KEY_PRICE = "price";

    private static final String BASE_URL = "http://10.225.44.152/tba-db/";

    private ProgressDialog pDialog;
    private int success;

    private TextView Brandname;
    private TextView phonename;
    private TextView Colorname;
    private TextView Screenname;
    private TextView Memoryname;
    private TextView Cameraname;
    private TextView pricename;

    private String brand;
    private String name;
    private String color;
    private String memory;
    private String screenSize;
    private String camera;
    private String price;

    private int productID;
    private String productIDstr;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_detail_guest);

        Intent intent = getIntent();
        Brandname = (TextView) findViewById(R.id.phoneBrandText);
        phonename = (TextView)findViewById(R.id.phoneNameText);
        Colorname = (TextView)findViewById(R.id.phoneColorText);
        Screenname =(TextView) findViewById(R.id.phoneScreenText);
        Memoryname = (TextView)findViewById(R.id.phoneMemoryText);
        Cameraname = (TextView)findViewById(R.id.phoneCameraText);
        pricename = (TextView)findViewById(R.id.phonePriceText);
        productIDstr = intent.getStringExtra(KEY_PRODUCT_ID);

        new PhoneActivityDetailGuest.phoneDetailAsyncTask().execute();



    }



    private class phoneDetailAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display proggress bar
            pDialog = new ProgressDialog(PhoneActivityDetailGuest.this);
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
                    BASE_URL + "products/phone/get_phone_details.php", "GET", httpParams);

            try {
                success = jsonObject.getInt(KEY_SUCCESS);

                JSONObject productPhone;

                if (success == 1) {
                    productPhone = jsonObject.getJSONObject(KEY_DATA);

                    productID = productPhone.getInt(KEY_PRODUCT_ID);
                    Log.w("customer1",productID+"" );
                    brand = productPhone.getString(KEY_BRAND);
                    Log.w("customer1",brand+"" );
                    name = productPhone.getString(KEY_NAME);
                    Log.w("products.length()2",name+"" );
                    color = productPhone.getString(KEY_COLOR);
                    Log.w("products.length()3",color+"" );
                    screenSize = productPhone.getString(KEY_SCREEN_SIZE);
                    Log.w("products.length()6",screenSize+"" );
                    memory = productPhone.getString(KEY_MEMORY);
                    Log.w("products.length()6",memory+"" );
                    camera = productPhone.getString(KEY_CAMERA);
                    Log.w("products.length()6",camera+"" );
                    price = productPhone.getString(KEY_PRICE);
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
                        Log.w("products",price+"" );
                        Brandname.setText(brand);
                        Log.w("produgth()6",price+"" );
                        phonename.setText(name);
                        Log.w("products.leth()6",price+"" );
                        Colorname.setText(color);
                        Log.w("pros.length()6",price+"" );
                        Screenname.setText(screenSize);
                        Log.w("productsgth()6",price+"" );
                        Memoryname.setText(memory);
                        Log.w("products.len",price+"" );
                        Cameraname.setText(camera);
                        Log.w("products.lengt6",price+"" );
                        pricename.setText(price);
                        Log.w("products.lengt",price+"" );


                        Intent i = getIntent();
                        //send result code 20 to notify about movie update
                        Log.w("products.length()6",price+"" );
                        setResult(20, i);
                        //Finish ths activity and go back to listing activity
                        //finish();

                    } else {
                        Toast.makeText(PhoneActivityDetailGuest.this,
                                "Some error occurred while logging in :(",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }


    }
}
