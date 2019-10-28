
package com.example.tba_osa_2019;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
import java.util.regex.Pattern;


public class ProfileActivity extends LoginActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,16}" +               //at least 8 and most 16 characters
                    "$");

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_DATA = "data";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_CUSTOMER_ID = "customer_ID";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_DOB = "dob";
    private static final String KEY_ADDRESS = "address";
    private static final String BASE_URL = "http://10.225.44.152/tba-db/";
    private static String STRING_EMPTY = "";
    private int success;
    private ProgressDialog pDialog;
    private Integer customerID;

    private Button cart;
    private Button orders;
    private Button changeAddress;
    private Button changePass;
    private Button sellerbtn;
    private TextView email;

    private Button updateProfile;
    private Button deleteAccountButton;
    private Button viewFavorites;

    private TextView Address;
    private TextView dateOfBirth;
    private TextView password1;

    private EditText mailedit;
    private EditText dobedit;
    private EditText addredit;
    private EditText passwordedit;


    private  String e_mail;
    private  String birth;
    private  String home;
    private String password;
    private int customer_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //set textViews
        email = (TextView) findViewById(R.id.emailTextView);
        Address = findViewById(R.id.AddressTextView);
        dateOfBirth = findViewById(R.id.dobTextView);
        password1 = findViewById(R.id.textView2);

        //set editTexts
        mailedit = findViewById(R.id.emailEditText);
        dobedit = findViewById(R.id.dobEditText);
        addredit = findViewById(R.id.AddressEditText);
        passwordedit = findViewById(R.id.passwordEditText);
        new profileAsyncTask().execute();
        deleteAccountButton = (Button) findViewById(R.id.deleteButton);
        deleteAccountButton.setBackgroundResource(R.drawable.button_border);
        deleteAccountButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    new deleteAsyncTask().execute();
                } else {
                    Toast.makeText(ProfileActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }

            }


        });


        viewFavorites = (Button) findViewById(R.id.favoritesButtonView);
        viewFavorites.setBackgroundResource(R.drawable.button_border);
        viewFavorites.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    Intent intent = new Intent(getApplicationContext(), ShowFavorites.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ProfileActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }

            }


        });


        updateProfile = (Button) findViewById(R.id.updateButton);
        updateProfile.setBackgroundResource(R.drawable.button_border);
        updateProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext()) && validateEmail()) {
                    Update();
                } else {
                    Toast.makeText(ProfileActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }

            }

        });

        sellerbtn = (Button) findViewById(R.id.sellerButton);
        sellerbtn.setBackgroundResource(R.drawable.button_border);
        sellerbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    becomeSeller();
                } else {
                    Toast.makeText(ProfileActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }

            }

        });

    }

    public void becomeSeller(){

        new sellerAsyncTask().execute();

    }



    public void Update(){
        e_mail = mailedit.getText().toString();
        password = passwordedit.getText().toString();
        birth = dobedit.getText().toString();
        home = addredit.getText().toString();
        new profileUpdateAsyncTask().execute();
    }

    // E-mail validation method
    private boolean validateEmail() {
        String emailInput = mailedit.getText().toString().trim();

        if (emailInput.isEmpty()) {
            mailedit.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            mailedit.setError("Please enter a valid email address");
            return false;
        } else {
            mailedit.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = passwordedit.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            passwordedit.setError("Field can't be empty");
            return false;
        } else if (passwordInput.length() > 16 || passwordInput.length() < 8) {
            passwordedit.setError("The password must be between 8 and 16 characters!");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            passwordedit.setError("Please enter a valid password");
            return false;
        } else {
            passwordedit.setError(null);
            return true;
        }
    }

    private class deleteAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display proggress bar
            pDialog = new ProgressDialog(ProfileActivity.this);
            pDialog.setMessage("Deleting Account. Please wait...");
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
                    BASE_URL + "customers/delete_customer.php", "GET", httpParams);
            Log.w("AfterGettin", "git json");
            try {
                Log.w("jsonObj", ""+jsonObject);
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
                        Toast.makeText(ProfileActivity.this,
                                "Customer Deleted", Toast.LENGTH_LONG).show();
                        Intent i = getIntent();
                        //send result code 20 to notify about movie update
                        setResult(20, i);
                        //Finish ths activity and go back to listing activity

                        finish();
                        Intent I = new Intent(getApplicationContext(),
                                MainActivity.class);
                        startActivity(I);

                    } else {
                        Toast.makeText(ProfileActivity.this,
                                "Some error occurred while adding user",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }

    private class profileAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display proggress bar
            pDialog = new ProgressDialog(ProfileActivity.this);
            pDialog.setMessage("Loading. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            customer_ID = onlineCustomer.ID;
            httpParams.put(KEY_CUSTOMER_ID, customer_ID+"");
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "customers/get_customer_details.php", "GET", httpParams);

            try {
                success = jsonObject.getInt(KEY_SUCCESS);

                JSONObject user;

                if (success == 1) {
                    user = jsonObject.getJSONObject(KEY_DATA);

                    customer_ID = user.getInt(KEY_CUSTOMER_ID);
                    Log.w("customer1",customer_ID+"" );
                    password = user.getString(KEY_PASSWORD);
                    Log.w("customer1",password );
                    e_mail = user.getString(KEY_EMAIL);
                    Log.w("products.length()2",e_mail+"" );
                    birth = user.getString(KEY_DOB);
                    Log.w("products.length()3",birth+"" );
                    home = user.getString(KEY_ADDRESS);
                    Log.w("products.length()6",home+"" );




                    //Parse the JSON response
                   /*
                    product_name = user.getString(KEY_PRODUCT_NAME);
                    product_brand = user.getString(KEY_PRODUCT_BRAND);
                    product_price = user.getInt(KEY_PRODUCT_PRICE);*/

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
                        mailedit.setText(e_mail);
                        passwordedit.setText(password);
                        dobedit.setText(birth);
                        addredit.setText(home);
                        Intent i = getIntent();
                        //send result code 20 to notify about movie update
                        setResult(20, i);
                        //Finish ths activity and go back to listing activity
                        //finish();

                    } else {
                        Toast.makeText(ProfileActivity.this,
                                "Some error occurred while logging in :(",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }


    }

    private class profileUpdateAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display proggress bar
            pDialog = new ProgressDialog(ProfileActivity.this);
            pDialog.setMessage("Loading. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            customer_ID = onlineCustomer.ID;
            httpParams.put(KEY_CUSTOMER_ID, customer_ID+"");
            httpParams.put(KEY_PASSWORD, password);
            httpParams.put(KEY_ADDRESS, home);
            httpParams.put(KEY_DOB, birth);
            httpParams.put(KEY_EMAIL, e_mail);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "customers/update_customer_details.php", "GET", httpParams);

            try {
                Log.w("jsonObj", ""+jsonObject);
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

                        Intent i = getIntent();
                        //send result code 20 to notify about movie update
                        setResult(20, i);
                        //Finish ths activity and go back to listing activity
                        //
                        //goHomePage();

                    } else {
                        Toast.makeText(ProfileActivity.this,
                                "Some error occurred while logging in :(",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }


    }

    public void goCart(){
        Intent intent = new Intent(this, ShoppingCartActivity.class);
        startActivity(intent);
    }

    private class sellerAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display proggress bar
            pDialog = new ProgressDialog(ProfileActivity.this);
            pDialog.setMessage("Loading. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            customer_ID = onlineCustomer.ID;
            password = onlineCustomer.password;
            e_mail = onlineCustomer.email;
            httpParams.put(KEY_CUSTOMER_ID, customer_ID+"");
            httpParams.put(KEY_EMAIL, e_mail+"");
            httpParams.put(KEY_PASSWORD, password+"");

            String message= "";

            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "customers/become_seller.php", "GET", httpParams);

            try {
                success = jsonObject.getInt(KEY_SUCCESS);

                message = jsonObject.getString(KEY_MESSAGE);

                Log.w("products.length()2",e_mail+"" );

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return message;
        }

        protected void onPostExecute(final String result) {
            Log.w("AfterGettin", "Inside onPost");
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(ProfileActivity.this,
                            result,
                            Toast.LENGTH_LONG).show();
                }
            });
        }


    }

}