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
import com.example.lms.models.Course;
import com.example.lms.page.CourseDetail;
import com.example.lms.page.CourseList;

import java.util.List;

public class MainActivity extends BaseActivity {
    List<Course> courseList;
    RecyclerView courseRecyclerView;
    CourseAdapter courseAdapter;

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

        Button btnShowAllCourse = findViewById(R.id.btnShowAllCourse);
        btnShowAllCourse.setOnClickListener(v -> {
            Intent courseListIntent = new Intent(MainActivity.this, CourseList.class);
            startActivity(courseListIntent);
        });

        loadCourses();
    }

    public void loadCourses() {
        // Lấy danh sách khóa học từ lớp Course
        courseList = Course.getSampleCourses();

        // Khởi tạo adapter (sự kiện nhấn đã được xử lý trong adapter)
        courseAdapter = new CourseAdapter(courseList);
        courseRecyclerView.setAdapter(courseAdapter);
    }
}