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
        txtHome.setOnClickListener(v -> {
            Intent intent = new Intent(CourseList.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
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