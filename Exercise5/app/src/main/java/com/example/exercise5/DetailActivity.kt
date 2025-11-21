package com.example.exercise5

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val img: ImageView = findViewById(R.id.imgDetail)
        val title: TextView = findViewById(R.id.txtTitle)
        val desc: TextView = findViewById(R.id.txtDescription)

        val name = intent.getStringExtra("name") ?: "No name"
        val image = intent.getStringExtra("image") ?: ""
        val description = intent.getStringExtra("description") ?: ""

        title.text = name
        desc.text = description

        val imgUrl = if (image.startsWith("http", true)) image else "http://app.iotstar.vn:8081/appfoods/images/$image"
        Glide.with(this)
            .load(if (imgUrl.isNotEmpty()) imgUrl else null)
            .placeholder(android.R.drawable.progress_indeterminate_horizontal)
            .error(android.R.drawable.ic_menu_report_image)
            .centerCrop()
            .into(img)
    }
}