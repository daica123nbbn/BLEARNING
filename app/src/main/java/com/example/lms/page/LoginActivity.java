package com.example.lms.page;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lms.MainActivity;
import com.example.lms.R;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView forgotPasswordText;
    private TextView registerText;

    // Tài khoản giả lập để test
    private static final String TEST_EMAIL = "giang@gmail.com";
    private static final String TEST_PASSWORD = "123456";
    private static final String TEST_USER_ID = "user1";
    private static final String TEST_FULL_NAME = "Bui Giang";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.btn_login);
        forgotPasswordText = findViewById(R.id.forgot_password);
        registerText = findViewById(R.id.register);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (validateInput(email, password)) {
                loginUser(email, password);
            }
        });

        forgotPasswordText.setOnClickListener(v -> {
            Toast.makeText(this, "Forgot Password clicked", Toast.LENGTH_SHORT).show();
            // Thêm logic để xử lý quên mật khẩu (ví dụ: mở activity ResetPasswordActivity)
        });

        registerText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private boolean validateInput(String email, String password) {
        // Kiểm tra Email
        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter a valid email address");
            emailEditText.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return false;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            passwordEditText.requestFocus();
            return false;
        }

        return true;
    }

    private void loginUser(String email, String password) {
        if (email.equals(TEST_EMAIL) && password.equals(TEST_PASSWORD)) {

            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("userId", TEST_USER_ID);
            editor.putBoolean("isLoggedIn", true);
            editor.putString("fullName", TEST_FULL_NAME);
            editor.putString("email", TEST_EMAIL);
            editor.apply();

            Toast.makeText(this, "Login successful! User ID: " + TEST_USER_ID, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_LONG).show();
        }
    }
}