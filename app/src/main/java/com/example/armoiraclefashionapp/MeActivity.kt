package com.example.armoiraclefashionapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import com.example.armoiraclefashionapp.api.RetrofitInstance
import kotlinx.coroutines.launch

class MeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_me)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPreferences = getSharedPreferences("dataLogIn", MODE_PRIVATE)
        val btnLogOut = findViewById<Button>(R.id.btnLogOut)
        val btnDelAcc = findViewById<Button>(R.id.btnDelAcc)

        val iduser = sharedPreferences.getString("id", "")

        btnLogOut.setOnClickListener {
            sharedPreferences.edit {
                remove("id")
                remove("remember")
                commit()
            }

            val intent = Intent(this, LogInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        btnDelAcc.setOnClickListener {
            if (iduser != "") {
                AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa tài khoản")
                    .setMessage("Bạn có chắc chắn muốn xóa tài khoản?")
                    .setPositiveButton("Có") { _, _ ->
                        lifecycleScope.launch {
                            try {
                                RetrofitInstance.api.deleteUser(iduser!!)
                            } catch (_: Exception) { }
                        }
                        sharedPreferences.edit {
                            remove("id")
                            remove("remember")
                            commit()
                        }
                        val intent = Intent(this, LogInActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                    .setNegativeButton("Không", null)
                    .show()
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