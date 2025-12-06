package com.example.exercise7.Model;

import java.util.List;

public class UpdateImagesResponse {
    private boolean success;
    private String message;
    private List<UserInfo> result;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<UserInfo> getResult() { return result; }
}

