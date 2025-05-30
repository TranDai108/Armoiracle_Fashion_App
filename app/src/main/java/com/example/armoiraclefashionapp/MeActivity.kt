package com.example.armoiraclefashionapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.armoiraclefashionapp.api.RetrofitInstance
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        val tvUsername = findViewById<TextView>(R.id.tvUsername)
        val tvBirthGen = findViewById<TextView>(R.id.tvBirthGen)
        val civAvatar = findViewById<CircleImageView>(R.id.civAvatar)
        val editButton = findViewById<ImageButton>(R.id.ibEditInfo)
        val btnInfo = findViewById<Button>(R.id.btnInfo)
        val btnEditStyle = findViewById<Button>(R.id.btnEditStyle)
        val btnEditPassword = findViewById<Button>(R.id.btnEditPassword)
        val btnMyClothes = findViewById<Button>(R.id.btnMyClothes)
        val btnNotify = findViewById<Button>(R.id.btnNotify)
        val btnEditTheme = findViewById<Button>(R.id.btnEditTheme)

        val sharedPreferences = getSharedPreferences("dataLogIn", MODE_PRIVATE)
        val btnLogOut = findViewById<Button>(R.id.btnLogOut)
        val btnDelAcc = findViewById<Button>(R.id.btnDelAcc)

        val iduser = sharedPreferences.getString("id", "")
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        lifecycleScope.launch {
            try {
                val user = RetrofitInstance.api.getUserBy("iduser", iduser!!)
                val date = inputFormat.parse(user.birthday)
                var str = outputFormat.format(date!!)
                tvUsername.text = user.username
                Log.d("user", str)
                str += if (user.gender == 0) {
                    " ♂️"
                } else if (user.gender == 1) {
                    " ♀️"
                } else {
                    " ❓"
                }
                tvBirthGen.text = str
                Glide.with(this@MeActivity)
                    .load(user.avatar)
                    .into(civAvatar)
            } catch (_: Exception) { }
        }

        btnInfo.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            intent.putExtra("mode", 0)
            startActivity(intent)
        }

        btnEditStyle.setOnClickListener {
            val intent = Intent(this, EditStyleActivity::class.java)
            startActivity(intent)
        }

        editButton.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            intent.putExtra("mode", 1)
            startActivity(intent)
        }


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