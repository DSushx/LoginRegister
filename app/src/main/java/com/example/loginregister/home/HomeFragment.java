package com.example.loginregister.home;

import static com.example.loginregister.home.HomeActivity.GetHomeFood;

import static java.sql.Types.NULL;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.loginregister.R;
import com.example.loginregister.datasets.FoodInfo;
import com.example.loginregister.insert_food_DB;
import com.example.loginregister.suggestion.CustomList;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
    String stringDate;
    TextView fat;
    TextView protein ;
    TextView carb ;
    TextView kal ;
    TextView textdate;
    ProgressBar kcalProgress;

    private static SQLiteDatabase dbread;

    public HomeFragment() {
        super(R.layout.fragment_main);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        listView=view.findViewById(R.id.lv_home);
        kcalProgress = view.findViewById(R.id.progressBar3);
        dbread = new insert_food_DB(mContext, "editFoodDB", null, 6).getWritableDatabase();
        //Date currentTime = Calendar.getInstance().getTime();
        Date date_of_today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        stringDate= format.format(date_of_today);
        textdate = view.findViewById(R.id.TextDate);
        textdate.setText(stringDate);
        fat = view.findViewById(R.id.fatnum);
        protein = view.findViewById(R.id.pornum);
        carb = view.findViewById(R.id.carbnum);
        kal = view.findViewById(R.id.kcal);
        foodData =GetHomeFood(stringDate);
        homeList = new HomeList(mContext,  foodData);
        listView.setAdapter(homeList);
        setkal();

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        foodData= GetHomeFood(stringDate);
        homeList = new HomeList(mContext,  foodData);
        listView.setAdapter(homeList);
        textdate.setText(stringDate);
        setkal();
    }

    public void setkal(){

        int total_fat=0;
        int total_pro=0;
        int total_carb=0;
        int total_kal=0;
        Cursor cursor = dbread.rawQuery("select * from myFoodTable where date='"+stringDate+"'", null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int i =cursor.getInt(5);
                if(cursor.getInt(5)==NULL){
                    total_kal+=cursor.getInt(1);
                    total_fat+=cursor.getInt(3);
                    total_pro+=cursor.getInt(2);
                    total_carb+=cursor.getInt(4);
                }
                else{
                    while (i!=0){
                        total_kal+=cursor.getInt(1);
                        total_fat+=cursor.getInt(3);
                        total_pro+=cursor.getInt(2);
                        total_carb+=cursor.getInt(4);
                        i--;
                    }

                }

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
        //dbread.close();
    }
    public void circleupdate(){

    }
}





