package com.example.armoiraclefashionapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.armoiraclefashionapp.models.ClothesResponse

class FollowAdapter(private val followItems: List<ClothesResponse>) :
    RecyclerView.Adapter<FollowAdapter.FollowViewHolder>() {

    class FollowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivFollowImage)
        val nameTextView: TextView = itemView.findViewById(R.id.tvRecommendation)
        val typeTextView: TextView = itemView.findViewById(R.id.tvTyle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_follow, parent, false)
        return FollowViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {
        val item = followItems[position]
        Glide.with(holder.itemView.context)
            .load(item.picture)
            .into(holder.imageView)
        holder.nameTextView.text = item.name
        holder.typeTextView.text = item.author
    }

    override fun getItemCount(): Int = followItems.size
}