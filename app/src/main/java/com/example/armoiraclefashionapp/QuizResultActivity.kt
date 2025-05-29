package com.example.armoiraclefashionapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.constraintlayout.widget.ConstraintLayout
import android.widget.ImageView
import android.widget.TextView

class QuizResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_result)

        // Danh sách các background để random
        val backgroundList = listOf(
            R.drawable.result_jun_bg,
            R.drawable.result_toki_bg,
            R.drawable.result_kiba_bg,
            R.drawable.result_sora_bg,
            R.drawable.result_hikari_bg
        )

        // Logic de xu ly ket qua nhom tinh cach nao
        val randomBackground = backgroundList.random()
        findViewById<ConstraintLayout>(R.id.main).setBackgroundResource(randomBackground)
        val btn_back = findViewById<ImageButton>(R.id.back_button)
        btn_back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Xác định mô tả dựa trên background được chọn
        val description = when (randomBackground) {
            R.drawable.result_jun_bg -> "Nhóm truyền thống, gọn gàng → Tượng trưng cho sự tinh giản, nền nếp, thanh lịch."
            R.drawable.result_toki_bg -> "Nhóm năng động, thực tế → Nhịp sống nhanh, luôn vận động, biết tận dụng thời cơ."
            R.drawable.result_kiba_bg -> "Nhóm lãnh đạo, quyết đoán → Biểu tượng của sức mạnh, quyền lực và bản lĩnh dẫn dắt."
            R.drawable.result_sora_bg -> "Nhóm sáng tạo, phóng khoáng → Đại diện cho sự tự do, bay bổng, trí tưởng tượng không giới hạn."
            R.drawable.result_hikari_bg -> "Nhóm thân thiện, ấm áp → Ánh sáng dịu dàng, lan tỏa sự ấm áp và tích cực đến mọi người."
            else -> "Khám phá bản thân bạn!"
        }
        // Cập nhật UI
        findViewById<TextView>(R.id.personality_description).text = description
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}