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
import android.widget.*
import androidx.core.text.set
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.loginregister.home.HomeActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


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

    var textCalorie1: TextInputLayout? = null
    //var textProtein1: TextInputLayout? = null
    //var textFat1: TextInputLayout? = null
    //var textCarbohydrate1: TextInputLayout? = null

    var result: String? = null
    var textView: TextView? = null
    var button: Button? = null

    var target: String? = null

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

        textCalorie1 = view.findViewById<View>(R.id.ed_calorie) as TextInputLayout
       // textProtein1 = view.findViewById<View>(R.id.ed_protein) as TextInputLayout
       // textFat1 = view.findViewById<View>(R.id.ed_fat) as TextInputLayout
       // textCarbohydrate1 = view.findViewById<View>(R.id.ed_carbohydrate) as TextInputLayout

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

            // 按鈕事件
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
/*
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

 */

                }
                override fun afterTextChanged(s: Editable?) {
                //    println("進入2")
                    target=textFoodName?.text.toString()

                    // 宣告執行緒
                    val thread: Thread = Thread(mThread)
                    thread.start() // 開始執行
                 //   println(target)
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

    /* ======================================== */ // 建立一個執行緒執行的事件取得網路資料
    // Android 有規定，連線網際網路的動作都不能再主線程做執行
    private var mThread: Runnable? = Runnable {
        try {

            val url = URL("http://192.168.1.72/GetData.php?foodname=$target")
            // 開始宣告 HTTP 連線需要的物件，這邊通常都是一綑的
            val connection = url.openConnection() as HttpURLConnection
            // 建立 Google 比較挺的 HttpURLConnection 物件
            connection.requestMethod = "POST"
            // 設定連線方式為 POST
            connection.doOutput = true // 允許輸出
            connection.doInput = true // 允許讀入
            connection.useCaches = false // 不使用快取
            connection.connect() // 開始連線
            val responseCode = connection.responseCode // 建立取得回應的物件
            if (responseCode == HttpURLConnection.HTTP_OK) {


                // 如果 HTTP 回傳狀態是 OK ，而不是 Error
                val inputStream = connection.inputStream
                // 取得輸入串流
                val bufReader = BufferedReader(InputStreamReader(inputStream, "utf-8"), 8)
                // 讀取輸入串流的資料
                var box = "" // 宣告存放用字串
                var line: String? = null // 宣告讀取用的字串
                while (bufReader.readLine().also { line = it } != null) {
                    box += """
                        $line
                        
                        """.trimIndent() // 每當讀取出一列，就加到存放字串後面
                }
                inputStream.close() // 關閉輸入串流


                var dataIndex:Int? = 1
                val targetLen = target!!.length
                val targetData = FloatArray(6)
                val getSubstring = box.substring(
                    box.indexOf(target!!)+targetLen ,
                    box.indexOf(target!!) + targetLen + 60
                ) // 邊界處理很重要!!!

                val targetSubstring=getSubstring.split(',',':','熱','量','"',' ', '蛋','白','質','(',')','g','脂','肪','碳','水','化','合','物','{','}').toTypedArray()
                val tkk=targetSubstring
                  // 8、20、31、45
                    textCalorie1!!.editText!!.setText(tkk[8])
                //    textProtein1!!.editText!!.setText(tkk[20])
                //    textFat1!!.editText!!.setText(tkk[31])
                //    textCarbohydrate1!!.editText!!.setText(tkk[45])


            }
            // 讀取輸入串流並存到字串的部分
            // 取得資料後想用不同的格式
            // 例如 Json 等等，都是在這一段做處理
        } catch (e: java.lang.Exception) {
            result = e.toString() // 如果出事，回傳錯誤訊息
        }

        // 當這個執行緒完全跑完後執行
        activity?.runOnUiThread(Runnable {
     //     print("跑完了")
        })
    }

    private fun Button.setOnClickListener(eeee: Any) {

    }

}
