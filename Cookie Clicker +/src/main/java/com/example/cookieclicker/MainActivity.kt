package com.example.cookieclicker

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import java.awt.font.TextAttribute.BACKGROUND
import java.lang.Thread
import kotlin.reflect.typeOf

class MainActivity : AppCompatActivity() {
    var cookieData = CookieData()
    val upgradeRequestCode = 10
    val handler = Handler()
    lateinit var timer: Runnable
    var enableGadgets = true
    val secondInMillis = 1000L
    var gTimeout = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupClickButton()
        setupUpgradeButton()
        initTimer()
        dice_roller()
        setupSettingsButton()
        rankButton()
        val leave=findViewById<Button>(R.id.leave)
        leave.setOnClickListener{
            AlertDialog.Builder(this)
                .setTitle("----------------------")
                .setMessage("你要離開喔?")
                .setNegativeButton("不捨得離開"){ dialog,which->1
                }
                .setPositiveButton("忍痛離開"){ dialog,which->1
                    finish()
                }.show()
        }
        Log.d("MainActivity2", "onCreate called")

    }

    fun dice_roller(){
        val btn_show_popup = findViewById<Button>(R.id.btn_show_popup)
        btn_show_popup.setOnClickListener {
            val popup = PopupMenu(this, btn_show_popup)
            popup.inflate(R.menu.popup)
            popup.setOnMenuItemClickListener {
                Toast.makeText(this, "Item : " + it.title, Toast.LENGTH_SHORT).show()
                true
            }
            popup.show()
        }
    }

    fun showGIF(){

        val imageView:ImageView = findViewById(R.id.fireworks)
        Glide.with(this).load(R.drawable.firework).into(imageView)
        imageView.setVisibility(View.VISIBLE)
        object : CountDownTimer(3000, 3000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                imageView.setVisibility(View.INVISIBLE)
            }
        }.start()

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setupClickButton() {
        val button = findViewById<ImageButton>(R.id.cookieButton)
        val auto_click=findViewById<Button>(R.id.auto_click)
        auto_click.setOnClickListener {
            if (cookieData.cookiesCounter.toInt() % 50 == 0){
                showGIF()
            }
            object : CountDownTimer(5000, 100) {
                override fun onTick(millisUntilFinished: Long) {
                    cookieData.cookiesCounter += cookieData.clickValue
                    refreshCookieView()
                    println(cookieData.cookiesCounter)
                }
                override fun onFinish() {
                    // 計時器結束後要執行的程式碼
                }
            }.start()
        }
        button.setOnClickListener {
            if (cookieData.cookiesCounter.toInt() % 50 == 0){
                showGIF()
            }
            cookieData.cookiesCounter += cookieData.clickValue
            if (button.drawable == getDrawable(R.drawable.cookie_cry)) {
                button.setImageDrawable(getDrawable(R.drawable.cookie))
            } else {
                button.setImageDrawable(getDrawable(R.drawable.cookie_cry))
            }
            refreshCookieView()
            handler.postDelayed({
                button.setImageResource(R.drawable.cookie)
            }, 800)
        }

    }

    fun setupUpgradeButton() {
        val button = findViewById<Button>(R.id.upgradeButton)
        button.setOnClickListener {
            val intent = Intent(this, UpgradesActivity::class.java)
            intent.putExtra("cookieData", cookieData)
            startActivityForResult(intent, upgradeRequestCode)
        }
    }
    fun rankButton() {
        val button = findViewById<Button>(R.id.rank)
        button.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            intent.putExtra("cookieData", cookieData)
            startActivityForResult(intent, upgradeRequestCode)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (true) {//resultCode == Activity.RESULT_OK
            cookieData = data!!.getSerializableExtra("cookieData") as CookieData
            println(cookieData)
            refreshCookieView()
            var button = findViewById<ImageButton>(R.id.cookieButton)
            var background = findViewById<ConstraintLayout>(R.id.background)
            cookieData.startBakeryBonus(handler , button, background)
        }
    }

    fun setupSettingsButton(){
        var button = findViewById<Button>(R.id.settingsButton)
        button.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("cookieData", cookieData)
            startActivityForResult(intent, upgradeRequestCode)
        }
    }

    fun initTimer() {
        timer = Runnable {
            gTimeout += 1
                if(gTimeout > 80)
                {
                    enableGadgets = true
                    gTimeout = 0
                }
                if(cookieData.cookiesCounter > 8000)
                {
                   var rnds = (0..52).random()
                   if(rnds == 30 || rnds == 31 || rnds == 32 || rnds == 33 || rnds == 34 || rnds == 35 || rnds == 36 || rnds == 37 || rnds == 38 || rnds == 39 || rnds == 40)
                   {
                       refreshCookieView()
                       var button = findViewById<ImageButton>(R.id.cookieButton)
                       var background = findViewById<ConstraintLayout>(R.id.background)
                       cookieData.startBakeryBonus(handler , button, background)
                   }
                }
                cookieData.cookiesCounter += cookieData.cookiesPerSecond
                refreshCookieView()
                handler.postDelayed(timer, secondInMillis)
        }
        handler.postDelayed(timer, secondInMillis)
    }


    fun refreshCookieView() {
        val cookiesTextView: TextView = findViewById(R.id.cookieTextArea)
        cookiesTextView.text = "🍪Cookies : ${cookieData.cookiesCounter}"
    }

}


