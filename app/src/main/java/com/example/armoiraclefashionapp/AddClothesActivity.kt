package com.example.armoiraclefashionapp

import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.armoiraclefashionapp.api.RetrofitInstance
import com.example.armoiraclefashionapp.models.ClothesCreate
import com.example.armoiraclefashionapp.models.ClothesResponse
import com.example.armoiraclefashionapp.models.UserClothesCreate
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.skydoves.colorpickerview.ColorPickerView
import com.skydoves.colorpickerview.sliders.AlphaSlideBar
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AddClothesActivity : AppCompatActivity() {
    private lateinit var btnColor: Button
    private var selectedColor: Int = Color.RED
    private var selectedSize: String = ""
    private lateinit var civImage: CircleImageView
    private lateinit var pictureUri: Uri

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            pictureUri = it
            civImage.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_clothes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val sharedPreferences = getSharedPreferences("dataLogIn", MODE_PRIVATE)
        val iduser = sharedPreferences.getString("id", "")

        civImage = findViewById(R.id.civImage)
        val ibUpload = findViewById<ImageButton>(R.id.ibUpload)
        val edtName = findViewById<TextInputEditText>(R.id.edtName)
        val spinnerSize = findViewById<Spinner>(R.id.spinnerSize)
        val edtMaterial = findViewById<TextInputEditText>(R.id.edtMaterial)
        val edtBrand = findViewById<TextInputEditText>(R.id.edtBrand)
        val edtType = findViewById<TextInputEditText>(R.id.edtType)
        val cbSpring = findViewById<CheckBox>(R.id.cbSpring)
        val cbSummer = findViewById<CheckBox>(R.id.cbSummer)
        val cbFall = findViewById<CheckBox>(R.id.cbFall)
        val cbWinter = findViewById<CheckBox>(R.id.cbWinter)
        val btnConfirm = findViewById<MaterialButton>(R.id.btnConfirm)
        var username = ""


        btnColor = findViewById(R.id.btnColor)
        btnColor.backgroundTintList = ColorStateList.valueOf(selectedColor)

        val sizes = arrayOf("S", "M", "L", "XL", "XXL");
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, sizes
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSize.adapter = adapter

        lifecycleScope.launch {
            try {
                val user = RetrofitInstance.api.getUserBy("iduser", iduser!!)
                username = user.username
            } catch (_: Exception) { }
        }

        spinnerSize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedSize = sizes[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        btnColor.setOnClickListener {
            showColorPickerDialog()
        }

        ibUpload.setOnClickListener {
            pickImage.launch("image/*")
        }

        btnConfirm.setOnClickListener {
            val name = edtName.text.toString()
            val type = edtType.text.toString()

            if (name.isEmpty()) {
                edtName.error = "Vui lòng nhập tên trang phục"
                return@setOnClickListener
            }

            if (type.isEmpty()) {
                edtType.error = "Vui lòng nhập loại trang phục"
                return@setOnClickListener
            }

            if (!cbSpring.isChecked && !cbSummer.isChecked && !cbFall.isChecked && !cbWinter.isChecked) {
                Toast.makeText(this, "Vui lòng chọn ít nhất một mùa", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val material = edtMaterial.text.toString().ifEmpty { "cotton" }
            val brand = edtBrand.text.toString().ifEmpty { "no name" }
            var weather = ""

            if (cbSpring.isChecked && cbSummer.isChecked && cbFall.isChecked && cbWinter.isChecked) {
                weather = "all"
            } else {
                if (cbSpring.isChecked) {
                    weather += "spring "
                }
                if (cbSummer.isChecked) {
                    weather += "summer "
                }
                if (cbFall.isChecked) {
                    weather += "fall "
                }
                if (cbWinter.isChecked) {
                    weather += "winter "
                }
            }

            lifecycleScope.launch {
                btnConfirm.isEnabled = false
                try {
                    val clothes = ClothesCreate(name, null, selectedSize, Integer.toHexString(selectedColor).uppercase(), material, brand, weather, type, username)
                    val response = RetrofitInstance.api.createClothes(clothes)
                    val id = response.idcloth
                    val data = uploadGalleryImage(pictureUri, id)
                    RetrofitInstance.api.createUserClothes(UserClothesCreate(iduser!!, id))

                    Toast.makeText(this@AddClothesActivity, "Thêm trang phục thành công", Toast.LENGTH_SHORT).show()
                    val intent = intent
                    intent.putExtra("data", data)
                    setResult(RESULT_OK, intent)
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@AddClothesActivity, e.message, Toast.LENGTH_SHORT).show()
                }
                btnConfirm.isEnabled = true
            }

        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val config = Configuration(newBase?.resources?.configuration)
        config.fontScale = 1.0f
        applyOverrideConfiguration(config)
        super.attachBaseContext(newBase)
    }

    private suspend fun uploadGalleryImage(uri: Uri, id: String): ClothesResponse? {
        val inputStream = contentResolver.openInputStream(uri) ?: return null
        val tempFile = withContext(Dispatchers.IO) {
            File.createTempFile("upload_", ".jpg", cacheDir)
        }
        tempFile.outputStream().use { output ->
            inputStream.copyTo(output)
        }

        val requestFile = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", tempFile.name, requestFile)

        return try {
            val clothes = RetrofitInstance.api.updatePicture(id, body)
            tempFile.delete()
            clothes
        } catch (e: Exception) {
            Log.d("Fix", e.message.toString())
            null
        }
    }

    private fun showColorPickerDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_color_picker, null)
        val colorPickerView = dialogView.findViewById<ColorPickerView>(R.id.colorPickerView)
        val alphaSlideBar = dialogView.findViewById<AlphaSlideBar>(R.id.alphaSlideBar)
        val brightnessSlideBar = dialogView.findViewById<BrightnessSlideBar>(R.id.brightnessSlide)

        colorPickerView.setInitialColor(selectedColor)
        colorPickerView.attachAlphaSlider(alphaSlideBar)
        colorPickerView.attachBrightnessSlider(brightnessSlideBar)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Chọn màu")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                selectedColor = colorPickerView.color
                btnColor.backgroundTintList = ColorStateList.valueOf(selectedColor)
            }
            .setNegativeButton("Hủy", null)
            .create()

        dialog.show()
    }
}