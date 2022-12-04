package com.example.loginregister.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.loginregister.R;
import com.example.loginregister.databinding.ActivityHomeBinding;
import com.example.loginregister.datasets.FoodInfo;
import com.example.loginregister.datasets.ItemInCart;
import com.example.loginregister.plan.pastplanFragment;
import com.example.loginregister.suggestion.CustomListSC;
import com.example.loginregister.insert_food_DB;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    private HomePager pagerAdapter = new HomePager(this);

    public SharedViewModel viewModel;

    private TextView tvEmptyCart;
    private CustomListSC customListSC;
    private ListView lvShowChosen;

    private static SQLiteDatabase db;
    private static SQLiteDatabase dbread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new insert_food_DB(this, "editFoodDB", null, 6).getWritableDatabase();
        dbread = new insert_food_DB(this, "editFoodDB", null, 6).getWritableDatabase();
        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.pagerHome.setUserInputEnabled(false);

        binding.pagerHome.setAdapter(pagerAdapter);

        binding.pagerHome.setOffscreenPageLimit(5);

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
                        title = "新增飲食";
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

                if (idx == 2) {
                    binding.btnShoppingCart.setVisibility(View.VISIBLE);
                } else {
                    binding.btnShoppingCart.setVisibility(View.GONE);
                }
                if (idx == 3) {
                    binding.planList.setVisibility(View.VISIBLE);
                } else {
                    binding.planList.setVisibility(View.GONE);
                }
            }
        });
    }
    Button.OnClickListener listener2= new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            Fragment secondfrag = new pastplanFragment();
            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            fm.replace(R.id.container,secondfrag).commit();

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

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void onClick(View V){
            int id=V.getId();
            switch(id){
                //購物車確認新增按鈕
                case R.id.btn_add_items:
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
                    alertDialog.setTitle("新增至飲食紀錄");
                    alertDialog.setMessage("確定新增?");
                    alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            viewModel.getChosenItems().getValue().forEach(item -> {
                                //加入SQLlite
                                db.execSQL("INSERT INTO myFoodTable(food_name, calorie, protein, fat, carbohydrate, quantity, image)" +
                                        " VALUES(\"" + item.foodInfo.title + "\"," + item.foodInfo.calories + "," + item.foodInfo.protein
                                        + "," + item.foodInfo.fat + "," + item.foodInfo.carbs + "," + item.quantity + ",\"" + item.foodInfo.image + "\")");

                                //改資料庫rating
                                new Thread(() -> {
                                    viewModel.getCon().updateRating(viewModel.getUserId(), item.foodInfo.food_id);
                                }).start();

                            });

                            //清空購物車
                            viewModel.emptyCart();
                            //更新推薦
                            viewModel.getSuggestion();
                            //清除狀態
                            viewModel.emptyStatus();


                            Toast.makeText(HomeActivity.this,"已新增至飲食紀錄",Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    });
                    alertDialog.setNegativeButton("取消",(dialog, which) -> {
                        return;
                    });
                    alertDialog.setCancelable(false);
                    alertDialog.show();

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

    public static ArrayList<FoodInfo> GetHomeFood(String date) {

        ArrayList<FoodInfo> list = new ArrayList<FoodInfo>();

        //哈囉
        Cursor cursor = dbread.rawQuery("select * from myFoodTable where date='"+date+"' ", null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                FoodInfo bean = new FoodInfo();
                bean.title = cursor.getString(0);
                bean.calories = cursor.getInt(1);
                bean.image = cursor.getString(7);
                list.add(bean);
            }
            cursor.close();
        }
        //dbread.close();
        return list;
    }


}
