package com.example.lms.models;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseId;
    private String courseTitle;
    private String courseDescription;
    private String courseThumbnail;
    private float coursePrice;
    private float discount;
    private List<Rating> courseRatings;
    private String educator;
    private List<Chapter> courseContent;
    private int totalDurationMinutes;
    private int studentCount;

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
        this.totalDurationMinutes = 0;
        this.studentCount = 0;
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
        // Update total duration and number of lectures when content is set
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

    // Helper method to get rating count
    public int getRatingCount() {
        return courseRatings != null ? courseRatings.size() : 0;
    }

    // Helper method to calculate total duration and number of lectures
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

    // Helper method to get number of sections (chapters)
    public int getSectionCount() {
        return courseContent != null ? courseContent.size() : 0;
    }

    // Helper method to get total number of lectures
    public int getLectureCount() {
        if (courseContent == null) return 0;
        int totalLectures = 0;
        for (Chapter chapter : courseContent) {
            totalLectures += chapter.getChapterContent().size();
        }
        return totalLectures;
    }

    // Inner class for Rating
    public static class Rating {
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

    // Inner class for Chapter
    public static class Chapter {
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

        // Helper method to calculate total duration of the chapter
        public int getTotalDuration() {
            int totalMinutes = 0;
            for (Lecture lecture : chapterContent) {
                totalMinutes += lecture.getLectureDuration();
            }
            return totalMinutes;
        }
    }

    // Inner class for Lecture
    public static class Lecture {
        private String lectureId;
        private String lectureTitle;
        private int lectureDuration;
        private String lectureUrl;
        private boolean isPreview;
        private int lectureOrder;

        public Lecture(String lectureId, String lectureTitle, int lectureDuration, String lectureUrl, boolean isPreview, int lectureOrder) {
            this.lectureId = lectureId;
            this.lectureTitle = lectureTitle;
            this.lectureDuration = lectureDuration;
            this.lectureUrl = lectureUrl;
            this.isPreview = isPreview;
            this.lectureOrder = lectureOrder;
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

        public boolean isPreview() {
            return isPreview;
        }

        public int getLectureOrder() {
            return lectureOrder;
        }
    }
}