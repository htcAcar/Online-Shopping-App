package com.example.tba_osa_2019;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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


public class PhoneActivityDetail extends AppCompatActivity {

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static String KEY_CUSTOMER_ID = "customer_ID";
    private static final String KEY_PRODUCT_ID = "product_ID";
    private static final String KEY_NAME = "name";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_COLOR = "color";
    private static final String KEY_SCREEN_SIZE = "screen_size";
    private static final String KEY_MEMORY = "memory";
    private static final String KEY_CAMERA = "camera";
    private static final String KEY_PRICE = "price";
    private static final String KEY_REVIEW_TEXT = "review";
    private static final String KEY_REVIEW_USERNAME = "customer_ID";

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

    private Button AddCartBtn3;
    private Button AddCommentButton;
    private Button AddToFavButton;

    private String brand;
    private String name;
    private String color;
    private String memory;
    private String screenSize;
    private String camera;
    private String price;

    private int productID;
    private String productIDstr;

    private Integer customer_ID = onlineCustomer.ID;

    private ArrayList<HashMap<String, String>> reviewList;
    private ListView reviewListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_detail);

        Intent intent = getIntent();
        Brandname = findViewById(R.id.phoneBrandText);
        phonename = findViewById(R.id.phoneNameText);
        Colorname = findViewById(R.id.phoneColorText);
        Screenname = findViewById(R.id.phoneScreenText);
        Memoryname = findViewById(R.id.phoneMemoryText);
        Cameraname = findViewById(R.id.phoneCameraText);
        pricename = findViewById(R.id.phonePriceText);
        reviewListView = (ListView) findViewById(R.id.reviewListScrollView);
        productIDstr = intent.getStringExtra(KEY_PRODUCT_ID);

        new phoneDetailAsyncTask().execute();

        AddCartBtn3 = (Button) findViewById(R.id.AddCartPhone);
        AddCartBtn3.setBackgroundResource(R.drawable.button_border);
        AddCartBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    getProductName(phonename);
                } else {
                    Toast.makeText(PhoneActivityDetail.this,
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
                    Toast.makeText(PhoneActivityDetail.this,
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
                    Toast.makeText(PhoneActivityDetail.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    // Method to add products to cart
    public void getProductName(TextView productNameText) {
        name = productNameText.getText().toString();
        new getPhoneNameAsyncTask().execute();
    }

    private class AddToFavAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(PhoneActivityDetail.this);
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
                    BASE_URL + "favorites/add_favorites_phone.php", "GET", httpParams);
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

    private class phoneDetailAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display proggress bar
            pDialog = new ProgressDialog(PhoneActivityDetail.this);
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
                        Brandname.setText(brand);
                        phonename.setText(name);
                        Colorname.setText(color);
                        Screenname.setText(screenSize);
                        Memoryname.setText(memory);
                        Cameraname.setText(camera);
                        pricename.setText(price);

                        new FetchReviewsAsyncTask().execute();

                        Intent i = getIntent();
                        //send result code 20 to notify about movie update
                        setResult(20, i);
                        //Finish ths activity and go back to listing activity
                        //finish();

                    } else {
                        Toast.makeText(PhoneActivityDetail.this,
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
            pDialog = new ProgressDialog(PhoneActivityDetail.this);
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

    private class getPhoneNameAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(PhoneActivityDetail.this);
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
                    BASE_URL + "carts/add_cart_phone.php", "GET", httpParams);
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
                    Toast.makeText(PhoneActivityDetail.this,
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
                        Toast.makeText(PhoneActivityDetail.this,
                                "Done", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(),
                                ShoppingCartActivity.class);
                        //send result code 20 to notify about movie update
                        setResult(20, i);
                        // Call addCartAsyncTask for send customer and product id to database
                        //Finish ths activity and go back to listing activity
                        finish();
                        startActivity(i);

                    } else {
                        Toast.makeText(PhoneActivityDetail.this,
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
                PhoneActivityDetail.this, reviewList,
                R.layout.review, new String[]{KEY_REVIEW_USERNAME, KEY_REVIEW_TEXT},
                new int[]{R.id.reviewUserName, R.id.reviewText});
        // Updating ListView
        reviewListView.setAdapter(adapter);
    }



}
