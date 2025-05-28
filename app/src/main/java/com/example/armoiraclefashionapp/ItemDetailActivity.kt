package com.example.armoiraclefashionapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.armoiraclefashionapp.model.WardrobeItem
import java.util.Date

class ItemDetailActivity : AppCompatActivity() {
    private lateinit var item: WardrobeItem
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                findViewById<ImageView>(R.id.ivItemPicture).setImageURI(uri)
                // Lưu tạm uri để sử dụng khi Save
               /* item = when (item) {
                    is WardrobeItem.FromRes -> WardrobeItem.FromUri(
                        title = item.title,
                        imageUri = uri,
                        label = item.label,
                        color = item.color,
                        size = item.size,
                        material = item.material,
                        brand = item.brand,
                        price = item.price,
                        purchaseDate = item.purchaseDate,
                        condition = item.condition,
                        description = item.description,
                        tags = item.tags
                    )
                    is WardrobeItem.FromUri -> item.copy(imageUri = uri)
                }*/
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_item_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}