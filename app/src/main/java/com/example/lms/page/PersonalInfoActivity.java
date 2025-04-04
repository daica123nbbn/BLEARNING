package com.example.lms.page;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.lms.activity.BaseActivity;
import com.example.lms.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class PersonalInfoActivity extends BaseActivity {
    private ImageView profileImage;
    private EditText editName, editDob, editJob, editPhone, editEmail, editPassword;
    private Button btnUpdateImage, btnSave, btnBackToProfile;
    private Uri imageUri;
    private static final int STORAGE_PERMISSION_CODE = 100;
    private String localImagePath; // Đường dẫn tệp cục bộ của ảnh

    // ActivityResultLauncher để chọn ảnh từ thiết bị
    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    // Sao chép ảnh vào bộ nhớ nội bộ và lưu đường dẫn
                    localImagePath = copyImageToInternalStorage(imageUri);
                    if (localImagePath != null) {
                        profileImage.setImageURI(Uri.fromFile(new File(localImagePath)));
                    } else {
                        Toast.makeText(this, "Failed to copy image", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_information);

        // Ánh xạ các thành phần
        profileImage = findViewById(R.id.profile_image);
        editName = findViewById(R.id.edit_name);
        editDob = findViewById(R.id.edit_dob);
        editJob = findViewById(R.id.edit_job);
        editPhone = findViewById(R.id.edit_phone);
        editEmail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_password);
        btnUpdateImage = findViewById(R.id.btn_update_image);
        btnSave = findViewById(R.id.btn_save);
        btnBackToProfile = findViewById(R.id.btn_back_to_profile);

        // Hiển thị thông tin người dùng từ SharedPreferences
        if (isLoggedIn) {
            editName.setText(fullName != null ? fullName : "Unknown");
            editEmail.setText(email != null ? email : "Unknown");
        }

        // Lấy các thông tin khác từ SharedPreferences (nếu có)
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editDob.setText(prefs.getString("dob", "01/01/1990"));
        editJob.setText(prefs.getString("job", ""));
        editPhone.setText(prefs.getString("phone", "+1234567890"));
        editPassword.setText(prefs.getString("password", "********"));

        // Hiển thị ảnh đại diện nếu có
        localImagePath = prefs.getString("profileImagePath", null);
        if (localImagePath != null) {
            profileImage.setImageURI(Uri.fromFile(new File(localImagePath)));
        }

        // Xử lý sự kiện nhấn nút Update Image
        btnUpdateImage.setOnClickListener(v -> {
            if (!hasStoragePermission()) {
                requestStoragePermission();
            } else {
                openImagePicker();
            }
        });

        // Xử lý sự kiện nhấn nút Save
        btnSave.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String dob = editDob.getText().toString().trim();
            String job = editJob.getText().toString().trim();
            String phone = editPhone.getText().toString().trim();
            String emailField = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            // Kiểm tra dữ liệu đầu vào
            if (name.isEmpty() || dob.isEmpty() || job.isEmpty() || phone.isEmpty() || emailField.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lưu thông tin vào SharedPreferences
            SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
            editor.putString("fullName", name);
            editor.putString("dob", dob);
            editor.putString("job", job);
            editor.putString("phone", phone);
            editor.putString("email", emailField);
            editor.putString("password", password);
            if (localImagePath != null) {
                editor.putString("profileImagePath", localImagePath); // Lưu đường dẫn tệp cục bộ
            }
            editor.apply();

            Toast.makeText(this, "Information saved successfully", Toast.LENGTH_SHORT).show();

            // Quay lại trang Profile
            Intent intent = new Intent(PersonalInfoActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        });

        // Xử lý sự kiện nhấn nút Back to My Profile
        btnBackToProfile.setOnClickListener(v -> {
            Intent intent = new Intent(PersonalInfoActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Sao chép ảnh từ Uri vào bộ nhớ nội bộ và trả về đường dẫn tệp cục bộ
    private String copyImageToInternalStorage(Uri uri) {
        try {
            // Xóa ảnh cũ nếu có
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String oldImagePath = prefs.getString("profileImagePath", null);
            if (oldImagePath != null) {
                File oldFile = new File(oldImagePath);
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }

            // Tạo tên tệp duy nhất
            String fileName = "profile_image_" + System.currentTimeMillis() + ".jpg";
            File file = new File(getFilesDir(), fileName);

            // Sao chép dữ liệu từ Uri vào tệp
            InputStream inputStream = getContentResolver().openInputStream(uri);
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.close();

            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean hasStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                    STORAGE_PERMISSION_CODE);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "Permission denied to access storage", Toast.LENGTH_SHORT).show();
            }
        }
    }
}