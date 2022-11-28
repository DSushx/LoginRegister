package com.example.loginregister
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
//自訂建構子並繼承 SQLiteOpenHelper 類別
class insert_food_DB(
    context: InsertFragment,
    name: String = database,
    factory: SQLiteDatabase.CursorFactory? = null,
    version: Int = v
) : SQLiteOpenHelper(context.requireContext(), name, factory, version) {
    companion object {
        private const val database = "editFoodDB" //資料庫名稱
        private const val v = 4 //資料庫版本

    }
    override fun onCreate(db: SQLiteDatabase) {
        //建立 myFoodTable 資料表
        db.execSQL("CREATE TABLE myFoodTable(food_name TEXT NOT NULL, calorie REAL NOT NULL, protein REAL, fat REAL, carbohydrate REAL, ID INTEGER PRIMARY KEY AUTOINCREMENT, date date not null DEFAULT CURRENT_DATE)")
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int,
                           newVersion: Int) {
        //升級資料庫版本時，刪除舊資料表，並重新執行 onCreate()，建立新資料表
        db.execSQL("DROP TABLE IF EXISTS myFoodTable")
        onCreate(db)
    }
}