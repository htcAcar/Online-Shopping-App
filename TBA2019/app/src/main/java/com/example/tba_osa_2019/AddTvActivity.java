package com.example.tba_osa_2019;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.tba_osa_2019.LoginActivity.onlineSeller;

public class AddTvActivity extends AppCompatActivity {

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static String KEY_SELLER_ID = "seller_ID";
    private static final String KEY_PRODUCT_ID = "product_ID";
    private static final String KEY_NAME = "name";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_SCREEN_SIZE = "screen_size";
    private static final String KEY_RESOLUTION = "resolution";
    private static final String KEY_PRICE = "price";

    private static final String BASE_URL = "http://10.225.44.152/tba-db/";

    private ProgressDialog pDialog;
    private int success;

    private TextView addTv;

    private TextView nameTextView;
    private EditText nameEditText;

    private TextView brandTextView;
    private EditText brandEditText;

    private TextView screenTextView;
    private EditText screenEditText;

    private TextView resolutionTextView;
    private EditText resolutionEditText;

    private TextView priceTextView;
    private EditText priceEditText;

    private String brand;
    private String name;
    private String screenS;
    private String resolution;
    private String price;
    private int priceAdd;

    private int sellerID = onlineSeller.seller_ID;

    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tv);

        addTv = (TextView) findViewById(R.id.addTvTextView);

        nameTextView = (TextView) findViewById(R.id.nameTextView);
        brandTextView = (TextView) findViewById(R.id.brandTextView);
        screenTextView = (TextView) findViewById(R.id.screenTextView);
        resolutionTextView = (TextView) findViewById(R.id.resolutionTextView);
        priceTextView = (TextView) findViewById(R.id.priceTextView);

        nameEditText = (EditText) findViewById(R.id.nameEditText);
        nameEditText.setBackgroundResource(R.drawable.button_border);

        brandEditText = (EditText) findViewById(R.id.brandEditText);
        brandEditText.setBackgroundResource(R.drawable.button_border);

        screenEditText = (EditText) findViewById(R.id.screenEditText);
        screenEditText.setBackgroundResource(R.drawable.button_border);

        resolutionEditText = (EditText) findViewById(R.id.resolutionTextView);
        resolutionEditText.setBackgroundResource(R.drawable.button_border);

        priceEditText = (EditText) findViewById(R.id.priceEditText);
        priceEditText.setBackgroundResource(R.drawable.button_border);

        addButton = (Button) findViewById(R.id.addButtonTV);
        addButton.setBackgroundResource(R.drawable.button_border);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (com.example.tba_osa_2019.helper.CheckNetworkStatus.isNetworkAvailable(getApplicationContext()))
                {
                    setProductDetails();
                }
                else
                {
                    Toast.makeText(AddTvActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    public void setProductDetails()
    {
        brand = brandEditText.getText().toString();
        name = nameEditText.getText().toString();
        screenS = screenEditText.getText().toString();
        resolution = resolutionEditText.getText().toString();
        price = priceEditText.getText().toString();
        priceAdd = Integer.parseInt(price);
        new SetNewProductAsyncTask().execute();
    }

    public void goSellerHomePage(){
        // Method to go user's profile
        Intent intent = new Intent(this, SellerHomePageActivity.class);
        startActivity(intent);
    }

    private class SetNewProductAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(AddTvActivity.this);
            pDialog.setMessage("Getting product name. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            com.example.tba_osa_2019.helper.HttpJsonParser httpJsonParser = new com.example.tba_osa_2019.helper.HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            httpParams.put(KEY_NAME, name);
            httpParams.put(KEY_BRAND, brand);
            httpParams.put(KEY_SCREEN_SIZE, screenS);
            httpParams.put(KEY_RESOLUTION, resolution);
            httpParams.put(KEY_PRICE, priceAdd+"");
            httpParams.put(KEY_SELLER_ID, sellerID+"");
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "products/tv/add_tv.php", "GET", httpParams);
            Log.w("AfterGettin", "git json");
            JSONObject data;
            try {
                Log.w("jsonObj", "" + jsonObject);
                success = jsonObject.getInt(KEY_SUCCESS);
                Log.w("AfterGettin", "git json");

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
                        Toast.makeText(AddTvActivity.this,
                                "Done", Toast.LENGTH_LONG).show();
                        Intent i = getIntent();
                        //send result code 20 to notify about movie update
                        setResult(20, i);
                        // Call addCartAsyncTask for send customer and product id to database
                        //Finish ths activity and go back to listing activity
                        goSellerHomePage();

                    } else {
                        Toast.makeText(AddTvActivity.this,
                                "Some error occurred while adding user",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }



}