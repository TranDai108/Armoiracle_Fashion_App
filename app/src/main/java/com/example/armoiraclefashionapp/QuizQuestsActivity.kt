package com.example.armoiraclefashionapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.armoiraclefashionapp.api.RetrofitInstance
import com.example.armoiraclefashionapp.models.AnswerResponse
import com.example.armoiraclefashionapp.models.QuestionResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class QuizQuestsActivity : AppCompatActivity() {
    private var currentQuestion = 0
    private val totalQuestions = 10
    private var personalties = mutableMapOf(
        "Jun" to 0,
        "Toki" to 0,
        "Kiba" to 0,
        "Sora" to 0,
        "Hikari" to 0
    )
    private lateinit var allQuestions: ArrayList<QuestionResponse>
    private lateinit var questions: List<QuestionResponse>
    private lateinit var allAnswers: List<List<AnswerResponse>>
    private var isDone = false

    private val illustrations = listOf(
        R.drawable.q1_illustration, R.drawable.q2_illustration, R.drawable.q3_illustration,
        R.drawable.q4_illustration, R.drawable.q5_illustration, R.drawable.q6_illustration,
        R.drawable.q7_illustration, R.drawable.q8_illustration, R.drawable.q9_illustration,
        R.drawable.q10_illustration
    )

    private val backgrounds = listOf(
        R.drawable.q1_bg, R.drawable.q2_bg, R.drawable.q3_bg,
        R.drawable.q4_bg, R.drawable.q5_bg, R.drawable.q6_bg,
        R.drawable.q7_bg, R.drawable.q8_bg, R.drawable.q9_bg,
        R.drawable.q10_bg
    )

    private val userAnswers = MutableList(totalQuestions) { "" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz_quests)

        // Nhận danh sách câu hỏi từ Intent
        allQuestions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra("questions", QuestionResponse::class.java) ?: arrayListOf()
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra("questions") ?: arrayListOf()
        }

        questions = if (allQuestions.size >= totalQuestions) {
            allQuestions.shuffled().take(totalQuestions)
        } else allQuestions

        // Tải tất cả câu trả lời cho các câu hỏi đã chọn
        lifecycleScope.launch {
            allAnswers = questions.map { question ->
                async {
                    try {
                        RetrofitInstance.api.getAnswers(question.idques)
                    } catch (e: Exception) {
                        emptyList()
                    }
                }
            }.awaitAll()

            // Sau khi có dữ liệu, cập nhật UI
            updateUI()
        }

        setupUIEvents()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupUIEvents() {
        findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            if (currentQuestion > 0) {
                currentQuestion--
                updateUI()
            }
        }

        findViewById<ImageButton>(R.id.next_button).setOnClickListener {
            if (currentQuestion < totalQuestions - 1) {
                currentQuestion++
                updateUI()
            } else {
                if (userAnswers.any { it.isEmpty() }) {
                    Toast.makeText(this, "Vui lòng trả lời hết tất cả câu hỏi!", Toast.LENGTH_SHORT).show()
                } else {
                    val match_personality = personalties.maxByOrNull { it.value }?.key
                    showResult(match_personality ?: "Không xác định")
                }
            }
        }

        val answerButtons = listOf(
            findViewById<Button>(R.id.button_ans_1),
            findViewById<Button>(R.id.button_ans_2),
            findViewById<Button>(R.id.button_ans_3)
        )

        answerButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                if (index >= allAnswers[currentQuestion].size) return@setOnClickListener

                // Ghi nhận câu trả lời
                val selectedAnswer = allAnswers[currentQuestion][index]
                userAnswers[currentQuestion] = selectedAnswer.answer

                // Nếu người dùng đã chọn trước đó, rollback điểm personality cũ (nếu cần thiết)
                // Bỏ qua nếu bạn không cần tính lại điểm khi người dùng quay lại

                lifecycleScope.launch {
                    try {
                        val responses = RetrofitInstance.api.getPersonal(selectedAnswer.idans)

                        // Tăng điểm cho personality tương ứng
                        responses.forEach { response ->
                            val name = response.name
                            if (personalties.containsKey(name)) {
                                personalties[name] = personalties[name]!! + 1
                            }
                        }

                        // Nếu là câu cuối cùng và tất cả câu đã trả lời → hiện kết quả
                        if (currentQuestion == totalQuestions - 1 && userAnswers.none { it.isEmpty() }) {
                            isDone = true
                            val match_personality = personalties.maxByOrNull { it.value }?.key
                            showResult(match_personality ?: "Không xác định")
                        } else {
                            // Nếu chưa phải câu cuối → chuyển sang câu tiếp
                            currentQuestion++
                            updateUI()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@QuizQuestsActivity, "Lỗi khi lấy dữ liệu personality", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        findViewById<SeekBar>(R.id.progress_bar).setOnTouchListener { _, _ -> true }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_quiz_quests)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        val progress = ((currentQuestion + 1).toFloat() / totalQuestions * 100).toInt()
        findViewById<ProgressBar>(R.id.progress_bar).progress = progress

        findViewById<ConstraintLayout>(R.id.activity_quiz_quests)
            .setBackgroundResource(backgrounds[currentQuestion])

        findViewById<TextView>(R.id.question_number).text = "Câu hỏi ${currentQuestion + 1}"
        findViewById<ImageView>(R.id.imageView).setImageResource(illustrations[currentQuestion])
        findViewById<TextView>(R.id.question_text).text = questions[currentQuestion].question

        val answerButtons = listOf(
            findViewById<Button>(R.id.button_ans_1),
            findViewById<Button>(R.id.button_ans_2),
            findViewById<Button>(R.id.button_ans_3)
        )

        allAnswers.getOrNull(currentQuestion)?.let { answers ->
            answerButtons.forEachIndexed { i, button ->
                button.text = answers.getOrNull(i)?.answer ?: "Không có đáp án"
                button.setBackgroundResource(
                    if (button.text == userAnswers[currentQuestion]) R.drawable.bg_button_selected
                    else R.drawable.bg_button_unveil
                )
            }
        }
    }

    private fun showResult(match_personality:String) {
        val intent = Intent(this, QuizResultActivity::class.java)
        intent.putExtra("match_personality", match_personality)
        startActivity(intent)
        finish()
    }

    override fun attachBaseContext(newBase: Context?) {
        val config = Configuration(newBase?.resources?.configuration)
        config.fontScale = 1.0f
        applyOverrideConfiguration(config)
        super.attachBaseContext(newBase)
    }
}
