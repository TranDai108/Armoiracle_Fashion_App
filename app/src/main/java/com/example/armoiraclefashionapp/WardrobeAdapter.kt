package com.example.armoiraclefashionapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.armoiraclefashionapp.R
import com.example.armoiraclefashionapp.model.WardrobeItem

class WardrobeAdapter(private val items: List<WardrobeItem>) :
    RecyclerView.Adapter<WardrobeAdapter.WardrobeViewHolder>() {

    inner class WardrobeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // 1 ảnh chính
        val imgItem: ImageView = itemView.findViewById(R.id.imgItem)
        // pill–label
        val tvLabel: TextView = itemView.findViewById(R.id.tvLabel)
        // tiêu đề bên dưới
        val tvTitle: TextView = itemView.findViewById(R.id.tvWardrobeItemTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WardrobeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_wardrobe, parent, false)
        return WardrobeViewHolder(view)
    }

    override fun onBindViewHolder(holder: WardrobeViewHolder, position: Int) {
        val item = items[position]

        // 1 ảnh duy nhất
        holder.imgItem.setImageResource(item.imageResId)
        // tiêu đề
        holder.tvTitle.text = item.title
        // label góc trên
        holder.tvLabel.text = item.label

        // (tuỳ chọn): đổi màu pill tuỳ label
        val bg = when (item.label) {
            "Cap"    -> R.drawable.bg_label
            "Pant"   -> R.drawable.bg_label
            "Hoodie" -> R.drawable.bg_label
            else     -> R.drawable.bg_label
        }
        holder.tvLabel.setBackgroundResource(bg)
    }

    override fun getItemCount() = items.size
}
