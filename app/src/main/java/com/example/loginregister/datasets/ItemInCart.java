package com.example.loginregister.datasets;

import androidx.annotation.NonNull;

public class ItemInCart {
    public FoodInfo foodInfo;
    public int quantity;
    public void addOne() {
        this.quantity += 1;
    }

    public void minusOne() {
        this.quantity -= 1;
    }

    @NonNull
    public String toString(){
        return foodInfo.toString() + "[" + quantity + "]";
    }
}
