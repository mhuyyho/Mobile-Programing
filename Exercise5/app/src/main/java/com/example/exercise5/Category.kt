package com.example.exercise5

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    // hỗ trợ cả key "images" và "image"
    @SerializedName(value = "images", alternate = ["image"])
    val images: String? = null,

    @SerializedName("description")
    val description: String? = null
)