package com.example.firstexercise;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Window window = getWindow();
        WindowInsetsController controller = window.getInsetsController();
        controller.hide(WindowInsets.Type.statusBars());

        ImageView img = findViewById(R.id.avatar_image);

        Glide.with(this)
                .load(R.drawable.avt) // ảnh cố định
                .circleCrop()                // bo tròn
                .into(img);

        EditText array_editText = findViewById(R.id.array_number_editText);
        EditText toast_editText = findViewById(R.id.toast_editText);

        Button print_button = findViewById(R.id.print_array_number_button);
        Button toast_button = findViewById(R.id.toast_button);

        TextView toast_textview = findViewById(R.id.toast_textview);

        ArrayList<Integer> array = new ArrayList<>();

        print_button.setOnClickListener(v -> {

            String input = array_editText.getText().toString().trim();

            String[] parts = input.split("\\s+");

            ArrayList<Integer> even_number = new ArrayList<>();
            ArrayList<Integer> odd_number = new ArrayList<>();

            for (var p : parts)
            {
                int num = Integer.parseInt(p);
                if (num % 2 == 0) even_number.add(num);
                else odd_number.add(num);
            }

            Log.d("KQ", "Even number array: " + even_number);
            Log.d("KQ", "Even number array: " + odd_number);

        });

        toast_button.setOnClickListener(v -> {
            String input = toast_editText.getText().toString().trim();

            String[] parts = input.split("\\s+");

            StringBuilder reversed = new StringBuilder();

            for (int i = parts.length - 1; i >= 0; i--) {
                reversed.append(parts[i]).append(" ");
            }

            String result = reversed.toString().trim().toUpperCase();

            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
            toast_textview.setText(result);
        });


    }
}