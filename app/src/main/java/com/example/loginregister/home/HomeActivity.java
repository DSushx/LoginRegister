package com.example.loginregister.home;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.RadioGroup;

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
                    default:
                        idx = 0;
                        title = "";
                        break;
                }
                binding.pagerHome.setCurrentItem(idx);
                if (title == "") {
                    binding.toolBar.setVisibility(View.INVISIBLE);
                }
                else {
                    binding.toolBar.setVisibility(View.VISIBLE);
                    binding.toolBarTitle.setText(title);
                }
            }
        });
    }
}
