package com.example.loginregister.home;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.loginregister.R;
import com.example.loginregister.datasets.FoodInfo;
import com.example.loginregister.suggestion.SuggestionFragment;

import java.util.List;

public class HomeList extends BaseAdapter {
    private Context mContext;
    private List<FoodInfo> foodInfo;

    public HomeList(Context c, List<FoodInfo> foodInfo) {
        mContext = c;

        this.foodInfo = foodInfo;
    }

    @Override
    public int getCount() {
        return foodInfo.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View list ;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            list = inflater.inflate(R.layout.item_home, viewGroup, false);
        } else {
            list = view;
        }
        
        TextView hmeFoodName = list.findViewById(R.id.textView11);
        TextView hmeKcalContent = list.findViewById(R.id.textView);
        ImageView hmeFoodPhoto = list.findViewById(R.id.imageView);

        hmeFoodName.setSelected(true);
        hmeFoodName.setText(foodInfo.get(i).title);
        hmeKcalContent.setText(String.format("%s", (int)foodInfo.get(i).calories));
        int resID;

        if (foodInfo.get(i).image != null) {
            resID = mContext.getResources().getIdentifier(foodInfo.get(i).image, "drawable", mContext.getPackageName());
            if (resID != 0) {
                hmeFoodPhoto.setImageResource(resID);
            }
        } else {
            hmeFoodPhoto.setImageResource(R.drawable.img_food);
        }
        return list;
    }
}
