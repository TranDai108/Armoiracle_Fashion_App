package com.example.armoiraclefashionapp

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.armoiraclefashionapp.api.RetrofitInstance
import com.example.armoiraclefashionapp.extensions.enablePasswordToggleWithIcon
import com.example.armoiraclefashionapp.models.UserLogIn
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import retrofit2.HttpException
import androidx.core.content.edit

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences("dataLogIn", MODE_PRIVATE)
        val remember = sharedPreferences.getBoolean("remember", false)
        if (remember) {
            val userID = sharedPreferences.getString("id", "")
            if (userID != null) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("id", userID)
                startActivity(intent)
            }
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_log_in)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val tvToSignUp = findViewById<TextView>(R.id.tvToSignUp)
        val edtUsername = findViewById<TextInputEditText>(R.id.edtUsername)
        val edtPassword = findViewById<TextInputEditText>(R.id.edtPassword)
        val ckbRemember = findViewById<CheckBox>(R.id.ckbRemember)
        val tvForgetPass = findViewById<TextView>(R.id.tvForgetPass)
        val btnLogIn = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnLogIn)
        val btnGoogleLogIn = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnGoogleLogIn)
        val btnFacebookLogIn = findViewById<com.google.android.material.button.MaterialButton>(R.id.btnFacebookLogIn)

        edtPassword.enablePasswordToggleWithIcon()

        btnLogIn.setOnClickListener {
            val username = edtUsername.text.toString()
            val password = edtPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                edtUsername.error = "Vui lòng nhập tên đăng nhập"
                edtPassword.error = "Vui lòng nhập mật khẩu"
            } else {
                lifecycleScope.launch {
                    try {
                        val userLogIn = UserLogIn(username, password)
                        val user = RetrofitInstance.api.logIn(userLogIn)
                        if (ckbRemember.isChecked) {
                            sharedPreferences.edit {
                                putString("id", user.iduser)
                                putBoolean("remember", ckbRemember.isChecked)
                                commit()
                            }
                        } else {
                            sharedPreferences.edit {
                                remove("id")
                                remove("remember")
                                commit()
                            }
                        }
                        val intent = Intent(this@LogInActivity, HomeActivity::class.java)
                        intent.putExtra("user", user)
                        startActivity(intent)
                    } catch (e: HttpException) {
                        Log.e("Lỗi", e.message.toString())
                        when (e.code()) {
                            401 -> {
                                edtPassword.error = "Mật khẩu không đúng"
                            }
                            else -> {
                                edtUsername.error = "Username không tồn tại"
                            }
                        }
                    }
                }
            }
        }

        tvToSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val config = Configuration(newBase?.resources?.configuration)
        config.fontScale = 1.0f
        applyOverrideConfiguration(config)
        super.attachBaseContext(newBase)
    }
}