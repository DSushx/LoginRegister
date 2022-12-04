package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.loginregister.home.HomeActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Login extends AppCompatActivity {

    TextInputEditText textInputEditTextUsername,textInputEditTextPassword;
    Button buttonLogin;
    TextView textViewSignUp;
    ProgressBar progressBar;
    int count=0;

    TextView textView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textView = (TextView)findViewById(R.id.textview);
        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        Python py =Python.getInstance();
        PyObject pyobj =py.getModule("myscript");
        PyObject obj =pyobj.callAttr("main");
        textView.setText(obj.toString());



        textInputEditTextUsername= findViewById(R.id.username);
        textInputEditTextPassword= findViewById(R.id.password);
        buttonLogin= findViewById(R.id.buttonLogin);
        textViewSignUp= findViewById(R.id.signUpText);
        progressBar = findViewById(R.id.progress);
        //openFragment();

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignUp.class);
                startActivity(intent);
                finish();
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username,password;
                username = String.valueOf(textInputEditTextUsername.getText());
                password = String.valueOf(textInputEditTextPassword.getText());

                if(!username.equals("")&&!password.equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field =  new String[2];
                            field[0] = "username";
                            field[1] = "password";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = username;
                            data[1] = password;
                            PutData putData = new PutData("http://192.168.1.116/LoginRegister/login.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if (result.equals("Login Success!")){

                                        new Thread(() -> {
                                            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("username", username); // Storing string
                                            editor.apply(); // commit changes
                                        }).start();

                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();

                                    }
                                    Log.i("PutData", result);
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(),"All fields required",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void openFragment(){
    Bundle bundle =new Bundle();
    bundle.putString("count",String.valueOf(count));
    }
   /* public void sava(View v){
        pref = getSharedPreferences("DATA",MODE_PRIVATE);
        pref.edit()
                .putString("NAME",textInputEditTextUsername.getText().toString())
                .putString("Password",textInputEditTextPassword.getText().toString())
                .apply();                   //或commit()
    }
    //讀取資料
    public void read(View v){
        pref = getSharedPreferences("DATA",MODE_PRIVATE);
        textInputEditTextUsername.setText(pref.getString("NAME",""));
        textInputEditTextPassword.setText(pref.getString("Password",""));
    }/*

    */
}