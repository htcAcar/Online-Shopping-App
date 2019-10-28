package com.example.tba_osa_2019;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.tba_osa_2019.helper.CheckNetworkStatus;
import com.example.tba_osa_2019.helper.HttpJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private static String IP = "10.225.44.152";
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,16}" +               //at least 8 and most 16 characters
                    "$");

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_CUSTOMER_ID = "customer_ID";
    private static final String KEY_SELLER_ID = "seller_ID";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_DOB = "dob";
    private static final String KEY_ADDRESS = "address";
    private static final String BASE_URL = "http://"+ IP +"/tba-db/";
    private static String STRING_EMPTY = "";
    private EditText emailEditText;
    private EditText passwordEditText;
    private String email;
    private String password;
    private String address;
    private String dob;
    private Integer id;
    private Button loginButton;
    private Button forgotPasswordButton;
    private RadioGroup rgp;
    private RadioButton customerButtonView;
    private RadioButton sellerButtonView;
    private Boolean customerBool;
    private Boolean sellerBool;
    private int success;
    private ProgressDialog pDialog;
    public static Customer onlineCustomer ;
    public static Seller onlineSeller ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = (EditText) findViewById(R.id.emailEditText);
        emailEditText.setBackgroundResource(R.drawable.edittext_border);

        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        passwordEditText.setBackgroundResource(R.drawable.edittext_border);

        rgp = (RadioGroup) findViewById(R.id.radioGroup);
        customerButtonView =(RadioButton) findViewById(R.id.customerButton);
        sellerButtonView =(RadioButton) findViewById(R.id.sellerButton);

        loginButton = (Button) findViewById(R.id.LogButton);
        loginButton.setBackgroundResource(R.drawable.button_border);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    LogIn();
                } else {
                   /* Toast.makeText(LoginActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();
*/
                }

            }

        });

        forgotPasswordButton = (Button) findViewById(R.id.forgotPasswordButton);
        forgotPasswordButton.setBackgroundResource(R.drawable.button_border);
        forgotPasswordButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    email = emailEditText.getText().toString();
                    new ForgotPasswordAsyncTask().execute();
                } else {
                   /* Toast.makeText(LoginActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();
*/
                }

            }

        });
    }

    public void LogIn(){
        //Intent intent = new Intent(this, LoginActivity.class);
        //startActivity(intent);
        Log.w("user", "crash here");
        if (!STRING_EMPTY.equals(emailEditText.getText().toString()) &&
                !STRING_EMPTY.equals(passwordEditText.getText().toString()) && validateEmail() && validatePassword()) {

            email = emailEditText.getText().toString();Log.w("user", "crash here2");
            password = passwordEditText.getText().toString();Log.w("user", "crash here3");
            new LoginActivity.LoginAsyncTask().execute();
        } else {
            /*Toast.makeText(LoginActivity.this,
                    "One or more fields left empty!",
                    Toast.LENGTH_LONG).show();*/

        }
    }

    // E-mail validation method
    private boolean validateEmail() {
        String emailInput = emailEditText.getText().toString().trim();

        if (emailInput.isEmpty()) {
            emailEditText.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            emailEditText.setError("Please enter a valid email address");
            return false;
        } else {
            emailEditText.setError(null);
            return true;
        }
    }


    private boolean validatePassword() {
        String passwordInput = passwordEditText.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            passwordEditText.setError("Field can't be empty");
            return false;
        } else if (passwordInput.length() > 16 || passwordInput.length() < 8) {
            passwordEditText.setError("The password must be between 8 and 16 characters!");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            passwordEditText.setError("Please enter a valid password");
            return false;
        } else {
            passwordEditText.setError(null);
            return true;
        }
    }

    /**
     * AsyncTask for adding a movie
     */
    private class LoginAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display proggress bar
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Logging in. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            httpParams.put(KEY_EMAIL, email);
            httpParams.put(KEY_PASSWORD, password);
            JSONObject jsonObject = null;
            if (rgp.getCheckedRadioButtonId() == R.id.customerButton ){
                jsonObject = httpJsonParser.makeHttpRequest(
                        BASE_URL + "customers/login_customer.php", "GET", httpParams);
            }
            else if (rgp.getCheckedRadioButtonId() == R.id.sellerButton){
                jsonObject = httpJsonParser.makeHttpRequest(
                        BASE_URL + "sellers/login_seller.php", "GET", httpParams);
            }


            try {
                success = jsonObject.getInt(KEY_SUCCESS);
                JSONObject user;
                if (success == 1 && rgp.getCheckedRadioButtonId() == R.id.customerButton) {
                    //Parse the JSON response
                    user = jsonObject.getJSONObject(KEY_DATA);
                    email = user.getString(KEY_EMAIL);
                    password = user.getString(KEY_PASSWORD);
                    address = user.getString(KEY_ADDRESS);
                    id = user.getInt(KEY_CUSTOMER_ID);
                    dob = user.getString(KEY_DOB);

                    Log.w("user", ""+user);
                    onlineCustomer = new Customer (id, email, password,dob,address);
                    Log.w("online", onlineCustomer+"");

                }
                if(success == 1 && rgp.getCheckedRadioButtonId() == R.id.sellerButton){
                    user = jsonObject.getJSONObject(KEY_DATA);
                    email = user.getString(KEY_EMAIL);
                    password = user.getString(KEY_PASSWORD);
                    id = user.getInt(KEY_SELLER_ID);

                    Log.w("user", ""+user);
                    onlineSeller = new Seller (id, email, password);
                    Log.w("online", onlineSeller+"");
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
                        Toast.makeText(LoginActivity.this,
                                "Welcome :)", Toast.LENGTH_LONG).show();
                        Intent i = getIntent();
                        //send result code 20 to notify about movie update
                        setResult(20, i);
                        //Finish ths activity and go back to listing activity
                        if (rgp.getCheckedRadioButtonId() == R.id.customerButton){
                            goCustomerHomePage();
                        }
                        else if (rgp.getCheckedRadioButtonId() == R.id.sellerButton) {
                            goSellerHomePage();
                        }

                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Some error occurred while logging in :(",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }

    //Forgot Password Async Task
    private class ForgotPasswordAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display proggress bar
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            httpParams.put(KEY_EMAIL, email);
            JSONObject jsonObject;
            jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "customers/forgot_password.php", "GET", httpParams);
            try {
                success = jsonObject.getInt(KEY_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return email;
        }

        protected void onPostExecute(String result) {
            Log.w("AfterGettin", "Inside onPost");
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 1) {
                        //Display success message
                        Toast.makeText(LoginActivity.this,
                                "Your password has been sent to your mailbox", Toast.LENGTH_LONG).show();
                        /*
                        Intent i = getIntent();
                        //send result code 20 to notify about movie update
                        setResult(20, i);
                        startActivity(i);
                        */
                        //Finish ths activity and go back to listing activity
                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Some error occurred while logging in :(",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }


    public void goCustomerHomePage(){
        // Method to go user's profile
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
    }

    public void goSellerHomePage(){
        // Method to go user's profile
        Intent intent = new Intent(this, SellerHomePageActivity.class);
        startActivity(intent);
    }

}

