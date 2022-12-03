package com.example.loginregister.datasets;

import java.util.ArrayList;
import java.util.List;

public class FoodList {
    public List<FoodInfo> foodInfoList = new ArrayList<>();

    public void addItem(FoodInfo item) {
        foodInfoList.add(item);
    }
}
