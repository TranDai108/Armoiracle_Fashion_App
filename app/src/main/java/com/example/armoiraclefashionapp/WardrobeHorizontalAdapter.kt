package com.example.armoiraclefashionapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.armoiraclefashionapp.models.ClothesResponse

class WardrobeHorizontalAdapter(private val items: List<ClothesResponse>) :
    RecyclerView.Adapter<WardrobeHorizontalAdapter.WardrobeViewHolder>() {

    inner class WardrobeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgItem: ImageView = itemView.findViewById(R.id.imgItem)
        val tvLabel: TextView = itemView.findViewById(R.id.tvLabel)
        val tvTitle: TextView = itemView.findViewById(R.id.tvWardrobeItemTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WardrobeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_wardrobe, parent, false)
        return WardrobeViewHolder(view)
    }

    override fun onBindViewHolder(holder: WardrobeViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.itemView.context)
            .load(item.picture)
            .into(holder.imgItem)
        holder.tvTitle.text = item.name
        holder.tvLabel.text = item.type

        // Tùy chỉnh background cho nhãn
//        val bg = when (label) {
//            "Cap" -> R.drawable.bg_label
//            "Pant" -> R.drawable.bg_label
//            "Hoodie" -> R.drawable.bg_label
//            else -> R.drawable.bg_label
//        }
        holder.tvLabel.setBackgroundResource(R.drawable.bg_label)
    }

    override fun getItemCount() = items.size
}
