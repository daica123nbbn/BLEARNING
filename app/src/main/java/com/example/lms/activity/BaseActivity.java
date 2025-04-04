package com.example.lms.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lms.MainActivity;
import com.example.lms.R;
import com.example.lms.page.LoginActivity;
import com.example.lms.page.ProfileActivity;
import com.example.lms.page.RegisterActivity;

import java.io.File;

public abstract class BaseActivity extends AppCompatActivity {

    private ImageButton profileButton;
    private TextView nameLoginTextView;
    private EditText searchEditText;
    private Button searchButton;
    private ImageView logo;
    protected boolean isLoggedIn = false;
    protected String fullName = "Guest";
    protected String userId;
    protected String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadUserData();
    }

    private void loadUserData() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        userId = prefs.getString("userId", null);
        fullName = prefs.getString("fullName", "Guest");
        email = prefs.getString("email", null);
    }

    protected void setupNavigationBar() {
        profileButton = findViewById(R.id.profileButton);
        nameLoginTextView = findViewById(R.id.namelogin);
        logo = findViewById(R.id.logoImageView);

        updateNavigationBar();

        if (profileButton != null) {
            profileButton.setOnClickListener(v -> showProfileMenu());
        }
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(BaseActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    private void updateNavigationBar() {
        if (nameLoginTextView != null) {
            nameLoginTextView.setText(fullName);
        }

        if (profileButton != null) {
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String imagePath = prefs.getString("profileImagePath", null);
            if (imagePath != null) {
                profileButton.setImageURI(Uri.fromFile(new File(imagePath)));
            } else {
                profileButton.setImageResource(R.drawable.user_icon);
            }
        }
    }

    protected void setupSearchBar() {
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);

        if (searchButton != null && searchEditText != null) {
            searchButton.setOnClickListener(v -> {
                String query = searchEditText.getText().toString().trim();
                if (query.isEmpty()) {
                    Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show();
                } else {
                    performSearch(query);
                }
            });
        }
    }

    protected void performSearch(String query) {
        Toast.makeText(this, "Searching for: " + query, Toast.LENGTH_SHORT).show();
    }

    private void showProfileMenu() {
        PopupMenu popupMenu = new PopupMenu(this, profileButton);

        if (isLoggedIn) {
            popupMenu.getMenu().add(0, 1, 0, "My Profile");
            popupMenu.getMenu().add(0, 2, 0, "Logout");
        } else {
            popupMenu.getMenu().add(0, 3, 0, "Login");
            popupMenu.getMenu().add(0, 4, 0, "Register");
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case 1: // My Profile
                    Intent profileIntent = new Intent(this, ProfileActivity.class);
                    startActivity(profileIntent);
                    return true;
                case 2: // Logout
                    logoutUser();
                    return true;
                case 3: // Login
                    Intent loginIntent = new Intent(this, LoginActivity.class);
                    startActivity(loginIntent);
                    return true;
                case 4: // Register
                    Intent registerIntent = new Intent(this, RegisterActivity.class);
                    startActivity(registerIntent);
                    return true;
                default:
                    return false;
            }
        });

        popupMenu.show();
    }

    protected void logoutUser() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String imagePath = prefs.getString("profileImagePath", null);
        if (imagePath != null) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                imageFile.delete();
            }
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        loadUserData();
        updateNavigationBar();

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
        updateNavigationBar();
    }
}