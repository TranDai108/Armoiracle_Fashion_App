package com.example.armoiraclefashionapp

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.armoiraclefashionapp.api.RetrofitInstance
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val civAvatar = findViewById<CircleImageView>(R.id.civAvatar)
        val edtUsername = findViewById<TextInputEditText>(R.id.edtUsername)
        val edtName = findViewById<TextInputEditText>(R.id.edtName)
        val edtEmail = findViewById<TextInputEditText>(R.id.edtEmail)
        val edtPhone = findViewById<TextInputEditText>(R.id.edtPhone)
        val edtBirthday = findViewById<TextInputEditText>(R.id.edtBirthday)
        val edtHeight = findViewById<TextInputEditText>(R.id.edtHeight)
        val edtWeight = findViewById<TextInputEditText>(R.id.edtWeight)
        val rbMale = findViewById<RadioButton>(R.id.rbMale)
        val rbFemale = findViewById<RadioButton>(R.id.rbFemale)
        val rbOther = findViewById<RadioButton>(R.id.rbOther)
        val btnConfirm = findViewById<MaterialButton>(R.id.btnConfirm)
        val sharedPreferences = getSharedPreferences("dataLogIn", MODE_PRIVATE)
        val iduser = sharedPreferences.getString("id", "")
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val mode = intent.getIntExtra("mode", 0)
        if (mode == 0) {
            btnConfirm.visibility = MaterialButton.VISIBLE
        }

        lifecycleScope.launch {
            try {
                val user = RetrofitInstance.api.getUserBy("iduser", iduser!!)
                val date = inputFormat.parse(user.birthday)
                edtUsername.setText(user.username)
                edtName.setText(user.name)
                edtBirthday.setText(outputFormat.format(date!!))
                edtEmail.setText(user.email)
                edtPhone.setText(user.phone)
                val height = user.height ?: 0
                val weight = user.weight ?: 0
                edtHeight.setText(height.toString())
                edtWeight.setText(weight.toString())
                if (user.gender == 0) {
                    rbMale.isChecked = true
                } else if (user.gender == 1) {
                    rbFemale.isChecked = true
                } else {
                    rbOther.isChecked = true
                }
                Glide.with(this@EditProfileActivity)
                    .load(user.avatar)
                    .into(civAvatar)
            } catch (_: Exception) { }
        }

        if (mode == 1) {
            
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val config = Configuration(newBase?.resources?.configuration)
        config.fontScale = 1.0f
        applyOverrideConfiguration(config)
        super.attachBaseContext(newBase)
    }
}