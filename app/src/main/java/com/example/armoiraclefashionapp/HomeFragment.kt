package com.example.armoiraclefashionapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.armoiraclefashionapp.adapter.FollowAdapter
import com.example.armoiraclefashionapp.adapter.WardrobeHorizontalAdapter
import com.example.armoiraclefashionapp.model.WardrobeItem
import com.example.armoiraclefashionapp.models.FollowItem

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // === CATEGORIES ===
        val rvCategories = view.findViewById<RecyclerView>(R.id.rvCategories)
        rvCategories.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val categories = listOf(
            Category("Vacation", R.drawable.bg_category_beige, listOf(R.drawable.cap, R.drawable.shorts, R.drawable.shirt), count = 16),
            Category("Hangout", R.drawable.bg_category_beige, listOf(R.drawable.hoodie, R.drawable.cap, R.drawable.sneaker), count = 8),
            Category("Sleepover", R.drawable.bg_category_beige, listOf(R.drawable.hoodie, R.drawable.shirt, R.drawable.sneaker), count = 3),
            Category("Weather", R.drawable.bg_category_beige, listOf(R.drawable.hoodie, R.drawable.shirt, R.drawable.sneaker), count = 0),
            Category("Season", R.drawable.bg_category_beige, listOf(R.drawable.shirt, R.drawable.shorts, R.drawable.sneaker), count = 1),
        )
        rvCategories.adapter = CategoryAdapter(categories)

        // === WARDROBE ===
        val rvWardrobe = view.findViewById<RecyclerView>(R.id.rvWardrobe)
        rvWardrobe.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val wardrobeItems = listOf(
            WardrobeItem.FromRes("Flower printed boxy fit shirt", R.drawable.hoodie, "Hoodie"),
            WardrobeItem.FromRes("Favorite Daughter Cap", R.drawable.cap, "Cap"),
        )
        rvWardrobe.adapter = WardrobeHorizontalAdapter(wardrobeItems)

        // === RECOMMENDATION (rvFollow) ===
        val rvFollow = view.findViewById<RecyclerView>(R.id.rvFollow)
        rvFollow.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val followItems = listOf(
            FollowItem("Fashionista1", R.drawable.hoodie, "Casual style for daily wear"),
            FollowItem("TrendyUser", R.drawable.cap, "Sporty look for weekends"),
            FollowItem("StyleGuru", R.drawable.shirt, "Elegant outfit for events")
        )
        rvFollow.adapter = FollowAdapter(followItems)

        return view
    }
}
