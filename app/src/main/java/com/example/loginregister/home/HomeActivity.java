package com.example.loginregister.home;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.loginregister.R;
import com.example.loginregister.databinding.ActivityHomeBinding;
import com.example.loginregister.datasets.ItemInCart;
import com.example.loginregister.suggestion.CustomListSC;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    private HomePager pagerAdapter = new HomePager(this);

    public SharedViewModel viewModel;

    private TextView tvEmptyCart;
    private CustomListSC customListSC;
    private ListView lvShowChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.pagerHome.setUserInputEnabled(false);

        binding.pagerHome.setAdapter(pagerAdapter);

        binding.btnShoppingCart.setOnClickListener(listener);
        binding.planList.setOnClickListener(listener2);
        binding.groupNav.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int idx;
                String title = "";
                switch(checkedId) {
                    case R.id.radio_nav_add:
                        idx = 1;
                        title = "";
                        break;
                    case R.id.radio_nav_search:
                        idx = 2;
                        title = "推薦飲食";
                        break;
                    case R.id.radio_nav_heart:
                        idx = 3;
                        title = "目標計畫";
                        break;
                    case R.id.radio_nav_person:
                        idx = 4;
                        title = "個人資料";
                        break;
                    default:
                        idx = 0;
                        title = "";
                        break;
                }
                binding.pagerHome.setCurrentItem(idx, false);
                if (title.equals("")) {
                    binding.toolBar.setVisibility(View.GONE);
                }
                else {
                    binding.toolBar.setVisibility(View.VISIBLE);
                    binding.toolBarTitle.setText(title);
                }
                if (idx == 3) {
                    binding.planList.setVisibility(View.VISIBLE);
                } else {
                    binding.planList.setVisibility(View.GONE);
                }
                if (idx == 2) {
                    binding.btnShoppingCart.setVisibility(View.VISIBLE);
                } else {
                    binding.btnShoppingCart.setVisibility(View.GONE);
                }
            }
        });
    }
    Button.OnClickListener listener2= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            popupWindow popupWindow = new popupWindow(HomeActivity.this);
            View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.popup_shopping_cart, null);
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        }
    };
    Button.OnClickListener listener= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            popupWindow popupWindow = new popupWindow(HomeActivity.this);
            View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.popup_shopping_cart, null);
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        }
    };

    public class popupWindow extends PopupWindow implements View.OnClickListener {
        View view;
        RelativeLayout addBtn;

        public popupWindow(HomeActivity mContext) {
            this.view = LayoutInflater.from(mContext).inflate(R.layout.popup_shopping_cart, null);
            addBtn = view.findViewById(R.id.btn_add_items);
            tvEmptyCart = view.findViewById(R.id.tv_empty_cart);
            lvShowChosen = view.findViewById(R.id.lv_show_chosen);
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
            this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
            this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
            this.setFocusable(true);
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            this.setBackgroundDrawable(dw);
            addBtn.setOnClickListener(this);

            refreshCart();
        }

        public void onClick(View V){
            int id=V.getId();
            switch(id){
                case R.id.btn_add_items:
                    //加入SQLlite
                    //改資料庫rating
                    break;
            }
        }
    }

    public void refreshCart() {
        List<ItemInCart> items = viewModel.getChosenItems().getValue();
        tvEmptyCart.setVisibility(View.INVISIBLE);
        customListSC = new CustomListSC(HomeActivity.this, items);
        lvShowChosen.setAdapter(customListSC);
        if (items.isEmpty()) {
            tvEmptyCart.setVisibility(View.VISIBLE);
        }
        Log.i("itemInCart", items.toString());
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
