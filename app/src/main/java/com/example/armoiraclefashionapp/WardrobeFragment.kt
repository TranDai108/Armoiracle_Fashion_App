package com.example.armoiraclefashionapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.armoiraclefashionapp.models.WardrobeItem
import com.google.android.material.button.MaterialButton

class WardrobeFragment : Fragment() {

    private var wardrobeAdapter: WardrobeGridAdapter? = null
    private val wardrobeItems = mutableListOf<WardrobeItem>()

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                wardrobeAdapter?.addItem(WardrobeItem.FromUri(title = "New Item", imageUri = uri))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wardrobe, container, false)

        val rvWardrobeItems = view.findViewById<RecyclerView>(R.id.rvWardrobeItems)
        rvWardrobeItems.layoutManager = GridLayoutManager(requireContext(), 3).apply {
            // Thêm khoảng cách giữa các cột
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

        wardrobeItems.addAll(listOf(
            WardrobeItem.FromRes(title = "Hoodie", imageResId = R.drawable.hoodie, label = "Hoodie"),
            WardrobeItem.FromRes(title = "Cap", imageResId = R.drawable.cap, label = "Cap"),
            WardrobeItem.FromRes(title = "Shirt", imageResId = R.drawable.shirt, label = "Shirt")
        ))
        wardrobeAdapter = WardrobeGridAdapter(wardrobeItems)
        rvWardrobeItems.adapter = wardrobeAdapter

        view.findViewById<Button>(R.id.btnAddItem).setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            pickImageLauncher.launch(intent)
        }

        return view
    }
}