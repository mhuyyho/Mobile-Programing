package com.example.exercise7;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // chuyển thẳng đến ProfileActivity
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);

        // có thể truyền dữ liệu user giả lập (giống trên XML profile)
        intent.putExtra("id", "3");
        intent.putExtra("username", "trung1");
        intent.putExtra("fullname", "Nguyễn Hữu Trung");
        intent.putExtra("email", "trung2@gmail.com");
        intent.putExtra("gender", "Male");
        startActivity(intent);

        finish(); // không quay lại Main
    }
}
