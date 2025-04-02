package com.example.lms;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lms.activity.BaseActivity;
import com.example.lms.adapter.CourseAdapter;
import com.example.lms.adapter.UserAdapter;
import com.example.lms.models.Course;
import com.example.lms.models.User;
import com.example.lms.page.CourseList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity {
    List<Course> courseList;
    RecyclerView courseRecyclerView;
    CourseAdapter courseAdapter;
    List<User> userList;
    RecyclerView testimonialRecyclerView;
    UserAdapter userAdapter;

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


        setupNavigationBar();

        setupSearchBar();

        courseRecyclerView = findViewById(R.id.courseRecyclerView);
        LinearLayoutManager layoutManagerCourse = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        courseRecyclerView.setLayoutManager(layoutManagerCourse);

        testimonialRecyclerView = findViewById(R.id.testimonialRecyclerView);
        LinearLayoutManager layoutManagerTestimonial = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        testimonialRecyclerView.setLayoutManager(layoutManagerTestimonial);

        Button btnShowAllCourse = findViewById(R.id.btnShowAllCourse);
        btnShowAllCourse.setOnClickListener(v -> {
            Intent courseListIntent = new Intent(MainActivity.this, CourseList.class);
            startActivity(courseListIntent);
        });

        loadCourses();
        loadUsers();
    }

    public void loadCourses() {
        courseList = new ArrayList<>();

        List<Course.Rating> ratings1 = new ArrayList<>();
        ratings1.add(new Course.Rating("user1", 4.5f));
        ratings1.add(new Course.Rating("user2", 4.0f));
        Course course1 = new Course(
                "course1",
                "React Router Complete Course in One Video",
                "Learn React Router in depth with this comprehensive course.",
                String.valueOf(R.drawable.course_1),
                10.99f,
                0f,
                ratings1,
                "Richard James"
        );
        courseList.add(course1);

        List<Course.Rating> ratings2 = new ArrayList<>();
        ratings2.add(new Course.Rating("user3", 4.7f));
        ratings2.add(new Course.Rating("user4", 4.8f));
        Course course2 = new Course(
                "course2",
                "Web Development",
                "Master JavaScript and build modern web applications.",
                String.valueOf(R.drawable.course_2),
                59.99f,
                10f,
                ratings2,
                "John Doe"
        );
        courseList.add(course2);

        List<Course.Rating> ratings3 = new ArrayList<>();
        ratings3.add(new Course.Rating("user5", 4.8f));
        ratings3.add(new Course.Rating("user6", 4.9f));
        Course course3 = new Course(
                "course3",
                "Data Science",
                "Python for Data Analysis and Machine Learning.",
                String.valueOf(R.drawable.course_3),
                79.99f,
                15f,
                ratings3,
                "Jane Smith"
        );
        courseList.add(course3);

        courseAdapter = new CourseAdapter(courseList);
        courseRecyclerView.setAdapter(courseAdapter);
    }

    public void loadUsers() {
        userList = new ArrayList<>();

        User user1 = new User(
                "user1",
                "Donald Jackman",
                "donald.jackman@example.com",
                String.valueOf(R.drawable.profile_img_1),
                Arrays.asList("course1", "course2"),
                "password123",
                "SWE 1 @ Amazon",
                4.0f,
                "I've been using imagify for nearly two years, primarily for Instagram, and it has been incredible user-friendly, making my work much easier."
        );
        userList.add(user1);

        User user2 = new User(
                "user2",
                "Jane Smith",
                "jane.smith@example.com",
                String.valueOf(R.drawable.profile_img_2),
                Arrays.asList("course2", "course3"),
                "password456",
                "Data Scientist @ Google",
                4.5f,
                "This platform has been a game-changer for my learning journey. The courses are well-structured and easy to follow."
        );
        userList.add(user2);

        User user3 = new User(
                "user3",
                "Richard James",
                "richard.james@example.com",
                String.valueOf(R.drawable.profile_img_3),
                Arrays.asList("course1"),
                "password789",
                "Educator @ Udemy",
                4.8f,
                "I highly recommend this app to anyone looking to upskill. The content is top-notch and the support is excellent."
        );
        userList.add(user3);

        userAdapter = new UserAdapter(userList);
        testimonialRecyclerView.setAdapter(userAdapter);
    }
}