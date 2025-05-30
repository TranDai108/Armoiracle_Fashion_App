package com.example.armoiraclefashionapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.constraintlayout.widget.ConstraintLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.armoiraclefashionapp.api.RetrofitInstance
import com.example.armoiraclefashionapp.models.UserStyleCreate
import kotlinx.coroutines.launch

class QuizResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_result)

        val sharedPreferences = getSharedPreferences("dataLogIn", MODE_PRIVATE)
        val iduser = sharedPreferences.getString("id", "")
        val matchPersonality = intent.getStringExtra("match_personality") ?: ""
        lifecycleScope.launch {
            try {
                val styles = RetrofitInstance.api.getStylesByPersonality(matchPersonality)
                styles.forEach {
                    RetrofitInstance.api.createUserStyle(UserStyleCreate(iduser!!, it))
                }
            } catch (e: Exception) {
                Log.e("QuizResultActivity", "Error fetching data: ${e.message.toString()}")
            }
        }

        // Ánh xạ tên personality → resource drawable
        val backgroundMap = mapOf(
            "Jun" to R.drawable.result_jun_bg,
            "Toki" to R.drawable.result_toki_bg,
            "Kiba" to R.drawable.result_kiba_bg,
            "Sora" to R.drawable.result_sora_bg,
            "Hikari" to R.drawable.result_hikari_bg
        )

        // Lấy background theo matchPersonality
        val randomBackground = backgroundMap[matchPersonality] ?: R.drawable.result_jun_bg

        // Cập nhật background
        findViewById<ConstraintLayout>(R.id.main).setBackgroundResource(randomBackground)

        // Xử lý back button
        findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Mô tả tương ứng với background
        val description = when (randomBackground) {
            R.drawable.result_jun_bg -> "Nhóm truyền thống, gọn gàng → Tượng trưng cho sự tinh giản, nền nếp, thanh lịch."
            R.drawable.result_toki_bg -> "Nhóm năng động, thực tế → Nhịp sống nhanh, luôn vận động, biết tận dụng thời cơ."
            R.drawable.result_kiba_bg -> "Nhóm lãnh đạo, quyết đoán → Biểu tượng của sức mạnh, quyền lực và bản lĩnh dẫn dắt."
            R.drawable.result_sora_bg -> "Nhóm sáng tạo, phóng khoáng → Đại diện cho sự tự do, bay bổng, trí tưởng tượng không giới hạn."
            R.drawable.result_hikari_bg -> "Nhóm thân thiện, ấm áp → Ánh sáng dịu dàng, lan tỏa sự ấm áp và tích cực đến mọi người."
            else -> "Khám phá bản thân bạn!"
        }

        findViewById<TextView>(R.id.personality_description).text = description

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
