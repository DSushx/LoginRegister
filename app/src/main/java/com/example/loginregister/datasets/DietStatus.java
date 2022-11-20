package com.example.loginregister.datasets;

import androidx.annotation.NonNull;

public class DietStatus {
    public Integer CaloriesPerDay;
    public Integer CaloriesPerMeal;
    public Integer CaloriesAchieved = 0;
    public double ProteinPerDay;
    public double ProteinPerMeal;
    public double ProteinAchieved = 0;
    public double CarbsPerDay;
    public double CarbsPerMeal;
    public double CarbsAchieved = 0;
    public double FatPerDay;
    public double FatPerMeal;
    public double FatAchieved = 0;

    @NonNull
    public String toString(){
        return "[" + CaloriesPerDay + "," + CaloriesPerMeal + "," + CaloriesAchieved + "," + ProteinPerDay + "," +
                ProteinPerMeal + "," + ProteinAchieved + "," + CarbsPerDay + "," + CarbsPerMeal + "," + CarbsAchieved
                + "," + FatPerDay + "," + FatPerMeal + "," + FatAchieved + "]";
    }
}
