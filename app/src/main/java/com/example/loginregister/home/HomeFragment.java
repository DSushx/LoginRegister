package com.example.loginregister.home;

import static com.example.loginregister.home.HomeActivity.GetHomeFood;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.loginregister.R;
import com.example.loginregister.datasets.FoodInfo;
import com.example.loginregister.insert_food_DB;
import com.example.loginregister.suggestion.CustomList;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    Context mContext;
    List<FoodInfo> foodData;
    HomeList homeList;
    ListView listView;
    View mView;

    public HomeFragment() {
        super(R.layout.fragment_main);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        listView=view.findViewById(R.id.lv_home);
        foodData =GetHomeFood();

        homeList = new HomeList(mContext, this, foodData);
        listView.setAdapter(homeList);

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }




}





