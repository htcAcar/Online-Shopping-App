package com.example.tba_osa_2019;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tba_osa_2019.helper.CheckNetworkStatus;
import com.example.tba_osa_2019.helper.HttpJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomePageActivity extends LoginActivity {

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String BASE_URL = "http://10.225.44.152/tba-db/";
    private static String KEY_CUSTOMER_ID = "customer_ID";
    private static String KEY_PRODUCT_ID = "product_ID";
    private static String KEY_PRODUCT_NAME = "name";

    private Button addToCart;
    private Button addToCart2;
    private Button addToCart3;
    private Button goToProfile;
    private Button goToCart;
    private Button loginOut;

    private Button go;
    private RadioGroup rgp;
    private RadioButton phoneBtn;
    private RadioButton headphoneBtn;
    private RadioButton tvBtn;

    private TextView ProductNameText;
    private TextView ProductNameText2;
    private TextView ProductNameText3;

    private int success;
    private ProgressDialog pDialog;
    private Integer customer_ID = onlineCustomer.ID;
    private Integer product_ID;
    private String productName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);




        loginOut = findViewById(R.id.btn_log_out);
        loginOut.setBackgroundResource(R.drawable.button_border);
        loginOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                logOut();
            }
        });

        goToProfile = (Button) findViewById(R.id.ProfileButton);
        goToProfile.setBackgroundResource(R.drawable.button_border);
        goToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goProfile();
            }
        });

        goToCart = (Button) findViewById(R.id.cartButton);
        goToCart.setBackgroundResource(R.drawable.button_border);
        goToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCart();
            }
        });

        /*
        ProductNameText = (TextView) findViewById(R.id.ProductNameTextView);
        ProductNameText2 = (TextView) findViewById(R.id.ProductNameTextView2);
        ProductNameText3 = (TextView) findViewById(R.id.ProductNameTextView3);
        */

        rgp = (RadioGroup) findViewById(R.id.radioGroup2);
        phoneBtn=(RadioButton) findViewById(R.id.phoneButton);
        headphoneBtn=(RadioButton) findViewById(R.id.headphoneButton);
        tvBtn=(RadioButton) findViewById(R.id.tvButton);

        go = (Button) findViewById(R.id.goBtn);
        go.setBackgroundResource(R.drawable.button_border);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper();
            }
        });

    }

    public void Helper(){
        if (rgp.getCheckedRadioButtonId() == R.id.phoneButton){
            Intent intent = new Intent(this, PhonesActivity.class);
            startActivity(intent);
        }
        else if (rgp.getCheckedRadioButtonId() == R.id.headphoneButton) {
            Intent intent = new Intent( this, HeadphonesActivity.class);
            startActivity(intent);
        }
        else if (rgp.getCheckedRadioButtonId() == R.id.tvButton) {
            Intent intent = new Intent( this, TVsActivity.class);
            startActivity(intent);
        }
    }

    // Method to add products to cart
    public void getProductName(TextView productNameText) {
        productName = productNameText.getText().toString();
        new HomePageActivity.getProductNameAsyncTask().execute();
    }

    private class getProductNameAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(HomePageActivity.this);
            pDialog.setMessage("Getting product name. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            httpParams.put(KEY_PRODUCT_NAME, productName);
            httpParams.put(KEY_CUSTOMER_ID, customer_ID+"");
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "carts/add_toCart.php", "GET", httpParams);
            Log.w("AfterGettin", "git json");
            JSONObject data;
            try {
                Log.w("jsonObj", "" + jsonObject);
                success = jsonObject.getInt(KEY_SUCCESS);
                Log.w("AfterGettin", "git json");
                if (success == 1){
                    data = jsonObject.getJSONObject(KEY_DATA);
                    product_ID = data.getInt(KEY_PRODUCT_ID);
                }
                else{
                    Toast.makeText(HomePageActivity.this,
                            "Some error occurred while adding user",
                            Toast.LENGTH_LONG).show();
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
                        Toast.makeText(HomePageActivity.this,
                                "Done", Toast.LENGTH_LONG).show();
                        Intent i = getIntent();
                        //send result code 20 to notify about movie update
                        setResult(20, i);
                        // Call addCartAsyncTask for send customer and product id to database
                        //Finish ths activity and go back to listing activity
                        finish();
                        startActivity(i);

                    } else {
                        Toast.makeText(HomePageActivity.this,
                                "Some error occurred while adding user",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }



    public void goProfile() {
        // Method to go user's profile
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void goCart() {
        // Method to view shopping cart.
        Intent intent = new Intent(this, ShoppingCartActivity.class);
        startActivity(intent);
    }

    public void logOut(){
        onlineCustomer = null;
        Intent intent = new Intent( this, MainActivity.class);
        startActivity(intent);

    }
}