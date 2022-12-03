package com.example.loginregister.suggestion;

import android.util.Log;

import com.example.loginregister.datasets.FoodInfo;
import com.example.loginregister.datasets.NowPlanInfo;
import com.example.loginregister.datasets.PlanInfo;
import com.example.loginregister.datasets.UserInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MysqlCon {
    String mysql_ip = "192.168.1.72";
    int mysql_port = 3306;
    String db_name = "food_db";
    String url = "jdbc:mysql://" + mysql_ip + ":" + mysql_port + "/" + db_name;
    String db_user = "root";
    String db_password = "";

    public void run() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Log.v("DB", "加載驅動成功");
        } catch(ClassNotFoundException e) {
            Log.e("DB", "加載驅動失敗");
            return;
        }

        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            Log.v("DB", "遠端連接成功");
        } catch(SQLException e) {
            Log.e("DB", "遠端連線失敗");
            Log.e("DB", e.toString());
        }
    }

    public UserInfo getUserData(String uname) {
        UserInfo userInfo = new UserInfo();
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "SELECT * FROM users WHERE `username` = \"" + uname + "\"";
            String sql2 = "SELECT * FROM plan WHERE `uname`= \"" + uname + "\"ORDER BY id DESC ";
            //SELECT MAX(id),uname* FROM plan GROUP BY uname WHERE `uname`
            Statement st = con.createStatement();
            Statement st2 = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            ResultSet rs2 = st2.executeQuery(sql2);
            rs.next();
            rs2.next();
            userInfo.user_id = rs.getInt("id");
            userInfo.fullname = rs.getString("fullname");
            userInfo.username = rs.getString("username");
            userInfo.password = rs.getString("password");
            userInfo.email = rs.getString("email");
            userInfo.height = rs.getInt("height");
            userInfo.weight = rs.getInt("weight");
            userInfo.birthday = rs.getDate("birthday");
            userInfo.gender = rs.getString("gender");
            userInfo.disease = rs2.getString("special_dis");
            userInfo.nutrient = rs2.getString("nutrient");

            st.close();
            st2.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return userInfo;
    }
    public NowPlanInfo getnowplanData(String uname) {
        NowPlanInfo nowplan = new NowPlanInfo();
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql2 = "SELECT * FROM plan WHERE `uname` = \"" + uname + "\" AND final_weight =0";
            Statement st2 = con.createStatement();
            ResultSet rs2 = st2.executeQuery(sql2);

            rs2.next();
            nowplan.nowstartdate = rs2.getString("startdate");
            nowplan.nowplan_weight = rs2.getDouble("plan_weight");
            nowplan.nowplan_weightnow = rs2.getDouble("plan_weightnow");
            st2.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return nowplan;
    }

    public List<FoodInfo> getFoodData(int calories) {
        List<FoodInfo> foodInfo = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "SELECT * FROM food_seven WHERE `熱量` <= " + calories;
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                FoodInfo result = new FoodInfo();
                result.food_id = rs.getInt("food_id");
                result.title = rs.getString("title");
                result.categories = rs.getString("categories");
                result.tags = rs.getString("tags");
                result.weight = rs.getDouble("重量(g)");
                result.calories = rs.getDouble("熱量");
                result.protein = rs.getDouble("蛋白質(g)");
                result.carbs = rs.getDouble("碳水化合物(g)");
                result.fat = rs.getDouble("脂肪(g)");
                result.sugar = rs.getDouble("糖");
                result.sodium = rs.getDouble("鈉");
                result.image = rs.getString("image");
                foodInfo.add(result);
            }
            st.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return foodInfo;
    }
    public List<PlanInfo> getPlanData(String uname) {
        List<PlanInfo> planInfo = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);
            String sql = "SELECT * FROM plan WHERE `uname` = \"" + uname + "\" AND final_weight !=0";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                PlanInfo presult = new PlanInfo();
                presult.enddate = rs.getString("end_date");
                presult.startdate = rs.getString("startdate");
                presult.plan_weight = rs.getDouble("plan_weight");
                presult.plan_weightnow = rs.getDouble("plan_weightnow");
                presult.final_weight = rs.getDouble("final_weight");
                planInfo.add(presult);
            }
            st.close();

        } catch(SQLException e) {
            e.printStackTrace();
        }
        return planInfo;
    }

    public void updateRating(int userId, int foodId) {
        try {
            Connection con = DriverManager.getConnection(url, db_user, db_password);

            String sql = "INSERT INTO food_rating(user_id, food_id, rating) VALUES (" +
                    userId + "," + foodId + ",1) ON DUPLICATE KEY UPDATE rating = rating + 1";
            Statement st = con.createStatement();
            st.executeUpdate(sql);
            Log.i("OK", "user_id = " + userId + ", food_id = " + foodId + " -> Rating updated. ");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

}
