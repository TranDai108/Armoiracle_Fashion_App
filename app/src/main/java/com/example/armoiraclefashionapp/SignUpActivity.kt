package com.example.armoiraclefashionapp

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.armoiraclefashionapp.api.RetrofitInstance
import com.example.armoiraclefashionapp.extensions.enablePasswordToggleWithIcon
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var isOkName = false
        var isOkUsername = false
        var isOkPassword = false
        var isOkConfirmPassword = false

        val edtFullName = findViewById<TextInputEditText>(R.id.edtFullName)
        val edtUsername = findViewById<TextInputEditText>(R.id.edtUsername)
        val edtPassword = findViewById<TextInputEditText>(R.id.edtPassword)
        val tvPasswordCriteria1 = findViewById<TextView>(R.id.tvPasswordCriteria1)
        val tvPasswordCriteria2 = findViewById<TextView>(R.id.tvPasswordCriteria2)
        val tvPasswordCriteria3 = findViewById<TextView>(R.id.tvPasswordCriteria3)
        val edtConfirmPassword = findViewById<TextInputEditText>(R.id.edtConfirmPassword)
        val rgGender = findViewById<RadioGroup>(R.id.rgGender)
        val btnContinue = findViewById<MaterialButton>(R.id.btnContinue)
        var gender = 0

        edtPassword.enablePasswordToggleWithIcon()
        edtConfirmPassword.enablePasswordToggleWithIcon()

        edtFullName.setOnFocusChangeListener { _, hasFocus ->
            val name = edtFullName.text.toString()
            isOkName = true
            if (!hasFocus) {
                if (name.isEmpty()) {
                    edtFullName.error = "Vui lòng nhập tên đầy đủ"
                    isOkName = false
                    return@setOnFocusChangeListener
                }

                if (!name.matches(Regex("^(\\p{L}+( \\p{L}+)*){2,50}$"))) {
                    edtFullName.error = "Tên không hợp lệ"
                    isOkName = false
                    return@setOnFocusChangeListener
                }
            }
        }

        edtUsername.setOnFocusChangeListener { _, hasFocus ->
            isOkUsername = true
            val username = edtUsername.text.toString()
            if (!hasFocus) {
                if (username.isEmpty()) {
                    edtUsername.error = "Vui lòng nhập username"
                    isOkUsername = false
                    return@setOnFocusChangeListener
                }

                if (!username.matches(Regex("^[\\p{L}\\p{N}\\p{P}]+$"))) {
                    edtUsername.error = "Username không hợp lệ"
                    isOkUsername = false
                    return@setOnFocusChangeListener
                }

                lifecycleScope.launch {
                    try {
                        RetrofitInstance.api.getUserBy("username", username)
                        edtUsername.error = "Username đã tồn tại"
                        isOkUsername = false
                    } catch (_: Exception) { }
                }
            }
        }

        edtPassword.addTextChangedListener {
            val password = edtPassword.text.toString()
            isOkPassword = true

            if (password.length < 8) {
                tvPasswordCriteria1.text = "❌ Mật khẩu phải có ít nhất 8 ký tự"
                tvPasswordCriteria1.setTextColor(Color.RED)
                isOkPassword = false
            } else {
                tvPasswordCriteria1.text = "✅ Mật khẩu phải có ít nhất 8 ký tự"
                tvPasswordCriteria1.setTextColor(Color.GREEN)
            }

            if (!password.matches(Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).+$"))) {
                tvPasswordCriteria2.text = "❌ Mật khẩu phải bao gồm chữ hoa, thường, số và ký tự đặc biệt"
                tvPasswordCriteria2.setTextColor(Color.RED)
                isOkPassword = false
            } else {
                tvPasswordCriteria2.text = "✅ Mật khẩu phải bao gồm chữ hoa, thường, số và ký tự đặc biệt"
                tvPasswordCriteria2.setTextColor(Color.GREEN)
            }

            if (password.contains(" ")) {
                tvPasswordCriteria3.text = "❌ Mật khẩu không được chứa khoảng trắng"
                tvPasswordCriteria3.setTextColor(Color.RED)
                isOkPassword = false
            } else {
                tvPasswordCriteria3.text = "✅ Mật khẩu không được chứa khoảng trắng"
                tvPasswordCriteria3.setTextColor(Color.GREEN)
            }
        }

        edtPassword.setOnFocusChangeListener { _, hasFocus ->
            isOkPassword = true
            if (!hasFocus) {
                if (edtPassword.text.isNullOrEmpty()) {
                    edtPassword.error = "Vui lòng nhập mật khẩu"
                    isOkPassword = false
                    return@setOnFocusChangeListener
                }
            }

            if (edtPassword.text.toString() != edtConfirmPassword.text.toString()) {
                isOkConfirmPassword = false
                edtConfirmPassword.error = "Mật khẩu không khớp"
            }
        }

        edtConfirmPassword.addTextChangedListener {
            isOkConfirmPassword = true
            if (edtPassword.text.toString() != edtConfirmPassword.text.toString()) {
                edtConfirmPassword.error = "Mật khẩu không khớp"
                isOkConfirmPassword = false
            }
        }

        edtConfirmPassword.setOnFocusChangeListener { _, hasFocus ->
            isOkConfirmPassword = true
            if (!hasFocus) {
                if (edtConfirmPassword.text.isNullOrEmpty()) {
                    edtConfirmPassword.error = "Vui lòng nhập lại mật khẩu"
                    isOkConfirmPassword = false
                    return@setOnFocusChangeListener
                }
            }
        }

        rgGender.setOnCheckedChangeListener { _, checkId ->
            when (checkId) {
                R.id.rbMale -> {
                    gender = 0
                }

                R.id.rbFemale -> {
                    gender = 1
                }

                R.id.rbOther -> {
                    gender = 2
                }
            }
        }

        btnContinue.setOnClickListener {
            if (isOkName && isOkUsername && isOkPassword && isOkConfirmPassword) {
                val bundle = Bundle()
                bundle.putString("name", edtFullName.text.toString())
                bundle.putString("username", edtUsername.text.toString())
                bundle.putString("password", edtPassword.text.toString())
                bundle.putInt("gender", gender)

                val intent = Intent(this, SignUpActivity1::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val config = Configuration(newBase?.resources?.configuration)
        config.fontScale = 1.0f
        applyOverrideConfiguration(config)
        super.attachBaseContext(newBase)
    }
}