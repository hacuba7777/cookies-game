package com.example.cookieclicker

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDBHelper (context: Context) : SQLiteOpenHelper(context, name, null, version) {
    companion object {
        // 資料庫名稱
        val name = "db"
        // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
        val version = 1
    }
    lateinit var writableDB: SQLiteDatabase

    init {
        // 在建構子中初始化 writableDB
        writableDB = writableDatabase
    }

    // 定義函式來取得可寫入的 SQLiteDatabase 物件
    fun getDB(): SQLiteDatabase {
        return writableDB
    }
    override fun onCreate(db: SQLiteDatabase) {
        // 在這裡實作資料庫的建立
        val sql = "CREATE TABLE if not exists myTABL(id integer PRIMARY KEY AUTOINCREMENT, username text NOT NULL, goal long NOT NULL)"
        db.execSQL(sql)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // 在這裡實作資料庫的更新
        db.execSQL("DROP TABLE IF EXISTS myTABLE")
        onCreate(db)
    }


}