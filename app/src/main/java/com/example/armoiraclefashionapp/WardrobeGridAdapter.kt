package com.example.armoiraclefashionapp

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.armoiraclefashionapp.api.RetrofitInstance
import com.example.armoiraclefashionapp.models.ClothesResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WardrobeGridAdapter(private val items: MutableList<ClothesResponse>) :
    RecyclerView.Adapter<WardrobeGridAdapter.WardrobeViewHolder>() {

    private var isDeleteMode = false
    private var allowExitDeleteMode = true


    inner class WardrobeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imgItem)
        val titleTextView: TextView = itemView.findViewById(R.id.tvWardrobeItemTitle)
        val labelTextView: TextView = itemView.findViewById(R.id.tvLabel)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

        init {
            // Long click to enable delete mode
            itemView.setOnLongClickListener {
                isDeleteMode = true
                notifyDataSetChanged()
                true
            }

            // Click delete button to remove item
            btnDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val itemToDelete = items[position]

                    // Gọi API để xóa item trên server
                    deleteItemFromServer(itemToDelete.idcloth, position, itemView)
                }
            }
        }
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
            .into(holder.imageView)

        holder.titleTextView.text = item.name
        holder.labelTextView.text = item.type

        // Show delete button if delete mode is active
        holder.btnDelete.visibility = if (isDeleteMode) View.VISIBLE else View.GONE

        holder.itemView.alpha = 0f
        holder.itemView.animate().alpha(1f).setDuration(300).start()

        holder.itemView.setOnLongClickListener {
            enterDeleteMode()
            true
        }
    }

    override fun getItemCount(): Int = items.size

    fun addItem(item: ClothesResponse) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    // Xóa item khỏi server và sau đó xóa khỏi danh sách trong adapter
    private fun deleteItemFromServer(itemId: String, position: Int, itemView: View) {
        // Gọi API xóa item từ server
        CoroutineScope(Dispatchers.Main).launch {
            try {
                withContext(Dispatchers.IO) {
                    RetrofitInstance.api.deleteClothes(itemId)
                }
                items.removeAt(position)
                notifyItemRemoved(position)
                Toast.makeText(itemView.context, "Item deleted successfully", Toast.LENGTH_SHORT).show()

                if (items.isEmpty()) {
                    isDeleteMode = false
                    notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e("WardrobeGridAdapter", "Error deleting item: ${e.message}")
            }
        }
    }

    private fun enterDeleteMode() {
        isDeleteMode = true
        allowExitDeleteMode = false
        notifyDataSetChanged()

        Handler(Looper.getMainLooper()).postDelayed({
            allowExitDeleteMode = true
        }, 300)
    }

    fun exitDeleteMode() {
        isDeleteMode = false
        notifyDataSetChanged()
    }

    fun isInDeleteMode(): Boolean = isDeleteMode

    fun canExitDeleteMode(): Boolean = allowExitDeleteMode
}