package com.example.armoiraclefashionapp

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.armoiraclefashionapp.model.WardrobeItem

class WardrobeGridAdapter(private val items: MutableList<WardrobeItem>) :
    RecyclerView.Adapter<WardrobeGridAdapter.WardrobeViewHolder>() {

    class WardrobeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imgItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WardrobeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_wardrobe, parent, false)
        return WardrobeViewHolder(view)
    }

    override fun onBindViewHolder(holder: WardrobeViewHolder, position: Int) {
        when (val item = items[position]) {
            is WardrobeItem.FromUri -> {
                try {
                    holder.imageView.setImageURI(item.imageUri)
                    if (holder.imageView.drawable == null) {
                        holder.imageView.setImageResource(R.drawable.shirt)
                    }
                } catch (e: Exception) {
                    holder.imageView.setImageResource(R.drawable.shirt)
                }
            }

            is WardrobeItem.FromRes -> {
                holder.imageView.setImageResource(item.imageResId)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun addItem(item: WardrobeItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }
}
