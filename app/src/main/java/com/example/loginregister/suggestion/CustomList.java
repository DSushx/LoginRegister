package com.example.loginregister.suggestion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.loginregister.R;
import com.example.loginregister.datasets.DietStatus;
import com.example.loginregister.datasets.FoodInfo;
import com.example.loginregister.datasets.ItemInCart;

import java.util.List;

public class CustomList extends BaseAdapter {
    private final Context mContext;
    private final SuggestionFragment mFragment;
    private final List<FoodInfo> foodInfo;
    private final List<ItemInCart> chosenItems;
    private DietStatus dietStatus;

    public CustomList(Context c, SuggestionFragment f, List<FoodInfo> foodInfo, List<ItemInCart> chosenItems, DietStatus dietStatus) {
        mContext = c;
        mFragment = f;
        this.foodInfo = foodInfo;
        this.chosenItems = chosenItems;
        this.dietStatus = dietStatus;
    }

    @Override
    public int getCount() {
        return foodInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        View list;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            list = inflater.inflate(R.layout.item_suggest_food, container, false);
        } else {
            list = convertView;
        }

        TextView tvFoodName = list.findViewById(R.id.tv_food_name);
        TextView tvKcalContent = list.findViewById(R.id.tv_kcal_content);
        TextView tvProteinContent = list.findViewById(R.id.tv_protein_content);
        TextView tvCarbsContent = list.findViewById(R.id.tv_carbs_content);
        TextView tvFatContent = list.findViewById(R.id.tv_fat_content);
        ImageView ivFoodPhoto = list.findViewById(R.id.iv_food_photo);

        tvFoodName.setSelected(true);
        tvFoodName.setText(foodInfo.get(position).title);
        tvKcalContent.setText(String.format("%s kcal", (int)foodInfo.get(position).calories));
        tvProteinContent.setText(String.format("%s g", foodInfo.get(position).protein));
        tvCarbsContent.setText(String.format("%s g", foodInfo.get(position).carbs));
        tvFatContent.setText(String.format("%s g", foodInfo.get(position).fat));
        int resID;
        if (foodInfo.get(position).image != null) {
            resID = mContext.getResources().getIdentifier(foodInfo.get(position).image, "drawable", mContext.getPackageName());
            if (resID != 0) {
                ivFoodPhoto.setImageResource(resID);
            }
        } else {
            ivFoodPhoto.setImageResource(R.drawable.img_food);
        }

        RelativeLayout addToCart = (RelativeLayout) list.findViewById(R.id.btn_add_to_cart);
        addToCart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dietStatus = mFragment.updateStatus(foodInfo.get(position).calories, foodInfo.get(position).protein,
                        foodInfo.get(position).carbs, foodInfo.get(position).fat);
                ItemInCart item = new ItemInCart();
                item.foodInfo = foodInfo.get(position);
                item.quantity = 1;
                chosenItems.add(item);
                mFragment.passData(chosenItems, dietStatus);
            }
        });

        return list;
    }
}
