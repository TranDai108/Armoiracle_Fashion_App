package com.example.armoiraclefashionapp

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import android.widget.ProgressBar
import androidx.core.view.WindowInsetsCompat
import android.widget.Toast
import android.widget.SeekBar
import android.content.Intent

class QuizQuestsActivity : AppCompatActivity() {
    private var currentQuestion = 1
    private val totalQuestions = 10
    private val questions = listOf(
        "Câu hỏi mẫu: Bạn thích màu sắc nào nhất?",
        "Câu hỏi 2: Bạn thích hoạt động nào?",
        "Câu hỏi 3: Bạn thích thời tiết nào?",
        "Câu hỏi 4: Bạn thích món ăn nào?",
        "Câu hỏi 5: Bạn thích phong cách nào?",
        "Câu hỏi 6: Bạn thích loại nhạc nào?",
        "Câu hỏi 7: Bạn thích đi du lịch đâu?",
        "Câu hỏi 8: Bạn thích thú cưng nào?",
        "Câu hỏi 9: Bạn thích mùa nào nhất?",
        "Câu hỏi 10: Bạn thích nghề nghiệp nào?"
    )
    private val answers = listOf(
        listOf("Đỏ", "Vàng", "Xanh"),
        listOf("Đọc sách", "Thể thao", "Du lịch"),
        listOf("Nắng", "Mưa", "Mát mẻ"),
        listOf("Cơm", "Phở", "Bánh mì"),
        listOf("Hiện đại", "Cổ điển", "Tự nhiên"),
        listOf("Pop", "Rock", "Classical"),
        listOf("Núi", "Biển", "Thành phố"),
        listOf("Chó", "Mèo", "Chim"),
        listOf("Xuân", "Hạ", "Thu"),
        listOf("Lập trình", "Thiết kế", "Kinh doanh")
    )
    private val illustrations = listOf(
        R.drawable.q1_illustration,
        R.drawable.q2_illustration,
        R.drawable.q3_illustration,
        R.drawable.q4_illustration,
        R.drawable.q5_illustration,
        R.drawable.q6_illustration,
        R.drawable.q7_illustration,
        R.drawable.q8_illustration,
        R.drawable.q9_illustration,
        R.drawable.q10_illustration
    )
    private val backgrounds = listOf(
        R.drawable.q1_bg,
        R.drawable.q2_bg,
        R.drawable.q3_bg,
        R.drawable.q4_bg,
        R.drawable.q5_bg,
        R.drawable.q6_bg,
        R.drawable.q7_bg,
        R.drawable.q8_bg,
        R.drawable.q9_bg,
        R.drawable.q10_bg
    )

    // Danh sách để lưu câu trả lời của người dùng
    private val userAnswers = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_quests)
        repeat(totalQuestions) { userAnswers.add("") }

        // Cập nhật giao diện ban đầu
        updateUI()

        // Xử lý nút lùi
        findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            if (currentQuestion > 1) {
                currentQuestion--
                updateUI()
            }
        }

        // Xử lý nút tiến
        findViewById<ImageButton>(R.id.next_button).setOnClickListener {
            if (currentQuestion < totalQuestions) {
                currentQuestion++
                updateUI()
            } else {
                // Khi đến câu hỏi cuối, hiển thị thông báo và kết thúc
                if (userAnswers.count{ it.isNotEmpty() } == totalQuestions)
                {
                    showResult()
                    Toast.makeText(this, "Bạn đã hoàn thành khảo sát! Kết quả: $userAnswers", Toast.LENGTH_LONG).show()
                }
                else
                    Toast.makeText(this, "Bạn chưa hoàn thành toàn bộ câu hỏi, vui lòng điền đủ đáp án !", Toast.LENGTH_SHORT).show()

            }
        }

        // Khoa khong cho nguoi dung dieu chinh thanh seekbar
        findViewById<SeekBar>(R.id.progress_bar).setOnTouchListener { _, _ ->
            true // Chặn tất cả tương tác
        }

        // Xử lý lựa chọn câu trả lời
        val answerButtons = listOf(
            findViewById<Button>(R.id.button_ans_1),
            findViewById<Button>(R.id.button_ans_2),
            findViewById<Button>(R.id.button_ans_3)
        )
        answerButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                // Lưu câu trả lời của người dùng
                userAnswers[currentQuestion - 1] = answers[currentQuestion - 1][index]

                // Chuyển sang câu hỏi tiếp theo (nếu chưa phải câu cuối)
                if (currentQuestion < totalQuestions) {
                    currentQuestion++
                    updateUI()
                } else {
                    if (userAnswers.count{ it.isNotEmpty() } == totalQuestions)
                    {
                        showResult()
                        Toast.makeText(this, "Bạn đã hoàn thành khảo sát! Kết quả: $userAnswers", Toast.LENGTH_LONG).show()
                    }
                    else
                        Toast.makeText(this, "Bạn chưa hoàn thành toàn bộ câu hỏi, vui lòng điền đủ đáp án !", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Xử lý WindowInsets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_quiz_quests)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun updateUI() {
        // Cập nhật thanh tiến trình
        val progress = (currentQuestion.toFloat() / totalQuestions.toFloat() * 100).toInt()
        findViewById<ProgressBar>(R.id.progress_bar).progress = progress

        // Cập nhật background
        findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.activity_quiz_quests)
            ?.setBackgroundResource(backgrounds[currentQuestion - 1])

        // Cập nhật số thứ tự câu hỏi
        findViewById<TextView>(R.id.question_number).text = "Question $currentQuestion"

        // Cập nhật hình minh họa
        findViewById<ImageView>(R.id.imageView).setImageResource(illustrations[currentQuestion - 1])
        findViewById<ImageView>(R.id.imageView).contentDescription = "Question $currentQuestion - illustration"

        // Cập nhật câu hỏi
        findViewById<TextView>(R.id.question_text).text = questions[currentQuestion - 1]

        // Cập nhật các lựa chọn trả lời
        val answerButtons = listOf(
            findViewById<Button>(R.id.button_ans_1),
            findViewById<Button>(R.id.button_ans_2),
            findViewById<Button>(R.id.button_ans_3)
        )
        val currentAnswers = answers[currentQuestion - 1]
        for (i in answerButtons.indices) {
            answerButtons[i].text = currentAnswers[i]
        }

        // Hiển thị lại câu trả lời đã chọn (nếu có)
        val selectedAnswer = userAnswers[currentQuestion - 1]
        if (selectedAnswer.isNotEmpty()) {
            answerButtons.forEachIndexed { index, button ->
                if (button.text == selectedAnswer) {
                    // Có thể thêm hiệu ứng (ví dụ: đổi màu nền) để đánh dấu câu trả lời đã chọn
                    button.setBackgroundResource(R.drawable.bg_button_selected)
                } else {
                    button.setBackgroundResource(R.drawable.bg_button_unveil) // Khôi phục nền mặc định
                }
            }
        }
    }

    private fun showResult() {
        val intent = Intent(this, QuizResultActivity::class.java)
        startActivity(intent)
        finish() // Kết thúc QuizQuestsActivity
    }
}