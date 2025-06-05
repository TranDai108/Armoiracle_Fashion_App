package com.example.armoiraclefashionapp.models

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.armoiraclefashionapp.R
import com.example.armoiraclefashionapp.WardrobePickerAdapter
import com.example.armoiraclefashionapp.api.RetrofitInstance
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

class CombineActivity : AppCompatActivity() {
    private lateinit var btnAdd: ImageButton
    private lateinit var screenLayout: ConstraintLayout
    private lateinit var wardrobeItems: List<ClothesResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_combine)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnAdd = findViewById(R.id.btnAdd)
        screenLayout = findViewById(R.id.screen)

        fetchWardrobeItems()

        btnAdd.setOnClickListener {
            showWardrobeDialog()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val config = Configuration(newBase?.resources?.configuration)
        config.fontScale = 1.0f
        applyOverrideConfiguration(config)
        super.attachBaseContext(newBase)
    }

    private fun fetchWardrobeItems() {
        val sharedPreferences = getSharedPreferences("dataLogIn", MODE_PRIVATE)
        val iduser = sharedPreferences.getString("id", "") ?: return

        lifecycleScope.launch {
            try {
                wardrobeItems = RetrofitInstance.api.getAllClothesByUser(iduser)
            } catch (e: Exception) {
                Toast.makeText(this@CombineActivity, "Error loading wardrobe", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showWardrobeDialog() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_wardrobe_picker, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvWardrobePicker)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = WardrobePickerAdapter(wardrobeItems) { selectedItem ->
            dialog.dismiss()
            addItemToScreen(selectedItem)
        }
        dialog.setContentView(view)
        dialog.show()
    }

    fun Int.toPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    private fun addItemToScreen(item: ClothesResponse) {
        val imageView = ImageView(this)

        val sizeInDp = 120
        val sizeInPx = (sizeInDp * resources.displayMetrics.density).toInt()
        val layoutParams = ConstraintLayout.LayoutParams(sizeInPx, sizeInPx)

        imageView.layoutParams = layoutParams
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        Glide.with(this).load(item.picture).into(imageView)

        screenLayout.addView(imageView)
        makeViewDraggableAndZoomable(imageView)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun makeViewDraggableAndZoomable(view: View) {
        var scaleFactor = 1.0f
        var dX = 0f
        var dY = 0f

        val scaleGestureDetector = ScaleGestureDetector(this, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                scaleFactor *= detector.scaleFactor
                scaleFactor = max(0.5f, min(scaleFactor, 3.0f))
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
                return true
            }
        })

        view.setOnTouchListener { v, event ->
            scaleGestureDetector.onTouchEvent(event)

            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    dX = v.x - event.rawX
                    dY = v.y - event.rawY
                }

                MotionEvent.ACTION_MOVE -> {
                    if (!scaleGestureDetector.isInProgress) {
                        v.animate()
                            .x(event.rawX + dX)
                            .y(event.rawY + dY)
                            .setDuration(0)
                            .start()
                    }
                }
            }

            true
        }
    }

}