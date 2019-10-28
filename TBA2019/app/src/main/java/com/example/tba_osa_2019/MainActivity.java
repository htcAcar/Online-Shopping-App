package com.example.tba_osa_2019;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tba_osa_2019.helper.CheckNetworkStatus;


public class MainActivity extends AppCompatActivity {

    private Button registerButton;
    private Button loginButton;
    private Button guestButton;

    private TextView tbaHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerButton = (Button) findViewById(R.id.RegisterButton);
        registerButton.setBackgroundResource(R.drawable.button_border);
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
                public void onClick(View view) {
                    //Check for network connectivity
                    if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                        Intent i = new Intent(getApplicationContext(),
                                RegistrationActivity.class);
                        startActivity(i);
                    } else {
                        //Display error message if not connected to internet
                        Toast.makeText(MainActivity.this,
                                "Unable to connect to internet",
                                Toast.LENGTH_LONG).show();

                    }

                }
            });


        loginButton = (Button) findViewById(R.id.LoginButton);
        loginButton.setBackgroundResource(R.drawable.button_border);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Check for network connectivity
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    Intent i = new Intent(getApplicationContext(),
                            LoginActivity.class);
                    startActivity(i);
                } else {
                    //Display error message if not connected to internet
                    Toast.makeText(MainActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }

            }

        });

        guestButton = (Button) findViewById(R.id.guestButton);
        guestButton.setBackgroundResource(R.drawable.button_border);
        guestButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Check for network connectivity
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    Intent i = new Intent(getApplicationContext(),
                            GuestHomePage.class);
                    startActivity(i);
                } else {
                    //Display error message if not connected to internet
                    Toast.makeText(MainActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }

            }

        });

        TextView tx = (TextView)findViewById(R.id.tbaHeader);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Pacifico.ttf");

        tx.setTypeface(custom_font);
    }

    public void test(){

    }

    /*
    public void openRegistration(){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void openLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }*/
}
