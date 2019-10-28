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

import com.example.tba_osa_2019.helper.CheckNetworkStatus;
import com.example.tba_osa_2019.helper.HttpJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.tba_osa_2019.LoginActivity.onlineCustomer;

public class AddCommentToProduct extends AppCompatActivity {

    private static final String KEY_SUCCESS = "success";
    private static String KEY_CUSTOMER_ID = "customer_ID";
    private static final String KEY_PRODUCT_ID = "product_ID";
    private static final String KEY_REVIEW = "review";

    private static final String BASE_URL = "http://10.225.44.152/tba-db/";

    private ProgressDialog pDialog;
    private int success;

    private Button AddCommentButton2;
    private EditText commentText;

    private String productIDstr;
    private String comment;

    private Integer customer_ID = onlineCustomer.ID;
    private String customer_email = onlineCustomer.email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment_to_product);

        Intent intent = getIntent();
        productIDstr = intent.getStringExtra(KEY_PRODUCT_ID);

        commentText = (EditText) findViewById(R.id.commentEditText);

        AddCommentButton2 = (Button) findViewById(R.id.commentButton);
        AddCommentButton2.setBackgroundResource(R.drawable.button_border);
        AddCommentButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment = commentText.getText().toString();
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    // go to add comment activity
                    Intent i = new Intent(getApplicationContext(),
                            AddCommentToProduct.class);
                    new AddCommentAsyncTask().execute();
                } else {
                    Toast.makeText(AddCommentToProduct.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    private class AddCommentAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display proggress bar
            pDialog = new ProgressDialog(AddCommentToProduct.this);
            pDialog.setMessage("Loading. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            Log.w("AfterGettin", "product_id" + productIDstr);
            Log.w("AfterGettin", "comment" + comment);
            httpParams.put(KEY_PRODUCT_ID, productIDstr);
            httpParams.put("email", customer_email);
            httpParams.put(KEY_REVIEW, comment);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "reviews/add_review.php", "GET", httpParams);

            try {
                success = jsonObject.getInt(KEY_SUCCESS);
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
                        finish();
                    } else {
                        Toast.makeText(AddCommentToProduct.this,
                                "Some error occurred",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }

    }
}