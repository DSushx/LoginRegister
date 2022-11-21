package com.example.loginregister.suggestion;

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
import com.example.loginregister.R;
import com.example.loginregister.datasets.DietStatus;
import com.example.loginregister.datasets.FoodInfo;
import com.example.loginregister.datasets.UserInfo;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SuggestionFragment extends Fragment {
    public SuggestionFragment() {
        super(R.layout.fragment_suggest_food);
    }

    UserInfo userData;
    DietStatus dietStatus;
    List<FoodInfo> foodData;
    CustomList customList;
    ProgressBar progressBar;
    TextView caloriesLimit, caloriesHad, proteinHad, carbsHad, fatHad, noResult;
    ProgressBar caloriesProgress, proteinProgress, carbsProgress, fatProgress;
    ListView lvShow;
    MysqlCon con;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

            con = new MysqlCon();
            con.run();

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
            String uname = pref.getString("username", null);
            Log.i("username", uname);
            userData = con.getUserData(uname);
            Log.v("OK", "使用者資料已回傳");
            Log.i("userData", userData.toString());

            dietStatus = getDietStatus(userData);
            Log.i("dietStatus", dietStatus.toString());

            view.post(() -> {
                caloriesLimit.setText(String.format("%s", dietStatus.CaloriesPerMeal));
                updateBoard();
            });

            refreshList(dietStatus.CaloriesPerMeal);

        }).start();

    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public DietStatus getDietStatus(UserInfo userInfo) {
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

        dietStatus.CaloriesPerDay = (int)TDEE;
        dietStatus.CaloriesPerMeal = (int)(dietStatus.CaloriesPerDay * 0.4);  //早:五:晚 = 2:4:4
        dietStatus.ProteinPerDay = dietStatus.CaloriesPerDay * proteinRatio / 4;
        dietStatus.ProteinPerMeal = dietStatus.ProteinPerDay * 0.4;
        dietStatus.CarbsPerDay = dietStatus.CaloriesPerDay * carbsRatio / 4;
        dietStatus.CarbsPerMeal = dietStatus.CarbsPerDay * 0.4;
        dietStatus.FatPerDay = dietStatus.CaloriesPerDay * fatRatio / 9;
        dietStatus.FatPerMeal = dietStatus.FatPerDay * 0.4;

        return dietStatus;
    }

    public void updateBoard() {
        caloriesHad.setText(String.format("%s", dietStatus.CaloriesAchieved));
        caloriesProgress.setProgress((int)((double)dietStatus.CaloriesAchieved / dietStatus.CaloriesPerMeal * 360));
        updateRingColor();
        proteinHad.setText(String.format("%s / %s g", String.format("%.1f", dietStatus.ProteinAchieved), String.format("%.1f", dietStatus.ProteinPerMeal)));
        proteinProgress.setProgress((int)(dietStatus.ProteinAchieved / dietStatus.ProteinPerMeal * 100));
        updateBarColor(dietStatus.ProteinAchieved, dietStatus.ProteinPerMeal, proteinProgress, proteinHad);
        carbsHad.setText(String.format("%s / %s g", String.format("%.1f", dietStatus.CarbsAchieved), String.format("%.1f", dietStatus.CarbsPerMeal)));
        carbsProgress.setProgress((int)(dietStatus.CarbsAchieved / dietStatus.CarbsPerMeal * 100));
        updateBarColor(dietStatus.CarbsAchieved, dietStatus.CarbsPerMeal, carbsProgress, carbsHad);
        fatHad.setText(String.format("%s / %s g", String.format("%.1f", dietStatus.FatAchieved), String.format("%.1f", dietStatus.FatPerMeal)));
        fatProgress.setProgress((int)(dietStatus.FatAchieved / dietStatus.FatPerMeal * 100));
        updateBarColor(dietStatus.FatAchieved, dietStatus.FatPerMeal, fatProgress, fatHad);
    }

    public void updateRingColor() {
        double fillRate = (double)dietStatus.CaloriesAchieved / dietStatus.CaloriesPerMeal;
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

    public void updateStatus(double calories, double protein, double carbs, double fat) {
        dietStatus.CaloriesAchieved += (int)calories;
        dietStatus.ProteinAchieved += protein;
        dietStatus.CarbsAchieved += carbs;
        dietStatus.FatAchieved += fat;
        updateBoard();
        int caloriesDifference = dietStatus.CaloriesPerMeal - dietStatus.CaloriesAchieved;
        int newThreshold = caloriesDifference > 0 ? caloriesDifference : 0;
        refreshList(newThreshold);
    }

    public void refreshList(int threshold) {
        noResult.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {

            foodData = con.getFoodData(threshold);
            Log.v("OK", "食物資料已回傳");
            Log.i("foodData", foodData.toString());

            getView().post(() -> {
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
