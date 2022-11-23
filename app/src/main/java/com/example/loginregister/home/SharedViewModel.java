package com.example.loginregister.home;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.loginregister.datasets.DietStatus;
import com.example.loginregister.datasets.FoodInfo;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<DietStatus> dietStatus = new MutableLiveData<DietStatus>();
    public LiveData<DietStatus> getDietStatus() {
        return dietStatus;
    }

    public void setDietStatus(DietStatus dietStatus) {
        this.dietStatus.setValue(dietStatus);
    }

    public void addToStatus(FoodInfo food) {
        DietStatus status = dietStatus.getValue();
        status.CaloriesAchieved += (int)food.calories;
        status.ProteinAchieved = oneDecimal(status.ProteinAchieved + food.protein);
        status.CarbsAchieved = oneDecimal(status.CarbsAchieved + food.carbs);
        status.FatAchieved = oneDecimal(status.FatAchieved + food.fat);
        dietStatus.setValue(status);
    }

    public void minusFromStatus(FoodInfo food) {
        DietStatus status = dietStatus.getValue();
        status.CaloriesAchieved -= (int)food.calories;
        status.ProteinAchieved = oneDecimal(status.ProteinAchieved - food.protein);
        status.CarbsAchieved = oneDecimal(status.CarbsAchieved - food.carbs);
        status.FatAchieved = oneDecimal(status.FatAchieved - food.fat);
        dietStatus.setValue(status);
    }

    public double oneDecimal(double num) {
        return Math.round(num * 10.0) / 10.0;
    }
}
