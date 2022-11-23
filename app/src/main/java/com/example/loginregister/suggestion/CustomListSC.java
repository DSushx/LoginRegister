package com.example.loginregister.suggestion;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.loginregister.R;
import com.example.loginregister.datasets.DietStatus;
import com.example.loginregister.datasets.FoodInfo;
import com.example.loginregister.datasets.ItemInCart;
import com.example.loginregister.home.HomeActivity;

import java.util.List;

public class CustomListSC extends BaseAdapter {
    private final Context mContext;
    private final List<ItemInCart> chosenItems;

    public CustomListSC(Context c, List<ItemInCart> chosenItems) {
        mContext = c;
        this.chosenItems = chosenItems;
    }

    @Override
    public int getCount() {
        return chosenItems.size();
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
            list = inflater.inflate(R.layout.item_food_chosen, container, false);
        } else {
            list = convertView;
        }

        TextView tvFoodTitle = list.findViewById(R.id.tv_food_title);
        TextView quantity = list.findViewById(R.id.tv_quantity);

        tvFoodTitle.setSelected(true);
        tvFoodTitle.setText(chosenItems.get(position).foodInfo.title);
        quantity.setText(String.valueOf(chosenItems.get(position).quantity));

        RelativeLayout plus = list.findViewById(R.id.btn_plus);
        RelativeLayout minus = list.findViewById(R.id.btn_minus);
        plus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                chosenItems.get(position).addOne();
                ((HomeActivity)mContext).viewModel.addToStatus(chosenItems.get(position).foodInfo);
                ((HomeActivity)mContext).refreshCart(chosenItems);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (chosenItems.get(position).quantity == 1) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                    alertDialog.setTitle("刪除此項目");
                    alertDialog.setMessage("確定刪除?");
                    alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((HomeActivity)mContext).viewModel.minusFromStatus(chosenItems.get(position).foodInfo);
                            chosenItems.remove(position);
                            ((HomeActivity)mContext).refreshCart(chosenItems);
                        }
                    });
                    alertDialog.setNegativeButton("取消",(dialog, which) -> {
                        return;
                    });
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                } else {
                    chosenItems.get(position).minusOne();
                    ((HomeActivity)mContext).viewModel.minusFromStatus(chosenItems.get(position).foodInfo);
                    ((HomeActivity)mContext).refreshCart(chosenItems);
                }
            }
        });

        return list;
    }
}
