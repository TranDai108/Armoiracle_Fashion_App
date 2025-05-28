package com.example.armoiraclefashionapp.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.armoiraclefashionapp.R
import com.example.armoiraclefashionapp.model.WardrobeItem

class WardrobeHorizontalAdapter(private val items: List<WardrobeItem>) :
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
        var label = ""
        // Gán hình ảnh dựa theo kiểu FromRes hay FromUri
        when (item) {
            is WardrobeItem.FromRes -> {
                holder.imgItem.setImageResource(item.imageResId)
                holder.tvTitle.text = item.title
                holder.tvLabel.text = item.label
                label = item.label
            }
            is WardrobeItem.FromUri -> {
                holder.imgItem.setImageURI(item.imageUri)
                holder.tvTitle.text = item.title
                holder.tvLabel.text = item.label
                label = item.label
            }
        }

        // Tùy chỉnh background cho nhãn
        val bg = when (label) {
            "Cap" -> R.drawable.bg_label
            "Pant" -> R.drawable.bg_label
            "Hoodie" -> R.drawable.bg_label
            else -> R.drawable.bg_label
        }
        holder.tvLabel.setBackgroundResource(bg)
    }

    override fun getItemCount() = items.size
}
