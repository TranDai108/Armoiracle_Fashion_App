package com.example.armoiraclefashionapp

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import com.example.armoiraclefashionapp.api.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.HttpException


class QuizStartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_start)
        lifecycleScope.launch {
            try{
                val questions = RetrofitInstance.api.getQuests()
                // Xử lý nút "Let's go"
                findViewById<Button>(R.id.button_letgo).setOnClickListener {
                    val intent = Intent(this@QuizStartActivity, QuizQuestsActivity::class.java)
                    intent.putParcelableArrayListExtra("questions", ArrayList(questions))
                    startActivity(intent)

                }
                // Xử lý nút "Cancel"
                findViewById<Button>(R.id.button_cancel).setOnClickListener {
                    onBackPressedDispatcher.onBackPressed() // Quay về màn hình trước đó
                }
            } catch (_: HttpException){

            }
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val config = Configuration(newBase?.resources?.configuration)
        config.fontScale = 1.0f
        applyOverrideConfiguration(config)
        super.attachBaseContext(newBase)
    }
}