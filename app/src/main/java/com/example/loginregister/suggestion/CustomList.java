package com.example.loginregister.suggestion;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.loginregister.R;
import com.example.loginregister.datasets.FoodInfo;
import com.example.loginregister.datasets.ItemInCart;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomList extends BaseAdapter {
    private final Context mContext;
    private final SuggestionFragment mFragment;
    private final List<FoodInfo> foodInfo;
    private final List<ItemInCart> chosenItems;

    public CustomList(Context c, SuggestionFragment f, List<FoodInfo> foodInfo, List<ItemInCart> chosenItems) {
        mContext = c;
        mFragment = f;
        this.foodInfo = foodInfo;
        this.chosenItems = chosenItems;
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
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(View v) {
                mFragment.viewModel.addToStatus(foodInfo.get(position));
                ItemInCart item = new ItemInCart();
                item.foodInfo = foodInfo.get(position);
                item.quantity = 1;
                AtomicInteger index = new AtomicInteger();
                index.set(-1);
                chosenItems.forEach(itemInCart -> {
                    if (Objects.equals(itemInCart.foodInfo.food_id, item.foodInfo.food_id)) {
                        itemInCart.addOne();
                        index.set(chosenItems.indexOf(itemInCart));
                        return;
                    }
                });
                if (index.get() == -1) {
                    chosenItems.add(item);
                }
                mFragment.passData(chosenItems);
            }
        });

        return list;
    }
}
