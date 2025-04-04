package com.example.lms.models;

import com.example.lms.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Course implements Serializable {
    private String courseId;
    private String courseTitle;
    private String courseDescription;
    private String courseThumbnail;
    private float coursePrice;
    private float discount;
    private List<Rating> courseRatings;
    private String educator;
    private List<Chapter> courseContent;
    private List<String> enrolledStudents;
    private int totalDurationMinutes;
    private int studentCount;

    private Map<String, Integer> userProgress; // Lưu tiến độ của từng user

    // Danh sách khóa học mẫu
    private static List<Course> sampleCourses;
    // Constructor
    public Course(String courseId, String courseTitle, String courseDescription, String courseThumbnail,
                  float coursePrice, float discount, List<Rating> courseRatings, String educator) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.courseThumbnail = courseThumbnail;
        this.coursePrice = coursePrice;
        this.discount = discount;
        this.courseRatings = courseRatings;
        this.educator = educator;
        this.courseContent = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
        this.totalDurationMinutes = 0;
        this.studentCount = 0;
        this.userProgress = new HashMap<>();
    }

    // Getters and setters
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseThumbnail() {
        return courseThumbnail;
    }

    public void setCourseThumbnail(String courseThumbnail) {
        this.courseThumbnail = courseThumbnail;
    }

    public float getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(float coursePrice) {
        this.coursePrice = coursePrice;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public List<Rating> getCourseRatings() {
        return courseRatings;
    }

    public void setCourseRatings(List<Rating> courseRatings) {
        this.courseRatings = courseRatings;
    }

    public String getEducator() {
        return educator;
    }

    public void setEducator(String educator) {
        this.educator = educator;
    }

    public List<Chapter> getCourseContent() {
        return courseContent;
    }

    public void setCourseContent(List<Chapter> courseContent) {
        this.courseContent = courseContent;
        updateCourseStats();
    }

    public int getTotalDurationMinutes() {
        return totalDurationMinutes;
    }

    public void setTotalDurationMinutes(int totalDurationMinutes) {
        this.totalDurationMinutes = totalDurationMinutes;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    // Helper method to calculate average rating
    public float getAverageRating() {
        if (courseRatings == null || courseRatings.isEmpty()) return 0f;
        float sum = 0f;
        for (Rating rating : courseRatings) {
            sum += rating.getRating();
        }
        return sum / courseRatings.size();
    }

    public int getRatingCount() {
        return courseRatings != null ? courseRatings.size() : 0;
    }

    private void updateCourseStats() {
        if (courseContent == null) return;
        int totalMinutes = 0;
        for (Chapter chapter : courseContent) {
            for (Lecture lecture : chapter.getChapterContent()) {
                totalMinutes += lecture.getLectureDuration();
            }
        }
        this.totalDurationMinutes = totalMinutes;
    }

    public int getSectionCount() {
        return courseContent != null ? courseContent.size() : 0;
    }

    public int getLectureCount() {
        if (courseContent == null) return 0;
        int totalLectures = 0;
        for (Chapter chapter : courseContent) {
            totalLectures += chapter.getChapterContent().size();
        }
        return totalLectures;
    }

    public void setUserProgress(String userId, int completedLectures) {
        userProgress.put(userId, completedLectures);
    }

    // Getter cho tiến độ của user
    public int getUserCompletedLectures(String userId) {
        return userProgress.getOrDefault(userId, 0);
    }

    public float getCompletionPercentage(String userId) {
        int completedLectures = getUserCompletedLectures(userId);
        int totalLectures = getLectureCount();
        if (totalLectures == 0) return 0;
        return (completedLectures * 100.0f) / totalLectures;
    }

    public List<String> getEnrolledStudents() { return enrolledStudents; }
    public void setEnrolledStudents(List<String> enrolledStudents) { this.enrolledStudents = enrolledStudents; }
    public static class Rating implements Serializable{
        private String userId;
        private float rating;

        public Rating(String userId, float rating) {
            this.userId = userId;
            this.rating = rating;
        }

        public String getUserId() {
            return userId;
        }

        public float getRating() {
            return rating;
        }
    }

    public static class Chapter implements Serializable{
        private String chapterId;
        private int chapterOrder;
        private String chapterTitle;
        private List<Lecture> chapterContent;

        public Chapter(String chapterId, int chapterOrder, String chapterTitle, List<Lecture> chapterContent) {
            this.chapterId = chapterId;
            this.chapterOrder = chapterOrder;
            this.chapterTitle = chapterTitle;
            this.chapterContent = chapterContent;
        }

        public String getChapterId() {
            return chapterId;
        }

        public int getChapterOrder() {
            return chapterOrder;
        }

        public String getChapterTitle() {
            return chapterTitle;
        }

        public List<Lecture> getChapterContent() {
            return chapterContent;
        }

        public int getTotalDuration() {
            int totalMinutes = 0;
            for (Lecture lecture : chapterContent) {
                totalMinutes += lecture.getLectureDuration();
            }
            return totalMinutes;
        }
    }

    public static class Lecture implements Serializable{
        private String lectureId;
        private String lectureTitle;
        private int lectureDuration;
        private String lectureUrl;

        public Lecture(String lectureId, String lectureTitle, int lectureDuration, String lectureUrl) {
            this.lectureId = lectureId;
            this.lectureTitle = lectureTitle;
            this.lectureDuration = lectureDuration;
            this.lectureUrl = lectureUrl;
        }

        public String getLectureId() {
            return lectureId;
        }

        public String getLectureTitle() {
            return lectureTitle;
        }

        public int getLectureDuration() {
            return lectureDuration;
        }

        public String getLectureUrl() {
            return lectureUrl;
        }

    }
    public static List<Course> getSampleCourses() {
        List<Course> sampleCourses = new ArrayList<>();

        // Khóa học 1: MERN Stack Text-to-Image SaaS
        List<Course.Rating> ratings1 = Arrays.asList(
                new Course.Rating("user1", 4.5f),
                new Course.Rating("user2", 4.0f)
        );
        List<Course.Lecture> lectures1 = Arrays.asList(
                new Course.Lecture("lec1", "App Overview", 10, "url1"),
                new Course.Lecture("lec2", "Tech Stack", 15, "url2")
        );
        List<Course.Chapter> chapters1 = Arrays.asList(
                new Course.Chapter("chap1", 1, "Project Intro", lectures1)
        );
        Course course1 = new Course(
                "course1",
                "Master MERN Stack by building a Full Stack Text to Image SaaS App",
                "Learn to build a SaaS app with React, Node.js, MongoDB, and Stripe.",
                String.valueOf(R.drawable.course_1),
                19.99f,
                50f,
                ratings1,
                "Richard James"
        );
        course1.setCourseContent(chapters1);
        course1.setStudentCount(21);
        sampleCourses.add(course1);

        // Khóa học 2: Python for Data Science
        List<Course.Rating> ratings2 = Arrays.asList(
                new Course.Rating("user3", 4.7f),
                new Course.Rating("user4", 4.8f)
        );
        List<Course.Lecture> lectures2 = Arrays.asList(
                new Course.Lecture("lec3", "Python Basics", 20, "url3"),
                new Course.Lecture("lec4", "Data Analysis with Pandas", 25, "url4")
        );
        List<Course.Chapter> chapters2 = Arrays.asList(
                new Course.Chapter("chap2", 1, "Data Science Intro", lectures2)
        );
        Course course2 = new Course(
                "course2",
                "Python for Data Science and Machine Learning",
                "Master Python for data analysis and machine learning.",
                String.valueOf(R.drawable.course_2),
                59.99f,
                10f,
                ratings2,
                "Jane Smith"
        );
        course2.setCourseContent(chapters2);
        course2.setStudentCount(35);
        sampleCourses.add(course2);

        // Khóa học 3: Advanced JavaScript
        List<Course.Rating> ratings3 = Arrays.asList(
                new Course.Rating("user5", 4.8f),
                new Course.Rating("user6", 4.9f)
        );
        List<Course.Lecture> lectures3 = Arrays.asList(
                new Course.Lecture("lec5", "ES6 Features", 15, "url5"),
                new Course.Lecture("lec6", "Async Programming", 20, "url6")
        );
        List<Course.Chapter> chapters3 = Arrays.asList(
                new Course.Chapter("chap3", 1, "JavaScript Basics", lectures3)
        );
        Course course3 = new Course(
                "course3",
                "Advanced JavaScript: From Zero to Hero",
                "Deep dive into modern JavaScript and build real-world projects.",
                String.valueOf(R.drawable.course_3),
                39.99f,
                20f,
                ratings3,
                "John Doe"
        );
        course3.setCourseContent(chapters3);
        course3.setStudentCount(50);
        sampleCourses.add(course3);

        return sampleCourses;
    }

    // Phương thức tạo khóa học giả dựa trên courseId
    public static Course createDummyCourse(String courseId) {
        List<Course> sampleCourses = getSampleCourses();
        for (Course course : sampleCourses) {
            if (course.getCourseId().equals(courseId)) {
                return course;
            }
        }
        return sampleCourses.get(0);
    }
}