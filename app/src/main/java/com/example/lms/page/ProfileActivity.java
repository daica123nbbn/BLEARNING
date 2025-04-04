package com.example.lms.page;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lms.activity.BaseActivity;
import com.example.lms.MainActivity;
import com.example.lms.R;

import java.io.File;

public class ProfileActivity extends BaseActivity {
    private ImageView profileAvatar;
    private TextView profileName;
    private TextView profileEmail;
    private Button btnBackToMain;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // Ánh xạ các thành phần
        profileAvatar = findViewById(R.id.profile_avatar);
        profileName = findViewById(R.id.profile_name);
        profileEmail = findViewById(R.id.profile_email);
        btnBackToMain = findViewById(R.id.btn_back_to_main);
        btnLogout = findViewById(R.id.btn_logout);

        // Hiển thị thông tin người dùng từ SharedPreferences
        if (isLoggedIn) {
            profileName.setText(fullName != null ? fullName : "Unknown");
            profileEmail.setText(email != null ? email : "Unknown");

            // Hiển thị ảnh đại diện nếu có
            String imagePath = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    .getString("profileImagePath", null);
            if (imagePath != null) {
                profileAvatar.setImageURI(Uri.fromFile(new File(imagePath)));
            }
        } else {
            // Dữ liệu mặc định nếu chưa đăng nhập
            profileName.setText("Lion Fischer");
            profileEmail.setText("lionfischer@gmail.com");
        }

        // Xử lý sự kiện nhấn nút Back to Main Page
        btnBackToMain.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Xử lý sự kiện nhấn nút Logout
        btnLogout.setOnClickListener(v -> {
            // Gọi phương thức logoutUser() từ BaseActivity để xóa dữ liệu và cập nhật giao diện
            logoutUser();
        });

        // Xử lý sự kiện nhấn vào Personal Information
        findViewById(R.id.personal_info_layout).setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, PersonalInfoActivity.class);
            startActivity(intent);
        });

        // Xử lý sự kiện nhấn vào My Courses
        findViewById(R.id.my_courses_layout).setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, MyCourseActivity.class);
            startActivity(intent);
        });
    }
}