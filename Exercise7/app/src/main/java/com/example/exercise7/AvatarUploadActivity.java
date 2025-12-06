package com.example.exercise7;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.exercise7.Model.ImageUpload;
import com.example.exercise7.Model.UpdateImagesResponse;
import com.example.exercise7.Model.UserInfo;
import com.example.exercise7.api.ServiceAPI;
import com.example.exercise7.utils.RealPathUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvatarUploadActivity extends AppCompatActivity {

    private ImageView largeAvatar;
    private Button btnChooseFile;
    private Button btnUpload;

    private Uri mUri;
    private ProgressDialog mProgressDialog;
    private String userId;

    public static final int MY_REQUEST_CODE = 200;
    public static final String TAG = AvatarUploadActivity.class.getName();

    // ======== permission arrays y như MainActivity ========
    public static String[] storage_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storage_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    public static String[] permissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return storage_permissions_33;
        } else {
            return storage_permissions;
        }
    }

    // ======== ActivityResultLauncher chọn ảnh ========
    private ActivityResultLauncher<Intent> mActivityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            Log.e(TAG, "onActivityResult");
                            if (result.getResultCode() == RESULT_OK) {
                                Intent data = result.getData();
                                if (data == null) return;

                                Uri uri = data.getData();
                                mUri = uri;
                                try {
                                    Bitmap bitmap = MediaStore.Images.Media
                                            .getBitmap(getContentResolver(), uri);
                                    largeAvatar.setImageBitmap(bitmap);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_upload);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        largeAvatar = findViewById(R.id.large_avatar);
        btnChooseFile = findViewById(R.id.btn_choose_file);
        btnUpload = findViewById(R.id.btn_upload);

        Glide.with(this)
                .load(R.drawable.ic_person_placeholder)
                .into(largeAvatar);

        // nhận ID người dùng từ ProfileActivity
        userId = getIntent().getStringExtra("id");
        if (userId == null) userId = "3";   // fallback giống hình demo

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Đang upload...");

        btnChooseFile.setOnClickListener(v -> CheckPermission());

        btnUpload.setOnClickListener(v -> {
            if (mUri != null) {
                UploadAvatar();
            } else {
                Toast.makeText(this, "Bạn chưa chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ======== xin quyền & mở gallery ========
    private void CheckPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES)
                    == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                requestPermissions(permissions(), MY_REQUEST_CODE);
            }
        } else {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                requestPermissions(permissions(), MY_REQUEST_CODE);
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }
    }

    // ======== Upload avatar lên updateimages.php ========
    private void UploadAvatar() {
        mProgressDialog.show();

        // gửi id
        RequestBody requestId =
                RequestBody.create(MediaType.parse("multipart/form-data"), userId);

        // gửi file ảnh
        String IMAGE_PATH = RealPathUtil.getRealPath(this, mUri);
        File file = new File(IMAGE_PATH);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part partBodyImage =
                MultipartBody.Part.createFormData(Const.MY_IMAGES,
                        file.getName(), requestFile);

        ServiceAPI.serviceapi.upload(requestId, partBodyImage)
                .enqueue(new Callback<UpdateImagesResponse>() {
                    // trong onResponse của upload(...)
                    @Override
                    public void onResponse(Call<UpdateImagesResponse> call,
                                           Response<UpdateImagesResponse> response) {
                        mProgressDialog.dismiss();

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().isSuccess()
                                && response.body().getResult() != null
                                && !response.body().getResult().isEmpty()) {

                            UserInfo user = response.body().getResult().get(0);

                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("id", user.getId());
                            resultIntent.putExtra("username", user.getUsername());
                            resultIntent.putExtra("fname", user.getFname());
                            resultIntent.putExtra("email", user.getEmail());
                            resultIntent.putExtra("gender", user.getGender());
                            resultIntent.putExtra("avatar_url", user.getImages());

                            setResult(RESULT_OK, resultIntent);
                            finish();

                            Toast.makeText(AvatarUploadActivity.this, "Upload thành công", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(AvatarUploadActivity.this, "Upload thành công", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateImagesResponse> call, Throwable t) {
                        Toast.makeText(AvatarUploadActivity.this, "Upload thất bại: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                });

    }
}
