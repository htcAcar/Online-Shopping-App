package com.example.tba_osa_2019;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.tba_osa_2019.helper.CheckNetworkStatus;
import com.example.tba_osa_2019.helper.HttpJsonParser;


public class RegistrationActivity extends LoginActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,16}" +               //at least 8 and most 16 characters
                    "$");

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_DOB = "dob";
    private static final String KEY_ADDRESS = "address";
    private static final String BASE_URL = "http://10.225.44.152/tba-db/";
    private static String STRING_EMPTY = "";
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText dobEditText;
    private EditText addressEditText;
    private String email;
    private String password;
    private String dob;
    private String address;
    private Button registerButton;
    private int success;
    private ProgressDialog pDialog;

    private Pattern pattern;
    private Matcher matcher;

    private static final String DATE_PATTERN= "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        pattern = Pattern.compile(DATE_PATTERN);

        emailEditText = (EditText) findViewById(R.id.txtEmail);
        passwordEditText = (EditText) findViewById(R.id.txtPassword);
        dobEditText = (EditText) findViewById(R.id.txtDob);
        addressEditText = (EditText) findViewById(R.id.txtAddress);
        registerButton = (Button) findViewById(R.id.btnRegister);
        registerButton.setBackgroundResource(R.drawable.button_border);
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext()) && validateEmail() && validatePassword() && validateDate(dobEditText.getText().toString())) {
                    AddCustomer();
                } else {
                    Toast.makeText(RegistrationActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }

            }

        });
    }

    public void AddCustomer(){
        //Intent intent = new Intent(this, LoginActivity.class);
        //startActivity(intent);

        if (!STRING_EMPTY.equals(emailEditText.getText().toString()) &&
                !STRING_EMPTY.equals(passwordEditText.getText().toString()) &&
                !STRING_EMPTY.equals(dobEditText.getText().toString()) &&
                !STRING_EMPTY.equals(addressEditText.getText().toString())) {

            email = emailEditText.getText().toString();
            password = passwordEditText.getText().toString();
            dob = dobEditText.getText().toString();
            address = addressEditText.getText().toString();
            new AddCustomerAsyncTask().execute();
        } else {
            Toast.makeText(RegistrationActivity.this,
                    "One or more fields left empty!",
                    Toast.LENGTH_LONG).show();

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

    public boolean validateDate(final String date){

        matcher = pattern.matcher(date);

        if(matcher.matches()){

            matcher.reset();

            if(matcher.find()){

                String day = matcher.group(1);
                String month = matcher.group(2);
                int year = Integer.parseInt(matcher.group(3));

                if (day.equals("31") &&
                        (month.equals("4") || month .equals("6") || month.equals("9") ||
                                month.equals("11") || month.equals("04") || month .equals("06") ||
                                month.equals("09"))) {
                    dobEditText.setError("Enter a correct date format");
                    return false; // only 1,3,5,7,8,10,12 has 31 days
                } else if (month.equals("2") || month.equals("02")) {
                    //leap year
                    if(year % 4==0){
                        if(day.equals("30") || day.equals("31")){
                            dobEditText.setError("Enter a correct date format");
                            return false;
                        }else{
                            return true;
                        }
                    }else{
                        if(day.equals("29")||day.equals("30")||day.equals("31")){
                            dobEditText.setError("Enter a correct date format");
                            return false;
                        }else{
                            return true;
                        }
                    }
                }else{
                    return true;
                }
            }else{
                dobEditText.setError("Enter a correct date format");
                return false;
            }
        }else{
            dobEditText.setError("Enter a correct date format");
            return false;
        }
    }

    /**
     * AsyncTask for adding a movie
     */
    private class AddCustomerAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display proggress bar
            pDialog = new ProgressDialog(RegistrationActivity.this);
            pDialog.setMessage("Adding Customer. Please wait...");
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
            httpParams.put(KEY_DOB, dob);
            httpParams.put(KEY_ADDRESS, address);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "customers/add_customer.php", "GET", httpParams);
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
                        Toast.makeText(RegistrationActivity.this,
                                "Customer Added", Toast.LENGTH_LONG).show();
                        Intent i = getIntent();
                        //send result code 20 to notify about movie update
                        setResult(20, i);
                        //Finish ths activity and go back to listing activity

                        finish();

                    } else {
                        Toast.makeText(RegistrationActivity.this,
                                "Some error occurred while adding user",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }

}
