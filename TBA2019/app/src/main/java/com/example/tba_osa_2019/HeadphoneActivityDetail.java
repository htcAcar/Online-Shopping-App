package com.example.tba_osa_2019;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tba_osa_2019.helper.CheckNetworkStatus;
import com.example.tba_osa_2019.helper.HttpJsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.tba_osa_2019.LoginActivity.onlineCustomer;

public class HeadphoneActivityDetail extends AppCompatActivity {

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static String KEY_CUSTOMER_ID = "customer_ID";
    private static final String KEY_PRODUCT_ID = "product_ID";
    private static final String KEY_NAME = "name";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_COLOR = "color";
    private static final String KEY_CABLE_LENGTH = "cable_length";
    private static final String KEY_PRICE = "price";
    private static final String KEY_REVIEW_TEXT = "review";
    private static final String KEY_REVIEW_USERNAME = "customer_ID";

    private static final String BASE_URL = "http://10.225.44.152/tba-db/";

    private ProgressDialog pDialog;
    private int success;

    private TextView Brandname;
    private TextView Headphonename;
    private TextView Colorname;
    private TextView Cablename;
    private TextView pricename;

    private Button AddCartBtn2;
    private Button AddCommentButton;
    private Button AddToFavButton;

    private String brand;
    private String name;
    private String color;
    private String cableLength;
    private String price;

    private Integer productID;
    private String productIDstr;

    private Integer customer_ID = onlineCustomer.ID;


    private ArrayList<HashMap<String, String>> reviewList;
    private ListView reviewListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headphone_detail);

        Intent intent = getIntent();
        Brandname = findViewById(R.id.headBrandText);
        Headphonename = findViewById(R.id.nameTexthead);
        Colorname = findViewById(R.id.headColorText);
        Cablename = findViewById(R.id.headCableText);
        pricename = findViewById(R.id.headPriceText);
        reviewListView = (ListView) findViewById(R.id.reviewListScrollView);
        productIDstr = intent.getStringExtra(KEY_PRODUCT_ID);
        new headphoneDetailAsyncTask().execute();

        AddCartBtn2 = (Button) findViewById(R.id.AddCartHead);
        AddCartBtn2.setBackgroundResource(R.drawable.button_border);
        AddCartBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    getProductName(Headphonename);
                } else {
                    Toast.makeText(HeadphoneActivityDetail.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }

            }
        });

        AddCommentButton = (Button) findViewById(R.id.addCommentButtonView);
        AddCommentButton.setBackgroundResource(R.drawable.button_border);
        AddCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    // go to add comment activity
                    Intent i = new Intent(getApplicationContext(),
                            AddCommentToProduct.class);
                    i.putExtra(KEY_PRODUCT_ID, productIDstr);
                    i.putExtra(KEY_CUSTOMER_ID, customer_ID);
                    startActivity(i);
                } else {
                    Toast.makeText(HeadphoneActivityDetail.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }

            }
        });

        AddToFavButton = (Button) findViewById(R.id.AddFavorites);
        AddToFavButton.setBackgroundResource(R.drawable.button_border);
        AddToFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    new AddToFavAsyncTask().execute();
                } else {
                    Toast.makeText(HeadphoneActivityDetail.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }

            }
        });
    }
    // Method to add products to cart
    public void getProductName(TextView productNameText) {
        name = productNameText.getText().toString();
        new getHeadphoneNameAsyncTask().execute();
    }
    private class AddToFavAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(HeadphoneActivityDetail.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            httpParams.put(KEY_CUSTOMER_ID, onlineCustomer.ID+"");
            httpParams.put(KEY_PRODUCT_ID, productIDstr);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "favorites/add_favorites_headphone.php", "GET", httpParams);
            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    goToFavorites();
                }
            });
        }

    }

    private void goToFavorites(){
        Intent i = new Intent(this, ShowFavorites.class);
        startActivity(i);
    }

    private class headphoneDetailAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display proggress bar
            pDialog = new ProgressDialog(HeadphoneActivityDetail.this);
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

                        new FetchReviewsAsyncTask().execute();

                        Intent i = getIntent();
                        //send result code 20 to notify about movie update
                        setResult(20, i);
                        //Finish ths activity and go back to listing activity
                        //finish();

                    } else {
                        Toast.makeText(HeadphoneActivityDetail.this,
                                "Some error occurred while logging in :(",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }


    }

    private class FetchReviewsAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(HeadphoneActivityDetail.this);
            pDialog.setMessage("Loading reviews. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            httpParams.put(KEY_PRODUCT_ID, productIDstr);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "reviews/show_reviews.php", "GET", httpParams);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONArray reviews;
                if (success == 1) {
                    reviewList = new ArrayList<>();
                    reviews = jsonObject.getJSONArray(KEY_DATA);

                    //Iterate through the response and populate phones list
                    for (int i = 0; i < reviews.length(); i++) {
                        JSONObject review = reviews.getJSONObject(i);
                        String reviewText = review.getString("review");
                        String reviewUserName = review.getString("email");
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(KEY_REVIEW_TEXT, reviewText);
                        map.put(KEY_REVIEW_USERNAME, reviewUserName);
                        reviewList.add(map);
                        Log.w("test", reviewList+"");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    populateReviewList();
                }
            });
        }

    }

    private class getHeadphoneNameAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(HeadphoneActivityDetail.this);
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
            httpParams.put(KEY_PRODUCT_ID, productIDstr);
            httpParams.put(KEY_CUSTOMER_ID, customer_ID+"");
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "carts/add_cart_headphone.php", "GET", httpParams);
            Log.w("AfterGettin", "git json");
            JSONObject data;
            try {
                Log.w("jsonObj", "" + jsonObject);
                success = jsonObject.getInt(KEY_SUCCESS);
                Log.w("AfterGettin", "git json");
                if (success == 1){
                    data = jsonObject.getJSONObject(KEY_DATA);
                    productID = data.getInt(KEY_PRODUCT_ID);
                }
                else{
                    Toast.makeText(HeadphoneActivityDetail.this,
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
                        Toast.makeText(HeadphoneActivityDetail.this,
                                "Done", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(),
                                ShoppingCartActivity.class);;
                        //send result code 20 to notify about movie update
                        setResult(20, i);
                        // Call addCartAsyncTask for send customer and product id to database
                        //Finish ths activity and go back to listing activity
                        finish();
                        startActivity(i);

                    } else {
                        Toast.makeText(HeadphoneActivityDetail.this,
                                "Some error occurred while adding user",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }

    private void populateReviewList() {
        Log.w("test", "test");
        ListAdapter adapter = new SimpleAdapter(
                HeadphoneActivityDetail.this, reviewList,
                R.layout.review, new String[]{KEY_REVIEW_USERNAME, KEY_REVIEW_TEXT},
                new int[]{R.id.reviewUserName, R.id.reviewText});
        // Updating ListView
        reviewListView.setAdapter(adapter);
    }
}
