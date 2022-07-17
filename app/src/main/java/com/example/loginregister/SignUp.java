package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.Calendar;

public class SignUp extends AppCompatActivity {
    TextInputEditText textInputEditTextFullname;
    TextInputEditText textInputEditTextUsername;
    TextInputEditText textInputEditTextPassword;
    TextInputEditText textInputEditTextEmail;
    TextInputEditText textInputEditTextHeight;
    TextInputEditText textInputEditTextWeight;
    Button buttonSignUp;
    TextView textViewLogin;
    ProgressBar progressBar;
    RadioGroup radioGroup;
    RadioButton selectedRadioButton;
    private EditText applydate = null;
    private int mYear, mMonth, mDay;
    String bDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        textInputEditTextFullname = findViewById(R.id.fullname);
        textInputEditTextUsername = findViewById(R.id.username);
        textInputEditTextPassword = findViewById(R.id.password);
        textInputEditTextEmail = findViewById(R.id.email);
        textInputEditTextHeight = findViewById(R.id.height);
        textInputEditTextWeight = findViewById(R.id.weight);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        applydate = findViewById(R.id.birthday);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        textViewLogin = findViewById(R.id.loginText);
        progressBar = findViewById(R.id.progress);

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname,username,password,email,height,weight,birthday,gender;
                fullname = String.valueOf(textInputEditTextFullname.getText());  //取得輸入的資料
                username = String.valueOf(textInputEditTextUsername.getText());
                password = String.valueOf(textInputEditTextPassword.getText());
                email = String.valueOf(textInputEditTextEmail.getText());
                height = String.valueOf(textInputEditTextHeight.getText());
                weight = String.valueOf(textInputEditTextWeight.getText());
                birthday = bDate;
                selectedRadioButton  = (RadioButton)findViewById(radioGroup.getCheckedRadioButtonId());
                gender = selectedRadioButton.getText().toString();
                if(!fullname.equals("")&&!username.equals("")&&!password.equals("")&&!email.equals("")&&!height.equals("")&&!weight.equals("")&&!birthday.equals("")&&!gender.equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
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
                            data[0] = fullname;
                            data[1] = username;
                            data[2] = password;
                            data[3] = email;
                            data[4] = height;
                            data[5] = weight;
                            data[6] = birthday;
                            data[7] = gender;
                            PutData putData = new PutData("http://10.1.1.14/LoginRegister/signup.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
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

    //選日期
    public void datePicker(View v) {
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);      //取得現在的日期年月日
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(v.getContext(), (view, year, month, day) -> {
            String datetime = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(day);
            applydate.setText(datetime);   //取得選定的日期指定給日期編輯框
            bDate=datetime;  //回傳選定的日期
        }, mYear, mMonth, mDay).show();  //顯示日曆
    }
}