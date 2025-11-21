package com.example.exercise5

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("categories.php")
    fun getCategoriesRaw(): Call<JsonElement>

    // nếu API luôn trả mảng, bạn vẫn có thể giữ:
    // @GET("categories.php")
    // fun getCategories(): Call<List<Category>>
}