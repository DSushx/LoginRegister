package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.loginregister.home.HomeActivity;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        int id = 67;
        String[] field =  new String[1];
        field[0] = "id";
        //Creating array for data
        String[] data = new String[1];
        data[0] = String.valueOf(id);

        PutData putData = new PutData("http://192.168.1.72/PythonSuggestion/getSuggestion.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                String encoded_result = null;
                try {
                    encoded_result = new String(result.getBytes("ISO-8859-1"), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Log.i("PutData", encoded_result);
            }
        }

    }
}