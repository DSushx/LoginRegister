package com.example.loginregister.suggestion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.loginregister.R;

public class CustomGrid extends BaseAdapter {
    private Context mContext;
    private final String productInfo[][];
    private final int[] ImageId;

    public CustomGrid(Context c, String productInfo[][], int[] ImageId) {
        mContext = c;
        this.ImageId = ImageId;
        this.productInfo = productInfo;
    }

    @Override
    public int getCount() {
        return productInfo.length;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = inflater.inflate(R.layout.item_suggest_food, null);
            TextView tvProductName = grid.findViewById(R.id.tv_product_name);
            TextView tvPrice = grid.findViewById(R.id.tv_price);
            TextView tvSize = grid.findViewById(R.id.tv_size);
            ImageView ivProduct = grid.findViewById(R.id.iv_product);

            tvProductName.setText(productInfo[position][0]);
            tvPrice.setText(productInfo[position][1]);
            tvSize.setText(productInfo[position][2]);
            ivProduct.setImageResource(ImageId[position]);
        } else {
            grid = convertView;
        }

        return grid;
    }
}
