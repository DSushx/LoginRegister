package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.loginregister.datasets.FoodInfo;
import com.example.loginregister.datasets.FoodList;
import com.example.loginregister.home.HomeActivity;
import com.example.loginregister.suggestion.MysqlCon;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText Et1, Et2;
    Button Btn;
    TextView tv;
    MysqlCon con;
    ResultSet rs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        Et1 = (EditText)findViewById(R.id.et1);
        Et2 = (EditText)findViewById(R.id.et2);
        Btn = (Button)findViewById(R.id.btn);
        tv=(TextView)findViewById(R.id.text_view);



        if (!Python.isStarted())
            Python.start (new AndroidPlatform(this));
        Python py=Python.getInstance();
        final PyObject pyobj=py.getModule("script");

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PyObject obj=pyobj.callAttr("main",Et1.getText().toString(),Et2.getText().toString());
                tv.setText(obj.toString());

                Log.i("result", obj.toString());
            }
        });

        final PyObject cofilResult = py.getModule("collab_filter").callAttr("main", 67);
        FoodList result = cofilResult.toJava(FoodList.class);
        Log.i("cofilResult", result.foodInfoList.toString());


    }
}