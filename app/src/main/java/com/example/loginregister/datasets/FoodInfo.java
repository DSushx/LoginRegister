package com.example.loginregister.datasets;

import androidx.annotation.NonNull;

public class FoodInfo {
    public Integer food_id;
    public String title;
    public String categories;
    public String tags;
    public double weight;
    public double calories;
    public double protein;
    public double carbs;
    public double fat;
    public double sugar;
    public double sodium;
    public String image;

    @NonNull
    public String toString(){
        return "[" + food_id + "," + title + "," + categories + "," + tags + "," + weight + "," + calories
                + "," + protein + "," + carbs + "," + fat + "," + sugar + "," + sodium + "," + image + "]";
    }
}
