package com.example.loginregister.datasets;

import androidx.annotation.NonNull;

public class ItemInCart {
    public FoodInfo foodInfo;
    public int quantity;

    @NonNull
    public String toString(){
        return foodInfo.toString() + quantity + "]";
    }
}
