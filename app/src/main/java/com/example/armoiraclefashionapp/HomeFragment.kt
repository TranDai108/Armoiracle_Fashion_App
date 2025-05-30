package com.example.armoiraclefashionapp

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.armoiraclefashionapp.api.RetrofitInstance
import com.example.armoiraclefashionapp.models.ClothesResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeFragment : Fragment() {
    private var recommendClothes = mutableListOf<ClothesResponse>()
    private var wardrobes = mutableListOf<ClothesResponse>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val sharedPreferences = requireContext().getSharedPreferences("dataLogIn", MODE_PRIVATE)
        val iduser = sharedPreferences.getString("id", "")

        // === CATEGORIES ===
        val rvCategories = view.findViewById<RecyclerView>(R.id.rvCategories)
        rvCategories.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val categories = listOf(
            Category(
                "Vacation",
                R.drawable.bg_category_beige,
                listOf(R.drawable.cap, R.drawable.shorts, R.drawable.shirt),
                count = 16
            ),
            Category(
                "Hangout",
                R.drawable.bg_category_beige,
                listOf(R.drawable.hoodie, R.drawable.cap, R.drawable.sneaker),
                count = 8
            ),
            Category(
                "Sleepover",
                R.drawable.bg_category_beige,
                listOf(R.drawable.hoodie, R.drawable.shirt, R.drawable.sneaker),
                count = 3
            ),
            Category(
                "Weather",
                R.drawable.bg_category_beige,
                listOf(R.drawable.hoodie, R.drawable.shirt, R.drawable.sneaker),
                count = 0
            ),
            Category(
                "Season",
                R.drawable.bg_category_beige,
                listOf(R.drawable.shirt, R.drawable.shorts, R.drawable.sneaker),
                count = 1
            ),
        )
        rvCategories.adapter = CategoryAdapter(categories)

        // === WARDROBE ===
        val rvWardrobe = view.findViewById<RecyclerView>(R.id.rvWardrobe)
        rvWardrobe.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        lifecycleScope.launch {
            try {
                wardrobes = RetrofitInstance.api.getAllClothesByUser(iduser!!).toMutableList()
                rvWardrobe.adapter = WardrobeHorizontalAdapter(wardrobes)
                Log.d("wardrobes", wardrobes.toString())
            } catch (e: HttpException) {
                Log.e("HomeFragment", "Error fetching data: ${e.message.toString()}")
                when (e.code()) {
                    404 -> {
                        wardrobes = mutableListOf()
                    }
                    else -> {
                        throw e
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeFragment1", "Error fetching data: ${e.message.toString()}")
            }
        }

        // === RECOMMENDATION (rvFollow) ===
        val rvFollow = view.findViewById<RecyclerView>(R.id.rvFollow)
        rvFollow.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        lifecycleScope.launch {
            try {
                recommendClothes = RetrofitInstance.api.getRecommendClothesByUser(iduser!!).toMutableList()
                rvFollow.adapter = FollowAdapter(recommendClothes)
            } catch (e: HttpException) {
                when (e.code()) {
                    404 -> {
                        recommendClothes = mutableListOf()
                    }

                    else -> {
                        throw e
                    }
                }
            }
//        val followItems = listOf(
//            FollowItem("Fashionista1", R.drawable.hoodie, "Casual style for daily wear"),
//            FollowItem("TrendyUser", R.drawable.cap, "Sporty look for weekends"),
//            FollowItem("StyleGuru", R.drawable.shirt, "Elegant outfit for events")
//        )
        }
        return view
    }
}
