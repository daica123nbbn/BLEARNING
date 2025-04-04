package com.example.lms.page;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lms.R;
import com.example.lms.activity.BaseActivity;
import com.example.lms.models.Course;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CourseDetail extends BaseActivity {

    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_course_detail);
        setupNavigationBar();

        // Nhận courseId từ Intent
        String courseId = getIntent().getStringExtra("courseId");

        // Tạo dữ liệu khóa học mẫu
        course = course.createDummyCourse(courseId);

        // Khởi tạo các view
        TextView courseTitle = findViewById(R.id.courseTitle);
        ImageView courseThumbnail = findViewById(R.id.imageView);
        TextView courseRating = findViewById(R.id.courseRating);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        TextView ratingCount = findViewById(R.id.ratingCount);
        TextView studentCount = findViewById(R.id.studentCount);
        TextView educator = findViewById(R.id.educator);
        TextView courseStructureSummary = findViewById(R.id.courseStructureSummary);
        LinearLayout courseContentContainer = findViewById(R.id.courseContentContainer);
        TextView coursePrice = findViewById(R.id.coursePrice);
        TextView originalPrice = findViewById(R.id.originalPrice);
        TextView discountPercent = findViewById(R.id.discountPercent);
        Button enrollButton = findViewById(R.id.enrollButton);
        TextView courseDescription = findViewById(R.id.courseDescription);

        // Gán dữ liệu cho các view
        courseTitle.setText(course.getCourseTitle());
        courseThumbnail.setImageResource(Integer.parseInt(course.getCourseThumbnail()));
        courseRating.setText(String.format("%.1f", course.getAverageRating()));
        ratingBar.setRating(course.getAverageRating());
        ratingCount.setText("(" + course.getRatingCount() + " ratings)");
        studentCount.setText(course.getStudentCount() + " students");
        educator.setText("Course by " + course.getEducator());
        courseStructureSummary.setText(course.getSectionCount() + " sections • " +
                course.getLectureCount() + " lectures • " +
                formatDuration(course.getTotalDurationMinutes()) + " total duration");

        // Hiển thị nội dung khóa học (chapters và lectures)
        for (Course.Chapter chapter : course.getCourseContent()) {
            View chapterView = LayoutInflater.from(this).inflate(R.layout.chapter_item, courseContentContainer, false);

            TextView chapterTitleView = chapterView.findViewById(R.id.chapterTitle);
            TextView chapterDetailsView = chapterView.findViewById(R.id.chapterDetails);
            ImageView chevronIcon = chapterView.findViewById(R.id.chevronIcon);
            LinearLayout lecturesContainer = chapterView.findViewById(R.id.lecturesContainer);
            LinearLayout chapterHeader = chapterView.findViewById(R.id.chapterHeader);

            chapterTitleView.setText(chapter.getChapterTitle());
            chapterDetailsView.setText(chapter.getChapterContent().size() + " lectures • " + chapter.getTotalDuration() + "m");

            for (Course.Lecture lecture : chapter.getChapterContent()) {
                View lectureView = LayoutInflater.from(this).inflate(R.layout.lecture_item, lecturesContainer, false);
                TextView lectureTitleView = lectureView.findViewById(R.id.lectureTitle);
                TextView lectureDurationView = lectureView.findViewById(R.id.lectureDuration);

                lectureTitleView.setText(lecture.getLectureTitle());
                lectureDurationView.setText(lecture.getLectureDuration() + " mins");

                lecturesContainer.addView(lectureView);
            }

            chapterHeader.setOnClickListener(v -> {
                if (lecturesContainer.getVisibility() == View.VISIBLE) {
                    lecturesContainer.setVisibility(View.GONE);
                    chevronIcon.setImageResource(android.R.drawable.arrow_down_float);
                } else {
                    lecturesContainer.setVisibility(View.VISIBLE);
                    chevronIcon.setImageResource(android.R.drawable.arrow_up_float);
                }
            });

            courseContentContainer.addView(chapterView);
        }

        // Giá và giảm giá
        float discountedPrice = course.getCoursePrice() * (1 - course.getDiscount() / 100);
        coursePrice.setText("$" + String.format("%.2f", discountedPrice));
        originalPrice.setText("$" + String.format("%.2f", course.getCoursePrice()));
        originalPrice.setPaintFlags(originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        discountPercent.setText((int) course.getDiscount() + "% off");

        courseDescription.setText(course.getCourseDescription());

        // Xử lý sự kiện bấm nút "Enroll Now"
        enrollButton.setOnClickListener(v -> {
            // Chuyển sang CourseContentActivity và truyền đối tượng Course
            Intent intent = new Intent(CourseDetail.this, CourseContent.class);
            intent.putExtra("course", course);
            startActivity(intent);
        });
    }

    private String formatDuration(int totalMinutes) {
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        return hours + "h " + minutes + "m";
    }

    @Override
    protected void performSearch(String query) {
        Toast.makeText(this, "Tìm kiếm: " + query, Toast.LENGTH_SHORT).show();
    }
}