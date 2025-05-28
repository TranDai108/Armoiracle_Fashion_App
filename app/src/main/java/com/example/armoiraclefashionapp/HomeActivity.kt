package com.example.armoiraclefashionapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_home)

        // Thiết lập BottomNavigationView
        bottomNavigation = findViewById(R.id.bottomNavigation)
        bottomNavigation.itemIconTintList = null

        // Hiển thị HomeFragment mặc định
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
            bottomNavigation.selectedItemId = R.id.nav_home
        }

        // Xử lý BottomNavigationView
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_box -> {
                    val intent = Intent(this, QuizStartActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .commit()
                    true
                }
                R.id.nav_stack -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, WardrobeFragment())
                        .addToBackStack(null)
                        .commit()
                    true
                }
                R.id.nav_add -> {
                    true
                }
                R.id.nav_clothes -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, WardrobeFragment())
                        .addToBackStack(null)
                        .commit()
                    true
                }
                else -> false
            }
        }

        // Edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.homepage)) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom)
            insets
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("SELECTED_ITEM", bottomNavigation.selectedItemId)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val selectedItemId = savedInstanceState.getInt("SELECTED_ITEM", R.id.nav_home)
        bottomNavigation.selectedItemId = selectedItemId
        when (selectedItemId) {
            R.id.nav_home -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
            R.id.nav_stack, R.id.nav_clothes -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, WardrobeFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}