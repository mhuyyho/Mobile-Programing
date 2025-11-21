package com.example.exercise5


object RetrofitClient {
    private const val BASE_URL = "http://app.iotstar.vn:8081/appfoods/"

    val apiService: ApiService by lazy {
        BaseClient.createService(BASE_URL, ApiService::class.java)
    }
}