package com.example.exercise7;

import com.google.gson.annotations.SerializedName;

public class ImageUpload {

    @SerializedName("id")
    private int id;

    @SerializedName("username")
    private String username;

    // server trả về "images": "http://.../upload/xxx.png"
    @SerializedName("images")
    private String avatar;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getAvatar() { return avatar; }

    public void setAvatar(String avatar) { this.avatar = avatar; }
}