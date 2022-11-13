package com.example.loginregister.suggestion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.loginregister.R;
import com.vishnusivadas.advanced_httpurlconnection.PutData;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class HomeFragment2 extends Fragment {
    public HomeFragment2() {
        super(R.layout.activity_main2);
    }

    ListView lvShow;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lvShow = view.findViewById(R.id.lv_show);

        new Thread(new Runnable() {
            @Override
            public void run() {
                MysqlCon con = new MysqlCon();
                con.run();
                final List<FoodInfo> data = con.getData(500);
                Log.v("OK", "資料已回傳");
//                setAdapter(data);
            }
        }).start();



    }

    private void setAdapter(List<FoodInfo> data) {
        ArrayAdapter<FoodInfo> adapter = new ArrayAdapter<FoodInfo>(requireActivity(), R.layout.item_suggest_food_new, R.id.lv_show, data);
        lvShow.setAdapter(adapter);
    }
}