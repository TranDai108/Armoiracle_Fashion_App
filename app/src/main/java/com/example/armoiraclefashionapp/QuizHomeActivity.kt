package com.example.armoiraclefashionapp

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class QuizHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_home)

        findViewById<Button>(R.id.button_unveil).setOnClickListener(){
            val intent = Intent(this, QuizStartActivity::class.java)
            startActivity(intent)
        }

        val textView = findViewById<TextView>(R.id.text_customize)
        textView.paintFlags = textView.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}