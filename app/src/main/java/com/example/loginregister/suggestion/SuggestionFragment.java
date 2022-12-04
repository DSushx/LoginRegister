package com.example.loginregister.suggestion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.loginregister.R;
import com.example.loginregister.datasets.DietStatus;
import com.example.loginregister.datasets.FoodInfo;
import com.example.loginregister.datasets.ItemInCart;
import com.example.loginregister.datasets.UserInfo;
import com.example.loginregister.home.SharedViewModel;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SuggestionFragment extends Fragment {
    public SuggestionFragment() {
        super(R.layout.fragment_suggest_food);
    }

    Context mContext;
    SharedViewModel viewModel;
    View mView;
    UserInfo userData;
    List<FoodInfo> foodData = new ArrayList<>();
    CustomList customList;
    ProgressBar progressBar;
    TextView caloriesLimit, caloriesHad, proteinHad, carbsHad, fatHad, noResult;
    ProgressBar caloriesProgress, proteinProgress, carbsProgress, fatProgress;
    ListView lvShow;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
        final Observer<DietStatus> statusObserver = new Observer<DietStatus>() {
            @Override
            public void onChanged(DietStatus dietStatus) {
                // Update the UI.
                update();
                Log.i("dietStatus", viewModel.getDietStatus().getValue().toString());
            }
        };
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        viewModel.getDietStatus().observe(getViewLifecycleOwner(), statusObserver);


        mView = view;

        caloriesLimit = view.findViewById(R.id.calories_limit);
        caloriesHad = view.findViewById(R.id.calories_had);
        caloriesProgress = view.findViewById(R.id.caloriesProgress);
        proteinHad = view.findViewById(R.id.protein_had);
        proteinProgress = view.findViewById(R.id.proteinProgress);
        carbsHad = view.findViewById(R.id.carbs_had);
        carbsProgress = view.findViewById(R.id.carbsProgress);
        fatHad = view.findViewById(R.id.fat_had);
        fatProgress = view.findViewById(R.id.fatProgress);
        lvShow = view.findViewById(R.id.lv_show);
        progressBar = view.findViewById(R.id.progressBar);
        noResult = view.findViewById(R.id.tv_no_result);

        progressBar.setVisibility(View.VISIBLE);

        new Thread(() -> {

            viewModel.getCon().run();

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
            String uname = pref.getString("username", null);
            Log.i("username", uname);
            userData = viewModel.getCon().getUserData(uname);
            Log.v("OK", "使用者資料已回傳");
            Log.i("userData", userData.toString());
            viewModel.setUserId(userData.user_id);

            String[] field =  new String[1];
            field[0] = "id";
            //Creating array for data
            String[] data = new String[1];
            data[0] = String.valueOf(userData.user_id);

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

            DietStatus dietStatus = getInitialDietStatus(userData);

            view.post(() -> {
                viewModel.setDietStatus(dietStatus);
                caloriesLimit.setText(String.format("%s", dietStatus.CaloriesPerMeal));
            });

        }).start();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public DietStatus getInitialDietStatus(UserInfo userInfo) {
        DietStatus dietStatus = new DietStatus();
        int age, activeLevel = 0, goal = 1;
        double BMR = 0, TDEE, proteinRatio, carbsRatio, fatRatio;

        Calendar bday = Calendar.getInstance();
        bday.setTime(userInfo.birthday);

        age = Period.between(
                LocalDate.of(bday.get(Calendar.YEAR), bday.get(Calendar.MONTH), bday.get(Calendar.DAY_OF_MONTH)),
                LocalDate.now())
                .getYears();

        Log.i("age", Integer.toString(age));

        if (Objects.equals(userInfo.gender, "Male")) {
            BMR = 66 + (13.7 * userInfo.weight + 5 * userInfo.height - 6.8 * age);
        } else if (Objects.equals(userInfo.gender, "Female")){
            BMR = 655 + (9.6 * userInfo.weight + 1.8 * userInfo.height - 4.7 * age);
        }

        Log.i("BMR", String.valueOf(BMR));

        switch(activeLevel) {
            case 0:  //低度
                TDEE = BMR * 1.2;
                break;
            case 1:  //中度
                TDEE = BMR * 1.4;
                break;
            case 2:  //高度
                TDEE = BMR * 1.6;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + activeLevel);
        }

        switch(goal) {
            case 0:  //減脂
                proteinRatio = 0.35;
                carbsRatio = 0.4;
                fatRatio = 0.25;
                TDEE *= 0.9;
                break;
            case 1:  //維持
                proteinRatio = 0.2;
                carbsRatio = 0.5;
                fatRatio = 0.3;
                break;
            case 2:  //增肌
                proteinRatio = 0.25;
                carbsRatio = 0.55;
                fatRatio = 0.2;
                TDEE *= 1.1;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + goal);
        }

        Log.i("TDEE", String.valueOf((int)TDEE));

        dietStatus.CaloriesPerDay = (int)TDEE;
        dietStatus.CaloriesPerMeal = (int)(dietStatus.CaloriesPerDay * 0.4);  //早:五:晚 = 2:4:4
        dietStatus.ProteinPerDay = viewModel.oneDecimal(dietStatus.CaloriesPerDay * proteinRatio / 4);
        dietStatus.ProteinPerMeal = viewModel.oneDecimal(dietStatus.ProteinPerDay * 0.4);
        dietStatus.CarbsPerDay = viewModel.oneDecimal(dietStatus.CaloriesPerDay * carbsRatio / 4);
        dietStatus.CarbsPerMeal = viewModel.oneDecimal(dietStatus.CarbsPerDay * 0.4);
        dietStatus.FatPerDay = viewModel.oneDecimal(dietStatus.CaloriesPerDay * fatRatio / 9);
        dietStatus.FatPerMeal = viewModel.oneDecimal(dietStatus.FatPerDay * 0.4);

        return dietStatus;
    }

    public void updateBoard(DietStatus dietStatus) {
        caloriesHad.setText(String.format("%s", dietStatus.CaloriesAchieved));
        caloriesProgress.setProgress((int)((double)dietStatus.CaloriesAchieved / dietStatus.CaloriesPerMeal * 360));
        updateRingColor();
        proteinHad.setText(String.format("%s / %s g", String.format("%s", dietStatus.ProteinAchieved), String.format("%s", dietStatus.ProteinPerMeal)));
        proteinProgress.setProgress((int)(dietStatus.ProteinAchieved / dietStatus.ProteinPerMeal * 100));
        updateBarColor(dietStatus.ProteinAchieved, dietStatus.ProteinPerMeal, proteinProgress, proteinHad);
        carbsHad.setText(String.format("%s / %s g", String.format("%s", dietStatus.CarbsAchieved), String.format("%s", dietStatus.CarbsPerMeal)));
        carbsProgress.setProgress((int)(dietStatus.CarbsAchieved / dietStatus.CarbsPerMeal * 100));
        updateBarColor(dietStatus.CarbsAchieved, dietStatus.CarbsPerMeal, carbsProgress, carbsHad);
        fatHad.setText(String.format("%s / %s g", String.format("%s", dietStatus.FatAchieved), String.format("%s", dietStatus.FatPerMeal)));
        fatProgress.setProgress((int)(dietStatus.FatAchieved / dietStatus.FatPerMeal * 100));
        updateBarColor(dietStatus.FatAchieved, dietStatus.FatPerMeal, fatProgress, fatHad);
    }

    public void updateRingColor() {
        double fillRate = (double)viewModel.getDietStatus().getValue().CaloriesAchieved / viewModel.getDietStatus().getValue().CaloriesPerMeal;
        if (fillRate < 0.75) {
            caloriesProgress.setProgressDrawable(getResources().getDrawable(R.drawable.progressring_green));
            caloriesHad.setTextColor(getResources().getColor(R.color.green_7ec972));
        } else if (fillRate < 1) {
            caloriesProgress.setProgressDrawable(getResources().getDrawable(R.drawable.progressring_yellow));
            caloriesHad.setTextColor(getResources().getColor(R.color.yellow_f7b400));
        } else {
            caloriesProgress.setProgressDrawable(getResources().getDrawable(R.drawable.progressring_red));
            caloriesHad.setTextColor(getResources().getColor(R.color.red_fa7c6b));
        }
    }

    public void updateBarColor(double achieved, double goal, ProgressBar progress, TextView text) {
        double fillRate = achieved / goal;
        if (fillRate < 0.75) {
            progress.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_green));
            text.setTextColor(getResources().getColor(R.color.green_7ec972));
        } else if (fillRate < 1) {
            progress.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_yellow));
            text.setTextColor(getResources().getColor(R.color.yellow_f7b400));
        } else {
            progress.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_red));
            text.setTextColor(getResources().getColor(R.color.red_fa7c6b));
        }
    }

    public void update() {
        DietStatus dietStatus = viewModel.getDietStatus().getValue();
        updateBoard(dietStatus);
        refreshList(dietStatus.CaloriesPerMeal, dietStatus.CaloriesAchieved);
    }

    public void refreshList(int caloriesPerMeal, int caloriesAchieved) {
        int caloriesDifference = caloriesPerMeal - caloriesAchieved;
        int threshold = Math.max(caloriesDifference, 0);
        noResult.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {

//            foodData = viewModel.getCon().getFoodData(threshold);
            Log.v("OK", "食物資料已回傳");
            Log.i("foodData", foodData.toString());

            mView.post(() -> {
                progressBar.setVisibility(View.INVISIBLE);
                customList = new CustomList(requireContext(), this, foodData);
                lvShow.setAdapter(customList);
                if (foodData.isEmpty()) {
                    noResult.setVisibility(View.VISIBLE);
                }
            });
        }).start();
    }
}
