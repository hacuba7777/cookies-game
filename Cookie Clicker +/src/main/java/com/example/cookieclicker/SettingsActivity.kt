package com.example.cookieclicker

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import com.google.gson.Gson

class SettingsActivity:AppCompatActivity() {
    lateinit var cookieData : CookieData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cookieData = intent.getSerializableExtra("cookieData") as CookieData
        setContentView(R.layout.settings_activity)
        setupLoadButton()
        setupExitButton()
        setupSaveButton()
    }

    fun setupSaveButton() {
        var button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            val json = Gson().toJson(cookieData)
            editor.putString("COOKIE_DATA_KEY", json)
            editor.commit()

            Toast.makeText(applicationContext, "儲存成功", Toast.LENGTH_LONG).show()
        }
    }

    fun setupLoadButton() {
        var button = findViewById<Button>(R.id.button3)
        button.setOnClickListener {
            val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
            val json = sharedPref.getString("COOKIE_DATA_KEY", null)
            if (json != null) {
                val cookieData = Gson().fromJson(json, CookieData::class.java) as CookieData
                println(cookieData)
                var returnIntent = Intent()
                returnIntent.putExtra("cookieData", cookieData)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
                Toast.makeText(applicationContext, "讀取成功", Toast.LENGTH_LONG).show()
                println("abc")
            }
            else{
                Toast.makeText(applicationContext, "讀取失敗", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun setupExitButton() {
        var exitButton = findViewById<Button>(R.id.exitButton)
        exitButton.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
