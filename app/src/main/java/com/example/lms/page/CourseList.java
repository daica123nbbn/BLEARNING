package com.example.lms.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lms.MainActivity;
import com.example.lms.R;
import com.example.lms.activity.BaseActivity;
import com.example.lms.adapter.CourseAdapter;
import com.example.lms.models.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseList extends BaseActivity {
    List<Course> courseList;
    RecyclerView courseRecyclerView;
    CourseAdapter courseAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        setupNavigationBar();

        setupSearchBar();

        courseRecyclerView = findViewById(R.id.allCourseRecyclerview);
        LinearLayoutManager layoutManagerAllCourse = new LinearLayoutManager(this);
        courseRecyclerView.setLayoutManager(layoutManagerAllCourse);

        TextView txtHome = findViewById(R.id.txtHome);
        txtHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseList.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); // Optional: Clear the activity stack
                startActivity(intent);
                finish();
            }
        });
        loadCourses();
    }

    public void loadCourses() {
        courseList = new ArrayList<>();

        // Course 1: React Router Complete Course
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

        // Course 2: Web Development
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

        // Course 3: Data Science
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

        // Course 4: Android Development
        List<Course.Rating> ratings4 = new ArrayList<>();
        ratings4.add(new Course.Rating("user7", 4.2f));
        ratings4.add(new Course.Rating("user8", 4.3f));
        Course course4 = new Course(
                "course4",
                "Android Development",
                "Learn to build Android apps with Kotlin.",
                String.valueOf(R.drawable.course_4), // Reuse image for demo
                49.99f,
                5f,
                ratings4,
                "Alice Brown"
        );
        courseList.add(course4);

        // Course 5: Machine Learning
        List<Course.Rating> ratings5 = new ArrayList<>();
        ratings5.add(new Course.Rating("user9", 4.6f));
        ratings5.add(new Course.Rating("user10", 4.7f));
        Course course5 = new Course(
                "course5",
                "Machine Learning",
                "Introduction to Machine Learning with Python.",
                String.valueOf(R.drawable.course_2), // Reuse image for demo
                89.99f,
                20f,
                ratings5,
                "Bob Wilson"
        );
        courseList.add(course5);

        courseAdapter = new CourseAdapter(courseList);
        courseRecyclerView.setAdapter(courseAdapter);
    }
}