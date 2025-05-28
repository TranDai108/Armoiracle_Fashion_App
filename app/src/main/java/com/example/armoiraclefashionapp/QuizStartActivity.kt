package com.example.armoiraclefashionapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
class QuizStartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_start)

        // Xử lý nút "Let's go"
        findViewById<Button>(R.id.button_letgo).setOnClickListener {
            val intent = Intent(this, QuizQuestsActivity::class.java)
            startActivity(intent)
        }

        // Xử lý nút "Cancel"
        findViewById<Button>(R.id.button_cancel).setOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Quay về màn hình trước đó
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}