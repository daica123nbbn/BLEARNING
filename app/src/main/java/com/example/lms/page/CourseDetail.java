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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lms.R;
import com.example.lms.activity.BaseActivity;
import com.example.lms.models.Course;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CourseDetail extends BaseActivity {

    private ActivityResultLauncher<Intent> paymentLauncher;
    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        setupNavigationBar();

        paymentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Toast.makeText(this, "Enrollment successful!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Enrollment failed or canceled.", Toast.LENGTH_LONG).show();
            }
        });

        String courseId = getIntent().getStringExtra("courseId");

        course = createDummyCourse(courseId);

        // Bind the data to the views
        TextView courseTitle = findViewById(R.id.courseTitle);
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

        // Populate the UI
        courseTitle.setText(course.getCourseTitle());
        courseRating.setText(String.format("%.1f", course.getAverageRating()));
        ratingBar.setRating(course.getAverageRating());
        ratingCount.setText("(" + course.getRatingCount() + " ratings)");
        studentCount.setText(course.getStudentCount() + " students");
        educator.setText("Course by " + course.getEducator());
        courseStructureSummary.setText(course.getSectionCount() + " sections • " +
                course.getLectureCount() + " lectures • " +
                formatDuration(course.getTotalDurationMinutes()) + " total duration");

        // Populate course content (chapters and lectures)
        for (Course.Chapter chapter : course.getCourseContent()) {
            // Inflate the chapter item layout
            View chapterView = LayoutInflater.from(this).inflate(R.layout.chapter_item, courseContentContainer, false);

            // Bind chapter data
            TextView chapterTitleView = chapterView.findViewById(R.id.chapterTitle);
            TextView chapterDetailsView = chapterView.findViewById(R.id.chapterDetails);
            ImageView chevronIcon = chapterView.findViewById(R.id.chevronIcon);
            LinearLayout lecturesContainer = chapterView.findViewById(R.id.lecturesContainer);
            LinearLayout chapterHeader = chapterView.findViewById(R.id.chapterHeader);

            chapterTitleView.setText(chapter.getChapterTitle());
            chapterDetailsView.setText(chapter.getChapterContent().size() + " lectures • " + chapter.getTotalDuration() + "m");

            // Populate lectures
            for (Course.Lecture lecture : chapter.getChapterContent()) {
                View lectureView = LayoutInflater.from(this).inflate(R.layout.lecture_item, lecturesContainer, false);
                TextView lectureTitleView = lectureView.findViewById(R.id.lectureTitle);
                TextView lectureDurationView = lectureView.findViewById(R.id.lectureDuration);

                lectureTitleView.setText(lecture.getLectureTitle());
                lectureDurationView.setText(lecture.getLectureDuration() + " mins");

                lecturesContainer.addView(lectureView);
            }

            // Implement expand/collapse functionality
            chapterHeader.setOnClickListener(v -> {
                if (lecturesContainer.getVisibility() == View.VISIBLE) {
                    lecturesContainer.setVisibility(View.GONE);
                    chevronIcon.setImageResource(android.R.drawable.arrow_down_float);
                } else {
                    lecturesContainer.setVisibility(View.VISIBLE);
                    chevronIcon.setImageResource(android.R.drawable.arrow_up_float);
                }
            });

            // Add the chapter view to the container
            courseContentContainer.addView(chapterView);
        }

        // Price and discount
        float discountedPrice = course.getCoursePrice() * (1 - course.getDiscount() / 100);
        coursePrice.setText("$" + String.format("%.2f", discountedPrice));
        originalPrice.setText("$" + String.format("%.2f", course.getCoursePrice()));
        originalPrice.setPaintFlags(originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        discountPercent.setText((int) course.getDiscount() + "% off");

        // Course description
        courseDescription.setText(course.getCourseDescription());

        // Enroll button click
        enrollButton.setOnClickListener(v -> {
            Intent intent = new Intent(CourseDetail.this, PaymentActivity.class);
            intent.putExtra("amount", (double) discountedPrice);
            intent.putExtra("courseId", "67eb4e8ba1a15739ec04faf4"); // Gửi courseId
            intent.putExtra("userId", "user_2v8QH56EvK9klfDzMPKgVy4m8iT"); // Gửi userId (thay bằng userId thực tế)
            paymentLauncher.launch(intent);
        });
    }

    // Helper method to format duration (e.g., 165 minutes -> "2h 45m")
    private String formatDuration(int totalMinutes) {
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        return hours + "h " + minutes + "m";
    }

    // Dummy method to create a course for testing
    private Course createDummyCourse(String courseId) {
        List<Course.Rating> ratings = new ArrayList<>();
        ratings.add(new Course.Rating("user1", 4.5f));
        ratings.add(new Course.Rating("user2", 4.0f));

        // Create lectures for each chapter
        List<Course.Lecture> projectIntroLectures = Arrays.asList(
                new Course.Lecture("lec1", "App Overview - Build Text-to-Image SaaS", 10, "url1", true, 1),
                new Course.Lecture("lec2", "Tech Stack - React, Node.js, MongoDB", 15, "url2", false, 2),
                new Course.Lecture("lec3", "Core Features - Authentication & payment", 20, "url3", false, 3)
        );

        List<Course.Lecture> projectSetupLectures = Arrays.asList(
                new Course.Lecture("lec4", "Environment Setup - Node.js, VS Code", 10, "url4", false, 1),
                new Course.Lecture("lec5", "Repo Setup - Clone project repository", 10, "url5", false, 2),
                new Course.Lecture("lec6", "Install Dependencies - Set up packages", 10, "url6", false, 3),
                new Course.Lecture("lec7", "Initial Config - Set up files & folders", 15, "url7", false, 4)
        );

        List<Course.Lecture> tailwindSetupLectures = Arrays.asList(
                new Course.Lecture("lec8", "Tailwind Setup", 45, "url8", false, 1)
        );

        List<Course.Lecture> frontendProjectLectures = Arrays.asList(
                new Course.Lecture("lec9", "Frontend Project", 45, "url9", false, 1)
        );

        List<Course.Lecture> backendProjectLectures = Arrays.asList(
                new Course.Lecture("lec10", "Backend Project", 45, "url10", false, 1)
        );

        List<Course.Lecture> paymentIntegrationLectures = Arrays.asList(
                new Course.Lecture("lec11", "Payment Integration", 45, "url11", false, 1)
        );

        List<Course.Lecture> projectDeploymentLectures = Arrays.asList(
                new Course.Lecture("lec12", "Project Deployment", 45, "url12", false, 1)
        );

        // Create chapters
        List<Course.Chapter> chapters = Arrays.asList(
                new Course.Chapter("chap1", 1, "Project Intro", projectIntroLectures),
                new Course.Chapter("chap2", 2, "Project Setup and config", projectSetupLectures),
                new Course.Chapter("chap3", 3, "Tailwind Setup", tailwindSetupLectures),
                new Course.Chapter("chap4", 4, "Frontend Project", frontendProjectLectures),
                new Course.Chapter("chap5", 5, "Backend Project", backendProjectLectures),
                new Course.Chapter("chap6", 6, "Payment Integration", paymentIntegrationLectures),
                new Course.Chapter("chap7", 7, "Project Deployment", projectDeploymentLectures)
        );

        Course course = new Course(
                courseId,
                "Master MERN Stack by building a Full Stack Text to Image SaaS App using React JS, MongoDB, Node JS, Express JS and Stripe Payment",
                "• Lifetime access with free updates.\n" +
                        "• Step-by-step, hands-on project guidance.\n" +
                        "• Downloadable resources and source code.\n" +
                        "• Quizzes to test your knowledge.\n" +
                        "• Certificate of completion.",
                String.valueOf(R.drawable.course_1),
                19.99f,
                50f,
                ratings,
                "Richard James"
        );
        course.setCourseContent(chapters);
        course.setStudentCount(21);
        return course;
    }
}