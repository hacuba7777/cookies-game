package com.example.cookieclicker

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*

class MainActivity2 : AppCompatActivity() {




    var cookieData=CookieData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        cookieData = intent.getSerializableExtra("cookieData") as CookieData

        Log.d("MainActivity2", "onCreate called")
        //adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
      //  listView.adapter = adapter

        writableDB = MyDBHelper(this).writableDatabase
        val myDBHelper = MyDBHelper(this)

        myDBHelper.writableDB = writableDB

        setExitButton()
        loadData()
        clearTable()
        addData()
    }
    lateinit var writableDB: SQLiteDatabase
    private var items : ArrayList<String> = ArrayList()  //定義資料清單






    fun addData() {
        var goal=cookieData.cookiesCounter
        val name=findViewById<EditText>(R.id.user)
        val button=findViewById<Button>(R.id.button2)
        button.setOnClickListener {
            val username=name.text.toString()
            Toast.makeText(this, username + " added to database", Toast.LENGTH_LONG).show()
            val values = ContentValues().apply {
                put("username", username)
                put("goal", goal)
            }
            val newRowId =writableDB.insert("myTABL", null, values)
            if (newRowId == -1L) {
                Log.e("Database", "新增資料失敗")
            } else {
                Log.i("Database", "新增資料成功，ID 為 $newRowId")
            }
            loadData()
        }





    }
    fun clearTable() {
        var exitButton = findViewById<Button>(R.id.delete)
        exitButton.setOnClickListener {
            writableDB.execSQL("DELETE FROM myTABL")
            loadData()
        }

    }
    fun setExitButton(){
        var exitButton = findViewById<Button>(R.id.exitButton)
        exitButton.setOnClickListener {
            var returnIntent = Intent()
            returnIntent.putExtra("cookieData", cookieData)
            setResult(Activity.RESULT_OK, returnIntent)
            finish()

        }
    }
    fun loadData() {
        // 取得可寫入的資料庫
        val db = writableDB
        var listView=findViewById<ListView>(R.id.list1)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        listView.adapter = adapter
        // 執行查詢並取得游標
        val cursor = db.rawQuery("SELECT * FROM myTABL ORDER BY goal DESC", null)

        // 將查詢結果處理並存入 items 中
        items.clear()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val id = cursor.getInt(0)
            val username = cursor.getString(1)
            val goal = cursor.getLong(2)
            items.add("$username: $goal")
            cursor.moveToNext()

        }

        // 通知 adapter 更新資料
        adapter.notifyDataSetChanged()

        cursor.close()
    }

}
