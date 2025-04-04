package com.example.lms.page;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lms.R;
import com.example.lms.activity.BaseActivity;
import com.example.lms.adapter.CourseContentAdapter;
import com.example.lms.models.Course;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CourseContent extends BaseActivity {
    private RecyclerView rvCourseContent;
    private TextView tvStartDate;
    private Course course;
    private CourseContentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_content);
        setupNavigationBar();

        // Nhận dữ liệu khóa học từ Intent
        course = (Course) getIntent().getSerializableExtra("course");
        if (course == null) {
            Toast.makeText(this, "Không tìm thấy khóa học", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Khởi tạo view
        tvStartDate = findViewById(R.id.tv_start_date);
        rvCourseContent = findViewById(R.id.rv_course_content);

        // Hiển thị ngày bắt đầu
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        tvStartDate.setText("Course start date: " + sdf.format(new Date()));

        // Thiết lập RecyclerView
        rvCourseContent.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CourseContentAdapter(this, course, email, (lectureId, chapterPosition, lecturePosition) -> {
            // Xử lý khi người dùng bấm "Mark as done"
            int completedLectures = course.getUserCompletedLectures(email);
            completedLectures++;
            course.setUserProgress(email, completedLectures);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Đã đánh dấu hoàn thành: " + lectureId, Toast.LENGTH_SHORT).show();
        });
        rvCourseContent.setAdapter(adapter);
    }

    @Override
    protected void performSearch(String query) {
        Toast.makeText(this, "Tìm kiếm: " + query, Toast.LENGTH_SHORT).show();
    }
}