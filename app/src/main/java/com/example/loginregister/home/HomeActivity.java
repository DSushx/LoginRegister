package com.example.loginregister.home;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginregister.R;
import com.example.loginregister.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    private HomePager pagerAdapter = new HomePager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.pagerHome.setUserInputEnabled(false);

        binding.pagerHome.setAdapter(pagerAdapter);

        binding.btnShoppingCart.setOnClickListener(listener);

        binding.groupNav.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int idx;
                String title = "";
                switch(checkedId) {
                    case R.id.radio_nav_add:
                        idx = 1;
                        title = "新增飲食";
                        break;
                    case R.id.radio_nav_search:
                        idx = 2;
                        title = "推薦飲食";
                        break;
                    case R.id.radio_nav_person:
                        idx = 3;
                        title = "個人資料";
                        break;
                    case R.id.radio_nav_heart:
                        idx = 4;
                        title = "目標計畫";
                        break;
                    default:
                        idx = 0;
                        title = "";
                        break;
                }
                binding.pagerHome.setCurrentItem(idx);
                if (title == "") {
                    binding.toolBar.setVisibility(View.GONE);
                }
                else {
                    binding.toolBar.setVisibility(View.VISIBLE);
                    binding.toolBarTitle.setText(title);
                }
                if (idx == 2) {
                    binding.btnShoppingCart.setVisibility(View.VISIBLE);
                } else {
                    binding.btnShoppingCart.setVisibility(View.GONE);
                }
            }
        });
    }

    Button.OnClickListener listener= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            popupWindow popupWindow = new popupWindow(HomeActivity.this);
            View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.popup_shopping_cart, null);
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        }
    };

    public static class popupWindow extends PopupWindow implements View.OnClickListener {
        View view;
        RelativeLayout okBtn;

        public popupWindow(HomeActivity mContext) {
            this.view = LayoutInflater.from(mContext).inflate(R.layout.popup_shopping_cart, null);
            okBtn = view.findViewById(R.id.btn_add_items);
            this.setOutsideTouchable(true);
            this.view.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                public boolean onTouch(View v, MotionEvent event) {
                    int height = view.findViewById(R.id.pop_layout).getTop();
                    int y = (int) event.getY();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y < height) {
                            dismiss();
                        }
                    }
                    return true;
                }
            });

            this.setContentView(this.view);
            this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
            this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
            this.setFocusable(true);
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            this.setBackgroundDrawable(dw);
            okBtn.setOnClickListener(this);
        }

        public void onClick(View V){
//            int id=V.getId();
//            switch(id){
//                case R.id.okBtn:
//                    Toast.makeText(MainActivity.this,"點擊確定", Toast.LENGTH_SHORT).show();
//                    break;
//            }
        }
    }
}
