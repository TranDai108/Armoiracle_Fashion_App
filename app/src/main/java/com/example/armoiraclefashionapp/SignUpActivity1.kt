package com.example.armoiraclefashionapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.armoiraclefashionapp.api.RetrofitInstance
import com.example.armoiraclefashionapp.models.UserCreate
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.content.edit
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import androidx.core.graphics.createBitmap
import androidx.core.widget.addTextChangedListener
import java.io.FileOutputStream

class SignUpActivity1 : AppCompatActivity() {
    private lateinit var cameraUri: Uri
    private lateinit var ivAvatar: CircleImageView
    private lateinit var imageFile: File
    private var method = 0

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            cameraUri = it
            ivAvatar.setImageURI(it)
        }
    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            ivAvatar.setImageURI(cameraUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var isOkEmail = false
        var isOkPhone = false

        val bundle = intent.extras
        val name = bundle?.getString("name")
        val username = bundle?.getString("username")
        val password = bundle?.getString("password")
        val gender = bundle?.getInt("gender")

        val edtEmail = findViewById<TextInputEditText>(R.id.edtEmail)
        val edtPhone = findViewById<TextInputEditText>(R.id.edtPhone)
        val edtBirthday = findViewById<TextInputEditText>(R.id.edtBirthday)
        ivAvatar = findViewById(R.id.ivAvatar)
        val ibEdit = findViewById<ImageButton>(R.id.ibEdit)
        val btnComplete = findViewById<MaterialButton>(R.id.btnComplete)

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val osdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var formattedDate = sdf.format(Date(System.currentTimeMillis()))
        var birthday = osdf.format(Date(System.currentTimeMillis()))
        edtBirthday.setText(formattedDate)

        edtEmail.setOnFocusChangeListener { _, hasFocus ->
            val email = edtEmail.text.toString()
            isOkEmail = true
            if (!hasFocus) {
                if (email.isEmpty()) {
                    edtEmail.error = "Vui lòng nhập email"
                    isOkEmail = false
                    return@setOnFocusChangeListener
                }

                if (!email.matches(Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))) {
                    edtEmail.error = "Email không hợp lệ"
                    isOkEmail = false
                    return@setOnFocusChangeListener
                }

                lifecycleScope.launch {
                    try {
                        RetrofitInstance.api.getUserBy("email", email)
                        isOkEmail = false
                        edtEmail.error = "Email đã được đăng ký"
                    } catch (_: Exception) { }
                }
            }
        }

        edtPhone.addTextChangedListener {
            val phone = edtPhone.text.toString()

            if (phone.isEmpty()) {
                edtPhone.error = "Vui lòng nhập số điện thoại"
                isOkPhone = false
                return@addTextChangedListener
            }

            if (!phone.matches(Regex("0[0-9]{9}"))) {
                edtPhone.error = "Số điện thoại không hợp lệ"
                isOkPhone = false
                return@addTextChangedListener
            }

            if (phone.length == 10) {
                lifecycleScope.launch {
                    try {
                        RetrofitInstance.api.getUserBy("phone", phone)
                        isOkPhone = false
                        edtPhone.error = "Số điện thoại đã được đăng ký"
                    } catch (_: Exception) {
                        isOkPhone = true
                    }
                }
            }
        }

        edtBirthday.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Chọn ngày sinh")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(
                    CalendarConstraints.Builder()
                        .setEnd(MaterialDatePicker.todayInUtcMilliseconds())
                        .build()
                )
                .build()

            datePicker.show(supportFragmentManager, "DATE_PICKER")

            datePicker.addOnPositiveButtonClickListener { selection ->
                formattedDate = sdf.format(Date(selection))
                edtBirthday.setText(formattedDate)
                birthday = osdf.format(Date(selection))
            }
        }

        ibEdit.setOnClickListener {
            showImagePicker()
        }

        btnComplete.setOnClickListener {
            btnComplete.isEnabled = false
            Log.d("Fix", "$isOkEmail $isOkPhone")
            if (isOkEmail && isOkPhone) {
                lifecycleScope.launch {
                    val user = UserCreate(name, username, password, edtEmail.text.toString(), edtPhone.text.toString(), gender, null, birthday, null, null)
                    try {
                        val response = RetrofitInstance.api.createUser(user)
                        val sharedPreferences = getSharedPreferences("dataLogIn", MODE_PRIVATE)
                        sharedPreferences.edit {
                            putString("id", response.iduser)
                            putBoolean("remember", true)
                        }

                        when (method) {
                            1 -> {
                                uploadCameraImage(imageFile)
                            }
                            2 -> {
                                uploadGalleryImage(cameraUri)
                            }
                            else -> {
                                val bitmap = getBitmapFromVectorDrawable(this@SignUpActivity1, R.drawable.ic_avatar_placeholder)
                                val file = saveBitMapToFile(this@SignUpActivity1, bitmap)
                                uploadCameraImage(file)
                            }
                        }
                        val intent = Intent(this@SignUpActivity1, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } catch (_: Exception) { }
                }
            }
            btnComplete.isEnabled = true
        }

    }

    override fun attachBaseContext(newBase: Context?) {
        val config = Configuration(newBase?.resources?.configuration)
        config.fontScale = 1.0f
        applyOverrideConfiguration(config)
        super.attachBaseContext(newBase)
    }

    private fun showImagePicker() {
        val options = arrayOf("Chụp ảnh", "Chọn ảnh từ thư viện")
        AlertDialog.Builder(this)
            .setTitle("Chọn ảnh")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        method = 1
                        if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            openCamera()
                        } else {
                            requestCameraPermission.launch(android.Manifest.permission.CAMERA)
                        }
                    }
                    1 -> {
                        method = 2
                        pickImage.launch("image/*")
                    }
                }
            }.show()
    }

    private fun openCamera() {
        imageFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "image_${System.currentTimeMillis()}.jpg")
        cameraUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", imageFile)
        takePicture.launch(cameraUri)
    }

    private val requestCameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {isGranted ->
        if (isGranted) {
            openCamera()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Yêu cầu quyền camera")
                .setMessage("Ứng dụng cần quyền camera để chụp ảnh")
                .setPositiveButton("OK", null)
                .show()
        }
    }

    private fun uploadGalleryImage(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri) ?: return
        val tempFile = File.createTempFile("upload_", ".jpg", cacheDir)
        tempFile.outputStream().use { output ->
            inputStream.copyTo(output)
        }

        val requestFile = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", tempFile.name, requestFile)

        lifecycleScope.launch {
            try {
                val iduser = getSharedPreferences("dataLogIn", MODE_PRIVATE).getString("id", "")
                RetrofitInstance.api.updateAvatar(iduser!!, body)
                tempFile.delete()
            } catch (e: Exception) {
                Log.d("Fix", e.message.toString())
            }
        }
    }

    private fun uploadCameraImage(file: File) {
        val mediaType = "image/*".toMediaTypeOrNull()
        val requestFile = file.asRequestBody(mediaType)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        lifecycleScope.launch {
            try {
                val iduser = getSharedPreferences("dataLogIn", MODE_PRIVATE).getString("id", "")
                Log.d("Fix", iduser.toString())
                RetrofitInstance.api.updateAvatar(iduser!!, body)
                Log.d("Fix", "a")
            } catch (e: Exception) {
                Log.d("Fix", e.message.toString())
            }
        }
    }

    private fun getBitmapFromVectorDrawable(context: Context, @DrawableRes drawableId: Int): Bitmap {
        val drawable = AppCompatResources.getDrawable(context, drawableId)!!
        val bitmap = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun saveBitMapToFile(context: Context, bitmap: Bitmap): File {
        val file = File(context.cacheDir, "avatar_placeholder.jpg")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return file
    }
}