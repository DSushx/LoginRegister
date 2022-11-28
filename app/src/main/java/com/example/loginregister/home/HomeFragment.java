package com.example.loginregister.home;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.loginregister.R;
import com.example.loginregister.datasets.FoodInfo;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private SQLiteOpenHelper dbHelperSqlite;

    public HomeFragment() {
        super(R.layout.fragment_main);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public ArrayList<FoodInfo> query(String _id) {

        ArrayList<FoodInfo> list = new ArrayList<FoodInfo>();

        SQLiteDatabase db = dbHelperSqlite.getReadableDatabase();
        Cursor cursor = db.rawQuery("select food_name, calories, image from myFoodTable where date= date('now')", new String[]{_id});
        if (cursor != null && cursor.getCount() > 0) {
//判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                FoodInfo bean = new FoodInfo();
                bean.food_id = cursor.getInt(0);
                bean.calories = cursor.getInt(1);
                bean.image = cursor.getString(2);
                list.add(bean);
                System.out.println("_id:" + bean.food_id + "; name: " + bean.calories + "; humi: " + bean.image);
            }
            cursor.close();
        }
        db.close();
        return list;
    }
}
