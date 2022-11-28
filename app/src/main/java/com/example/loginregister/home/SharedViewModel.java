package com.example.loginregister.home;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.loginregister.datasets.DietStatus;
import com.example.loginregister.datasets.FoodInfo;
import com.example.loginregister.datasets.ItemInCart;
import com.example.loginregister.suggestion.MysqlCon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private MysqlCon con = new MysqlCon();
    public MysqlCon getCon() { return con; }
//    public void setCon(MysqlCon con) {
//        this.con = con;
//    }

    private MutableLiveData<DietStatus> dietStatus = new MutableLiveData<>();
    public LiveData<DietStatus> getDietStatus() {
        return dietStatus;
    }

    private MutableLiveData<List<ItemInCart>> chosenItems = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<ItemInCart>> getChosenItems() {
        return chosenItems;
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

    public void setChosenItems(List<ItemInCart> chosenItems) {
        this.chosenItems.setValue(chosenItems);
    }

    public void addToCart(ItemInCart item) {
        List<ItemInCart> itemList = chosenItems.getValue();
        itemList.add(item);
        chosenItems.setValue(itemList);
    }

    public void addQuantity(int index) {
        List<ItemInCart> itemList = chosenItems.getValue();
        ItemInCart theItem = itemList.get(index);
        theItem.addOne();
        itemList.set(index, theItem);
        chosenItems.setValue(itemList);
    }

    public void minusQuantity(int index) {
        List<ItemInCart> itemList = chosenItems.getValue();
        ItemInCart theItem = itemList.get(index);
        theItem.minusOne();
        itemList.set(index, theItem);
        chosenItems.setValue(itemList);
    }
}
