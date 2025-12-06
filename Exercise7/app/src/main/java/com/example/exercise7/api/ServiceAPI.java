package com.example.exercise7.api;

import com.example.exercise7.Const;
import com.example.exercise7.Model.ImageUpload;
import com.example.exercise7.Model.UpdateImagesResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ServiceAPI {

    Gson gson = new GsonBuilder()
            .setLenient()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();


    ServiceAPI serviceapi = new Retrofit.Builder()
            .baseUrl(Const.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ServiceAPI.class);

    @Multipart
    @POST("updateimages.php")
    Call<UpdateImagesResponse> upload(
            @Part("id") RequestBody id,
            @Part("images") MultipartBody.Part images
    );

}
