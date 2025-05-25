package com.example.armoiraclefashionapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.armoiraclefashionapp.adapter.WardrobeAdapter
import com.example.armoiraclefashionapp.model.WardrobeItem


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val rvCategories = findViewById<RecyclerView>(R.id.rvCategories)
        rvCategories.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val categories = listOf(
            Category("Vacation", R.drawable.bg_category_beige,
                listOf(R.drawable.cap, R.drawable.shorts, R.drawable.shirt),
                count = 16),
            Category("Hangout",  R.drawable.bg_category_beige,
                listOf(R.drawable.hoodie, R.drawable.cap, R.drawable.sneaker),
                count = 8),
            Category("Sleepover",  R.drawable.bg_category_beige,
                listOf(R.drawable.hoodie, R.drawable.shirt, R.drawable.sneaker),
                count = 3),
            Category("Weather",  R.drawable.bg_category_beige,
                listOf(R.drawable.hoodie, R.drawable.shirt, R.drawable.sneaker),
                count = 0),
            Category("Season",  R.drawable.bg_category_beige,
                listOf(R.drawable.shirt, R.drawable.shorts, R.drawable.sneaker),
                count = 1),
        )
        rvCategories.adapter = CategoryAdapter(categories)


        // === WARDROBE ===
        val rvWardrobe = findViewById<RecyclerView>(R.id.rvWardrobe)
        rvWardrobe.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // CHỈ ĐỔ label LÀ STRING
        val wardrobeItems = listOf(
            WardrobeItem("Flower printed boxy fit shirt", R.drawable.hoodie, "Hoodie"),
            WardrobeItem("Favorite Daughter Cap",        R.drawable.cap,  "Cap"),
            // thêm các món khác…
        )
        rvWardrobe.adapter = WardrobeAdapter(wardrobeItems)

        // edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.homepage)) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom)
            insets
        }
    }
}
