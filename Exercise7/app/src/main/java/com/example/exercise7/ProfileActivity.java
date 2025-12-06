package com.example.exercise7;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

public class ProfileActivity extends AppCompatActivity {

    private ImageView avatar;
    private EditText tvId, tvUsername, tvFullname, tvEmail, tvGender;
    private Button btnLogout;

    // launcher nhận kết quả từ AvatarUploadActivity
    private final ActivityResultLauncher<Intent> uploadLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();

                            String id      = data.getStringExtra("id");
                            String username= data.getStringExtra("username");
                            String fname   = data.getStringExtra("fname");
                            String email   = data.getStringExtra("email");
                            String gender  = data.getStringExtra("gender");
                            String avatarUrl = data.getStringExtra("avatar_url");

                            if (id != null)       tvId.setText(id);
                            if (username != null) tvUsername.setText(username);
                            if (fname != null)    tvFullname.setText(fname);
                            if (email != null)    tvEmail.setText(email);
                            if (gender != null)   tvGender.setText(gender);

                            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                                Glide.with(ProfileActivity.this)
                                        .load(avatarUrl)
                                        .circleCrop()
                                        .into(avatar);
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        avatar      = findViewById(R.id.avatar);
        tvId        = findViewById(R.id.tv_id);
        tvUsername  = findViewById(R.id.tv_username);
        tvFullname  = findViewById(R.id.tv_fullname);
        tvEmail     = findViewById(R.id.tv_email);
        tvGender    = findViewById(R.id.tv_gender);
        btnLogout   = findViewById(R.id.btn_logout);

        // click avatar -> mở màn upload
        avatar.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, AvatarUploadActivity.class);
            // gửi ID hiện tại cho uploadimages.php
            intent.putExtra("id", tvId.getText().toString().trim());
            uploadLauncher.launch(intent);
        });
    }
}

