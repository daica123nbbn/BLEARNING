package com.example.lms.page;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lms.R;
import com.example.lms.activity.BaseActivity;
import com.example.lms.adapter.CourseListViewAdapter;
import com.example.lms.models.Course;

import java.util.ArrayList;
import java.util.List;

public class MyCourseActivity extends BaseActivity {
    private ListView courseListView;
    private CourseListViewAdapter adapter;
    private List<Course> courseList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_coures);
        setupNavigationBar();

        courseListView = findViewById(R.id.lst_course);
        courseList = createSampleCourses();

        adapter = new CourseListViewAdapter(this, courseList, email);
        courseListView.setAdapter(adapter);

        // Thêm sự kiện click để mở CourseContentActivity
        courseListView.setOnItemClickListener((parent, view, position, id) -> {
            Course selectedCourse = courseList.get(position);
            Intent intent = new Intent(MyCourseActivity.this, CourseContent.class);
            intent.putExtra("course", selectedCourse);
            startActivity(intent);
        });
    }

    @Override
    protected void performSearch(String query) {
        Toast.makeText(this, "Tìm kiếm: " + query, Toast.LENGTH_SHORT).show();
    }

    private List<Course> createSampleCourses() {
        List<Course> courses = new ArrayList<>();

        // Khóa học 1
        List<Course.Lecture> lectures1 = new ArrayList<>();
        lectures1.add(new Course.Lecture("lec1", "Introduction", 300, "url1"));
        lectures1.add(new Course.Lecture("lec2", "Lecture 1", 400, "url2"));

        List<Course.Chapter> chapters1 = new ArrayList<>();
        chapters1.add(new Course.Chapter("chap1", 1, "Introduction", lectures1));

        List<Course.Lecture> lectures2 = new ArrayList<>();
        lectures2.add(new Course.Lecture("lec3", "Lecture 2", 350, "url3"));
        lectures2.add(new Course.Lecture("lec4", "Lecture 3", 450, "url4"));

        chapters1.add(new Course.Chapter("chap2", 2, "Chapter 1", lectures2));

        List<Course.Rating> ratings1 = new ArrayList<>();
        ratings1.add(new Course.Rating("user1", 4.5f));

        Course course1 = new Course("course1", "Build Text to Speech", "Description 1", "thumbnail_url_1", 150f, 0.1f, ratings1, "Educator 1");
        course1.setCourseContent(chapters1);
        course1.getEnrolledStudents().add("lionfischer@gmail.com");
        course1.setUserProgress("lionfischer@gmail.com", 1);
        courses.add(course1);

        // Khóa học 2
        List<Course.Lecture> lectures3 = new ArrayList<>();
        lectures3.add(new Course.Lecture("lec5", "Introduction", 500, "url5"));
        lectures3.add(new Course.Lecture("lec6", "Lecture 1", 300, "url6"));

        List<Course.Chapter> chapters2 = new ArrayList<>();
        chapters2.add(new Course.Chapter("chap3", 1, "Introduction", lectures3));

        List<Course.Lecture> lectures4 = new ArrayList<>();
        lectures4.add(new Course.Lecture("lec7", "Lecture 2", 350, "url7"));
        lectures4.add(new Course.Lecture("lec8", "Lecture 3", 450, "url8"));

        chapters2.add(new Course.Chapter("chap4", 2, "Chapter 1", lectures4));

        List<Course.Rating> ratings2 = new ArrayList<>();
        ratings2.add(new Course.Rating("user2", 4.0f));

        Course course2 = new Course("course2", "Advanced Android", "Description 2", "thumbnail_url_2", 100f, 0.0f, ratings2, "Educator 2");
        course2.setCourseContent(chapters2);
        course2.getEnrolledStudents().add("lionfischer@gmail.com");
        course2.setUserProgress("lionfischer@gmail.com", 2);
        courses.add(course2);

        return courses;
    }
}