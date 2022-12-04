package com.example.loginregister

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.loginregister.home.HomeActivity
import com.google.android.material.textfield.TextInputEditText


class InsertFragment : Fragment() {
    private var items: ArrayList<String> = ArrayList()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var dbrw: SQLiteDatabase

    var btnInsert: Button? = null
    var btnUpdate: Button? = null
    var btnDelete: Button? = null
    var textFoodName: TextInputEditText? = null
    var textCalorie: TextInputEditText? = null
    var textProtein: TextInputEditText? = null
    var textFat: TextInputEditText? = null
    var textCarbohydrate: TextInputEditText? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_insert, container, false)
        super.onCreate(savedInstanceState)

        textFoodName = view.findViewById<View>(R.id.ed_food_name1) as TextInputEditText
        textCalorie = view.findViewById<View>(R.id.ed_calorie1) as TextInputEditText
        textProtein = view.findViewById<View>(R.id.ed_protein1) as TextInputEditText
        textFat = view.findViewById<View>(R.id.ed_fat1) as TextInputEditText
        textCarbohydrate = view.findViewById<View>(R.id.ed_carbohydrate1) as TextInputEditText
        btnInsert = view.findViewById(R.id.btn_insert);
        btnUpdate = view.findViewById(R.id.btn_update);
        btnDelete = view.findViewById(R.id.btn_delete);

        //取得資料庫實體
        dbrw = insert_food_DB(this.requireContext() as HomeActivity).writableDatabase
        //宣告 Adapter 並連結 ListView ! 好用!
        adapter = ArrayAdapter(requireActivity(),
            android.R.layout.simple_list_item_1, items)
        view.findViewById<ListView>(R.id.listView).adapter = adapter
        //設定監聽器
        setListener()
        // Inflate the layout for this fragment
        return view
    }

    override fun onDestroy() {
        dbrw.close() //關閉資料庫
        super.onDestroy()
    }

    //設定監聽器
    private fun setListener() {

        btnInsert?.setOnClickListener {

            // 點擊新增按鈕 自動收回鍵盤
            val imm: InputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity?.window?.decorView?.windowToken, 0);

            //判斷是否有填入品名或熱量
            if (textFoodName!!.length() < 1 || textCalorie!!.length() < 1)
                showToast("品名、熱量欄位請勿留空")
            else
                try {
                    //新增一筆紀錄於 myFoodTable 資料表
                    if (textCalorie != null) {
                        if (textProtein != null) {
                            if (textFat != null) {
                                if (textCarbohydrate != null) {
                                    dbrw.execSQL(
                                        "INSERT INTO myFoodTable(food_name, calorie, protein, fat, carbohydrate) VALUES(?,?,?,?,?)",
                                        arrayOf(
                                            textFoodName!!.text.toString(),
                                            textCalorie!!.text,
                                            textProtein!!.text,
                                            textFat!!.text,
                                            textCarbohydrate!!.text)
                                    )
                                    showToast("新增:${textFoodName!!.text},熱量:${textCalorie!!.text},蛋白質:${textProtein!!.text},脂肪:${textFat!!.text},醣類:${textCarbohydrate!!.text}")
                                    cleanEditText()
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    //showToast("新增失敗，請正確輸入")
                }
        }

        btnUpdate?.setOnClickListener {

            // 點擊修改按鈕 自動收回鍵盤
            val imm: InputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity?.window?.decorView?.windowToken, 0);

            //判斷是否有填入品名或熱量
            if (textFoodName != null) {
                if (textCalorie != null) {
                    if (textFoodName!!.length() < 1 || textCalorie!!.length() < 1)
                        showToast("品名、熱量欄位請勿留空")
                    else
                        try {
                            //尋找相同品名的紀錄並更新 各欄位的值
                            if (textProtein != null) {
                                if(textProtein!!.length()>0)
                                    dbrw.execSQL("UPDATE myFoodTable SET calorie=${textCalorie!!.text}, protein = ${textProtein!!.text} WHERE food_name LIKE '${textFoodName!!.text}'")
                            }
                            if (textFat != null) {
                                if(textFat!!.length()>0)
                                    dbrw.execSQL("UPDATE myFoodTable SET calorie=${textCalorie!!.text}, fat = ${textFat!!.text} WHERE food_name LIKE '${textFoodName!!.text}'")
                            }
                            if (textCarbohydrate != null) {
                                if(textCarbohydrate!!.length()>0)
                                    dbrw.execSQL("UPDATE myFoodTable SET calorie=${textCalorie!!.text}, carbohydrate = ${textCarbohydrate!!.text} WHERE food_name LIKE '${textFoodName!!.text}'")
                                else
                                    dbrw.execSQL("UPDATE myFoodTable SET calorie=${textCalorie!!.text} WHERE food_name LIKE '${textFoodName!!.text}'")
                            }

                            if (textProtein != null) {
                                if (textFat != null) {
                                    if (textCarbohydrate != null) {
                                        showToast("更新:${textFoodName!!.text},熱量:${textCalorie!!.text},蛋白質:${textProtein!!.text},脂肪:${textFat!!.text},醣類:${textCarbohydrate!!.text}")
                                        cleanEditText()
                                    }
                                }
                            }
                        } catch (e: Exception) {
                          //  showToast("更新失敗,請檢查輸入")
                        }
                }
            }
        }

        btnDelete?.setOnClickListener {

            // 點擊刪除按鈕 自動收回鍵盤
            val imm: InputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity?.window?.decorView?.windowToken, 0);

            //判斷是否有填入品名
            if (textFoodName != null) {
                if (textFoodName!!.length() < 1)
                    showToast("品名請勿留空")
                else
                    try {
                        //從 myFoodTable 資料表刪除相同品名的紀錄
                        dbrw.execSQL("DELETE FROM myFoodTable WHERE food_name LIKE '${textFoodName!!.text}'")
                        showToast("刪除:${textFoodName!!.text}")
                        cleanEditText()
                    } catch (e: Exception) {
                     //   showToast("刪除失敗:$e")
                    }
            }
        }

        // 使用此技術，可以拔掉一個查詢按鈕，增加使用者體驗度
        // 即時更新listview(輸入不用enter即查詢資料庫內的資料，輸入時即可動態查詢!)
        if (textFoodName != null) {
            textFoodName!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    //若無輸入品名則 SQL 語法為查詢全部菜色，反之查詢該品名資料
                    val queryString = "SELECT * FROM myFoodTable WHERE food_name LIKE '%${textFoodName!!.text}%'"
                    val c = dbrw.rawQuery(queryString, null)

                    c.moveToFirst() //從第一筆開始輸出
                    //hi
                    items.clear() //清空舊資料
                  //  showToast("共有${c.count}筆資料")
                    for (i in 0 until c.count) {
                        //加入新資料
                        items.add("品名:${c.getString(0)}\t\t\t\t\t\t\t\t\t\t 熱量:${c.getInt(1)}")
                        c.moveToNext() //移動到下一筆
                    }
                    adapter.notifyDataSetChanged() //更新列表資料
                    c.close() //關閉 Cursor
                }
                override fun afterTextChanged(s: Editable?) {

                }
            })
        }
    }

    //建立 showToast 方法顯示 Toast 訊息
    private fun showToast(text: String) =
        Toast.makeText(requireActivity(),text, Toast.LENGTH_SHORT).show()

    //清空輸入的品名與各欄位值
    private fun cleanEditText() {
        textFoodName?.setText("")
        textCalorie?.setText("")
        textProtein?.setText("")
        textFat?.setText("")
        textCarbohydrate?.setText("")
    }

    private fun Button.setOnClickListener(eeee: Any) {

    }

}