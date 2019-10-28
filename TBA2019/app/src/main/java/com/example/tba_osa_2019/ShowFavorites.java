package com.example.tba_osa_2019;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

public class ShowFavorites extends AppCompatActivity {

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_PRODUCT_ID = "product_ID";
    private static final String KEY_CUSTOMER_ID = "customer_ID";
    private static final String KEY_PRODUCT_NAME = "name";
    private static final String KEY_PRODUCT_PRICE = "price";
    private static final String BASE_URL = "http://10.225.44.152/tba-db/";

    private String phoneId;

    private ArrayList<HashMap<String, String>> itemList;
    private ListView itemListView;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_favorites);

        itemListView = (ListView) findViewById(R.id.favListView);
        new ShowFavorites.ShowFavsAsyncTask().execute();
    }

    private class ShowFavsAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(ShowFavorites.this);
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
                    BASE_URL + "favorites/show_favorites.php", "GET", httpParams);
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
                    populateItemList();
                }
            });
        }

    }

    private void populateItemList() {
        Log.w("test", "test");
        ListAdapter adapter = new SimpleAdapter(
                ShowFavorites.this, itemList,
                R.layout.phone, new String[]{KEY_PRODUCT_ID,
                KEY_PRODUCT_NAME, KEY_PRODUCT_PRICE},
                new int[]{R.id.phoneId, R.id.phoneName, R.id.phonePrice});
        // Updating ListView
        itemListView.setAdapter(adapter);
        Log.w("test", "test");
        itemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Check for network connectivity
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    phoneId = ((TextView) view.findViewById(R.id.phoneId))
                            .getText().toString();
                    AlertDialog.Builder altdial = new AlertDialog.Builder(ShowFavorites.this);
                    altdial.setMessage("Do you want to remove this item from your favorites?").setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new RemoveFavAsyncTask().execute();
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
                    Toast.makeText(ShowFavorites.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }
                return true;
            }
        });
    }

    private class RemoveFavAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(ShowFavorites.this);
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
            httpParams.put(KEY_PRODUCT_ID, phoneId);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "favorites/remove_from_favorites.php", "GET", httpParams);
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
}
