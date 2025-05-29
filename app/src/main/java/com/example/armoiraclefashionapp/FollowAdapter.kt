package com.example.armoiraclefashionapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.armoiraclefashionapp.R
import com.example.armoiraclefashionapp.models.FollowItem

class FollowAdapter(private val followItems: List<FollowItem>) :
    RecyclerView.Adapter<FollowAdapter.FollowViewHolder>() {

    class FollowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivFollowImage)
        val usernameTextView: TextView = itemView.findViewById(R.id.tvRecommendation)
        val descriptionTextView: TextView = itemView.findViewById(R.id.tvDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_follow, parent, false)
        return FollowViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {
        val item = followItems[position]
        holder.imageView.setImageResource(item.imageResId)
        holder.usernameTextView.text = item.username
        holder.descriptionTextView.text = item.description
    }

    override fun getItemCount(): Int = followItems.size
}