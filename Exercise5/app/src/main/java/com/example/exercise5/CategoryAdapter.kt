package com.example.exercise5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CategoryAdapter(
    private val items: MutableList<Category>,
    private val onClick: (Category, Int) -> Unit,
    private val onLongClick: (Category, Int) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.imgCategory)
        val name: TextView = itemView.findViewById(R.id.txtName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.name

        // base nếu server trả tên file
        val base = "http://app.iotstar.vn:8081/appfoods/images/"
        val raw = item.images ?: ""
        val imgUrl = when {
            raw.isEmpty() -> null
            raw.startsWith("http", true) -> raw
            else -> base + raw
        }

        Glide.with(holder.itemView.context)
            .load(imgUrl)
            .placeholder(android.R.drawable.progress_indeterminate_horizontal)
            .error(android.R.drawable.ic_menu_report_image)
            .centerCrop()
            .into(holder.img)

        holder.itemView.setOnClickListener {
            onClick(item, position)
        }

        holder.itemView.setOnLongClickListener {
            onLongClick(item, position)
            true
        }
    }

    override fun getItemCount(): Int = items.size

    fun removeAt(position: Int) {
        if (position in 0 until items.size) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): Category? = items.getOrNull(position)
}