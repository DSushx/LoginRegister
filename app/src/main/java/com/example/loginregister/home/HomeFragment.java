package com.example.loginregister.home;

import static com.example.loginregister.home.HomeActivity.GetHomeFood;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.loginregister.R;
import com.example.loginregister.datasets.FoodInfo;
import com.example.loginregister.insert_food_DB;
import com.example.loginregister.suggestion.CustomList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class HomeFragment extends Fragment {
    Context mContext;
    List<FoodInfo> foodData;
    HomeList homeList;
    ListView listView;
    View mView;

    private static SQLiteDatabase dbread;

    public HomeFragment() {
        super(R.layout.fragment_main);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        listView=view.findViewById(R.id.lv_home);
        foodData =GetHomeFood();
        homeList = new HomeList(mContext,  foodData);
        listView.setAdapter(homeList);
        dbread = new insert_food_DB(mContext, "editFoodDB", null, 6).getWritableDatabase();
        Date currentTime = Calendar.getInstance().getTime();

        TextView textdate = view.findViewById(R.id.TextDate);
        String date= String.valueOf(currentTime);
        textdate.setText(date);

        Cursor cursor = dbread.rawQuery("select * from myFoodTable'"+currentTime+"'", null);

        TextView fat = view.findViewById(R.id.fatnum);
        TextView protein = view.findViewById(R.id.pornum);
        TextView carb = view.findViewById(R.id.carbnum);
        TextView kal = view.findViewById(R.id.kcal);

        int total_fat=0;
        int total_pro=0;
        int total_carb=0;
        int total_kal=0;

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                total_kal+=cursor.getInt(1);
                total_fat+=cursor.getInt(3);
                total_pro+=cursor.getInt(2);
                total_carb+=cursor.getInt(4);
            }
            cursor.close();
        }
        String f=Integer.toString(total_fat);
        fat.setText(f);
        String p=Integer.toString(total_pro);
        protein.setText(p);
        String c=Integer.toString(total_carb);
        carb.setText(c);
        String k=Integer.toString(total_kal);
        kal.setText(k);
        dbread.close();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        foodData= GetHomeFood();
        homeList = new HomeList(mContext,  foodData);
        listView.setAdapter(homeList);
    }
}





