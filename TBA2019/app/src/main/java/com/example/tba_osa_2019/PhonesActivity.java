package com.example.tba_osa_2019;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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


public class PhonesActivity extends AppCompatActivity {

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_PRODUCT_ID = "product_ID";
    private static final String KEY_PRODUCT_NAME = "name";
    private static final String KEY_PRODUCT_PRICE = "price";
    private static final String BASE_URL = "http://10.225.44.152/tba-db/";

    private ArrayList<HashMap<String, String>> phoneList;
    private ListView phoneListView;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phones);
        phoneListView = (ListView) findViewById(R.id.phoneList);
        new FetchMoviesAsyncTask().execute();
    }

    private class FetchMoviesAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(PhonesActivity.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "products/phone/show_phones.php", "GET", null);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONArray phones;
                if (success == 1) {
                    phoneList = new ArrayList<>();
                    phones = jsonObject.getJSONArray(KEY_DATA);
                    //Iterate through the response and populate phones list
                    for (int i = 0; i < phones.length(); i++) {
                        JSONObject phone = phones.getJSONObject(i);
                        Integer phoneID = phone.getInt(KEY_PRODUCT_ID);
                        String phoneName = phone.getString(KEY_PRODUCT_NAME);
                        Integer phonePrice = phone.getInt(KEY_PRODUCT_PRICE);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(KEY_PRODUCT_ID, phoneID.toString());
                        map.put(KEY_PRODUCT_NAME, phoneName);
                        map.put(KEY_PRODUCT_PRICE, phonePrice.toString());
                        phoneList.add(map);
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

    /**
     * Updating parsed JSON data into ListView
     * */
    private void populatePhoneList() {
        ListAdapter adapter = new SimpleAdapter(
                PhonesActivity.this, phoneList,
                R.layout.phone, new String[]{KEY_PRODUCT_ID,
                KEY_PRODUCT_NAME, KEY_PRODUCT_PRICE},
                new int[]{R.id.phoneId, R.id.phoneName, R.id.phonePrice});
        // Updating ListView
        phoneListView.setAdapter(adapter);

        //Will call PhoneDetailsActivity when a phone is clicked
        phoneListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Check for network connectivity
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    String phoneId = ((TextView) view.findViewById(R.id.phoneId))
                            .getText().toString();
                    Intent intent = new Intent(getApplicationContext(),
                            PhoneActivityDetail.class);
                    intent.putExtra(KEY_PRODUCT_ID, phoneId);
                    startActivityForResult(intent, 20);

                } else {
                    Toast.makeText(PhonesActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }


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
}



