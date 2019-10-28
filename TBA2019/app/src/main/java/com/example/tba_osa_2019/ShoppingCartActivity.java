package com.example.tba_osa_2019;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCartActivity extends LoginActivity {

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_PRODUCT_ID = "product_ID";
    private static final String KEY_CUSTOMER_ID = "customer_ID";
    private static final String KEY_PRODUCT_NAME = "name";
    private static final String KEY_PRODUCT_PRICE = "price";
    private static final String BASE_URL = "http://10.225.44.152/tba-db/";

    private ArrayList<HashMap<String, String>> itemList;
    private ListView itemListView;
    private ProgressDialog pDialog;

    private String phoneID;

    private Button goProfileFromCart;
    private Button removeItemsFromCart;
    private Button buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        itemListView = (ListView) findViewById(R.id.itemListDyn);
        new ShoppingCartActivity.FetchItemsAsyncTask().execute();

        goProfileFromCart = (Button) findViewById(R.id.goToProfileButton);
        goProfileFromCart.setBackgroundResource(R.drawable.button_border);
        goProfileFromCart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goProfile();
            }
        });

        removeItemsFromCart = (Button) findViewById(R.id.removeItemsButton);
        removeItemsFromCart.setBackgroundResource(R.drawable.button_border);
        removeItemsFromCart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new ShoppingCartActivity.RemoveItemsAsyncTask().execute();
            }
        });

        buy = (Button) findViewById(R.id.buyButton);
        buy.setBackgroundResource(R.drawable.button_border);
        buy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new ShoppingCartActivity.BuyAsyncTask().execute();
            }
        });

    }

    private class FetchItemsAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(ShoppingCartActivity.this);
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
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "carts/fetch_all_cartItems.php", "GET", httpParams);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONArray items;
                if (success == 1) {
                    itemList = new ArrayList<>();
                    items = jsonObject.getJSONArray(KEY_DATA);

                    //Iterate through the response and populate phones list
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        Integer itemID = item.getInt(KEY_PRODUCT_ID);
                        String itemName = item.getString(KEY_PRODUCT_NAME);
                        Integer itemPrice = item.getInt(KEY_PRODUCT_PRICE);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(KEY_PRODUCT_ID, itemID.toString());
                        map.put(KEY_PRODUCT_NAME, itemName);
                        map.put(KEY_PRODUCT_PRICE, itemPrice.toString());
                        itemList.add(map);
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
                    populatePhoneList();
                }
            });
        }

    }

    private class RemoveOneItemAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(ShoppingCartActivity.this);
            //pDialog.setMessage("Loading products. Please wait...");
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
            httpParams.put(KEY_PRODUCT_ID, phoneID);

            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "carts/remove_fromCart.php", "GET", httpParams);

            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(ShoppingCartActivity.this,
                            "Item successfully deleted from your cart", Toast.LENGTH_LONG).show();
                    goHomePage();
                }
            });
        }

    }

    private class RemoveItemsAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(ShoppingCartActivity.this);
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
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "carts/remove_all.php", "GET", httpParams);

            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(ShoppingCartActivity.this,
                            "Cart successfully cleaned", Toast.LENGTH_LONG).show();
                    goHomePage();
                }
            });
        }

    }

    private class BuyAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(ShoppingCartActivity.this);
            pDialog.setMessage("Processing purchase. Please wait...");
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
            httpParams.put("email", onlineCustomer.email);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "carts/buy.php", "GET", httpParams);

            return null;
        }

        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(ShoppingCartActivity.this,
                            "Items successfully purchased", Toast.LENGTH_LONG).show();
                    goHomePage();
                }
            });
        }

    }


    /**
     * Updating parsed JSON data into ListView
     * */
    private void populatePhoneList() {
        Log.w("test", "test");
        ListAdapter adapter = new SimpleAdapter(
                ShoppingCartActivity.this, itemList,
                R.layout.phone, new String[]{KEY_PRODUCT_ID,
                KEY_PRODUCT_NAME, KEY_PRODUCT_PRICE},
                new int[]{R.id.phoneId, R.id.phoneName, R.id.phonePrice});
        // Updating ListView
        itemListView.setAdapter(adapter);
        Log.w("test", "test");
        //Will call PhoneDetailsActivity when a phone is clicked
        itemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Check for network connectivity
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    phoneID = ((TextView) view.findViewById(R.id.phoneId))
                            .getText().toString();
                    Log.w("test", phoneID);
                    AlertDialog.Builder altdial = new AlertDialog.Builder(ShoppingCartActivity.this);
                    altdial.setMessage("Do you want to remove this item from your cart?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new RemoveOneItemAsyncTask().execute();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = altdial.create();
                    alert.show();


                } else {
                    Toast.makeText(ShoppingCartActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }
                return true;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 20) {
            // If the result code is 20 that means that
            // the user has deleted/updated the movie.
            // So refresh the movie listing
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    public void goProfile(){
            // Method to go user's profile
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
    }

    public void goHomePage(){
        // Method to go user's profile
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }

}


