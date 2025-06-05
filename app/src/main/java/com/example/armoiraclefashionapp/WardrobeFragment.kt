package com.example.armoiraclefashionapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.armoiraclefashionapp.api.RetrofitInstance
import com.example.armoiraclefashionapp.models.ClothesResponse
import com.example.armoiraclefashionapp.models.WardrobeItem
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class WardrobeFragment : Fragment() {

    private var wardrobeAdapter: WardrobeGridAdapter? = null
    private var wardrobeItems = mutableListOf<ClothesResponse>()

    //    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            result.data?.data?.let { uri ->
//                wardrobeAdapter?.addItem(WardrobeItem.FromUri(title = "New Item", imageUri = uri))
//            }
//        }
//    }
    private lateinit var addClothesLauncher: ActivityResultLauncher<Intent>

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wardrobe, container, false)

        val rvWardrobeItems = view.findViewById<RecyclerView>(R.id.rvWardrobeItems)
        rvWardrobeItems.layoutManager = GridLayoutManager(requireContext(), 3).apply {
            rvWardrobeItems.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: android.graphics.Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.left = 8
                    outRect.right = 8
                    outRect.top = 8
                    outRect.bottom = 8
                }
            })
        }

        val sharedPreferences = requireContext().getSharedPreferences("dataLogIn", MODE_PRIVATE)
        val iduser = sharedPreferences.getString("id", "")

        lifecycleScope.launch {
            try {
                wardrobeItems = RetrofitInstance.api.getAllClothesByUser(iduser!!).toMutableList()
                wardrobeAdapter = WardrobeGridAdapter(wardrobeItems)
                rvWardrobeItems.adapter = wardrobeAdapter
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            }
        }

        addClothesLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val newItem = data?.getParcelableExtra("data", ClothesResponse::class.java)
                if (newItem != null) {
                    wardrobeAdapter?.addItem(newItem)
                }
            }
        }

        view.findViewById<Button>(R.id.btnAddItem).setOnClickListener {
            val intent = Intent(requireContext(), AddClothesActivity::class.java)
            addClothesLauncher.launch(intent)
        }

        rvWardrobeItems.setOnTouchListener { _, _ ->
            if (wardrobeAdapter?.isInDeleteMode() == true && wardrobeAdapter?.canExitDeleteMode() == true) {
                wardrobeAdapter?.exitDeleteMode()
            }
            false
        }


        return view
    }
}