package com.example.armoiraclefashionapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(private val items: List<Category>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img1: ImageView = view.findViewById(R.id.img1)
        val img2: ImageView = view.findViewById(R.id.img2)
        val img3: ImageView = view.findViewById(R.id.img3)
        val tvCount: TextView = view.findViewById(R.id.tvCount)
        val txtTitle: TextView = view.findViewById(R.id.txtTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CategoryViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false))

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = items[position]
        // ảnh
        if (item.imageResIds.isNotEmpty()) holder.img1.setImageResource(item.imageResIds[0])
        if (item.imageResIds.size > 1) holder.img2.setImageResource(item.imageResIds[1])
        if (item.imageResIds.size > 2) holder.img3.setImageResource(item.imageResIds[2])
        // title & count
        holder.txtTitle.text = item.title
        holder.tvCount.text = item.count.toString()
    }

    override fun getItemCount() = items.size
}
