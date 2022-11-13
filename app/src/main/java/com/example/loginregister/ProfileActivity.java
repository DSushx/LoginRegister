package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class ProfileActivity extends AppCompatActivity {
    TextInputEditText textInputEditTextUsername1;
    TextInputEditText textInputEditTextPassword1;
    TextInputEditText textInputEditTextEmail1;
    TextInputEditText textInputEditTextHeight1;
    Button buttonsave;
    RadioGroup radioGroup1;
    RadioButton selectedRadioButton1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        textInputEditTextUsername1 = findViewById(R.id.username1);
        textInputEditTextPassword1 = findViewById(R.id.password1);
        textInputEditTextEmail1 = findViewById(R.id.email1);
        textInputEditTextHeight1 = findViewById(R.id.height1);
        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        buttonsave = findViewById(R.id.buttonsave);
        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username,password,email,height,exercise ;
                //取得輸入的資料
                username = String.valueOf(textInputEditTextUsername1.getText());
                password = String.valueOf(textInputEditTextPassword1.getText());
                email = String.valueOf(textInputEditTextEmail1.getText());
                height = String.valueOf(textInputEditTextHeight1.getText());


                selectedRadioButton1  = (RadioButton)findViewById(radioGroup1.getCheckedRadioButtonId());
                exercise = selectedRadioButton1.getText().toString();
                if(!username.equals("")&&!password.equals("")&&!email.equals("")&&!height.equals("")&&!exercise.equals("")) {

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[8];
                            field[0] = "fullname";
                            field[1] = "username";
                            field[2] = "password";
                            field[3] = "email";
                            field[4] = "height";
                            field[5] = "weight";
                            field[6] = "birthday";
                            field[7] = "gender";
                            //Creating array for data
                            String[] data = new String[8];

                            data[1] = username;
                            data[2] = password;
                            data[3] = email;
                            data[4] = height;

                            PutData putData = new PutData("http://10.0.2.2/LoginRegister/signup.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {

                                    String result = putData.getResult();
                                    if (result.equals("Sign Up Success")){
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(),Login.class);
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
}