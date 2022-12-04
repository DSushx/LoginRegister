package com.example.loginregister.home;

import android.util.Log;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.loginregister.datasets.DietStatus;
import com.example.loginregister.datasets.FoodInfo;
import com.example.loginregister.datasets.GoalActiveLevel;
import com.example.loginregister.datasets.ItemInCart;
import com.example.loginregister.suggestion.MysqlCon;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private int userId;
    public int getUserId() { return userId; }
    public void setUserId(int id) { this.userId = id; }

    private MutableLiveData<GoalActiveLevel> goalActiveLevel = new MutableLiveData<>(new GoalActiveLevel());
    public LiveData<GoalActiveLevel> getGoalActiveLevel() { return goalActiveLevel; }
    public void setGoalActiveLevel(GoalActiveLevel goalActiveLevel) { this.goalActiveLevel.setValue(goalActiveLevel); }

    private MysqlCon con = new MysqlCon();
    public MysqlCon getCon() { return con; }

    private List<FoodInfo> foodInfos = new ArrayList<>();
    public List<FoodInfo> getFoodData() { return foodInfos; }

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

    public void emptyStatus() {
        DietStatus status = dietStatus.getValue();
        status.CaloriesAchieved = 0;
        status.ProteinAchieved = 0;
        status.CarbsAchieved = 0;
        status.FatAchieved = 0;
        dietStatus.setValue(status);
    }

    public double oneDecimal(double num) {
        return Math.round(num * 10.0) / 10.0;
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

    public void emptyCart() {
        List<ItemInCart> emptyList = new ArrayList<>();
        chosenItems.setValue(emptyList);
    }

    public void getSuggestion() {
        List<FoodInfo> foodData = new ArrayList<>();

        String[] field =  new String[1];
        field[0] = "id";
        //Creating array for data
        String[] data = new String[1];
        data[0] = String.valueOf(userId);

        PutData putData = new PutData("http://192.168.1.211/PythonSuggestion/getSuggestion.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                String encoded_result = null;
                try {
                    encoded_result = new String(result.getBytes("ISO-8859-1"), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Log.i("PutData", encoded_result);

                try {
                    JSONObject obj = new JSONObject(encoded_result);
                    JSONArray arr = obj.getJSONArray("data");
                    for (int i = 0; i < arr.length(); i++) {
                        FoodInfo item = new FoodInfo();
                        item.food_id = arr.getJSONObject(i).getInt("food_id");
                        item.title = arr.getJSONObject(i).getString("title");
                        item.categories = arr.getJSONObject(i).getString("categories");
                        item.tags = arr.getJSONObject(i).getString("tags");
                        item.weight = arr.getJSONObject(i).getDouble("重量(g)");
                        item.calories = arr.getJSONObject(i).getDouble("熱量");
                        item.protein = arr.getJSONObject(i).getDouble("蛋白質(g)");
                        item.fat = arr.getJSONObject(i).getDouble("脂肪(g)");
                        item.carbs = arr.getJSONObject(i).getDouble("碳水化合物(g)");
                        item.sugar = arr.getJSONObject(i).getDouble("糖");
                        item.sodium = arr.getJSONObject(i).getDouble("鈉");
                        item.image = arr.getJSONObject(i).getString("image");
                        foodData.add(item);
                    }
                    Log.i("foodData", foodData.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        this.foodInfos = foodData;
    }
}
