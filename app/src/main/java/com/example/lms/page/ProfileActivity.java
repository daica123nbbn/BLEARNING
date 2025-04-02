package com.example.lms.page;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lms.R;

public class ProfileActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView emailTextView;
    private TextView jobTitleTextView;
    private TextView enrolledCoursesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        nameTextView = findViewById(R.id.name);
        emailTextView = findViewById(R.id.email);
        jobTitleTextView = findViewById(R.id.jobTitle);
        enrolledCoursesTextView = findViewById(R.id.enrolledCourses);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);
        String fullName = prefs.getString("fullName", "Unknown");
        String email = prefs.getString("email", "Unknown");

        if (userId == null) {
            finish();
            return;
        }

        nameTextView.setText("Name: " + fullName);
        emailTextView.setText("Email: " + email);
        jobTitleTextView.setText("Job Title: N/A");
        enrolledCoursesTextView.setText("Enrolled Courses: 0");
    }
}