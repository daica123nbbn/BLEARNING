package com.example.lms.models;

import java.util.List;

public class User {
    private String id;
    private String name;
    private String email;
    private String imageUrl;
    private List<String> enrolledCourses;
    private String password;
    private String jobTitle;
    private float rating;
    private String review;

    // Constructor
    public User(String id, String name, String email, String imageUrl, List<String> enrolledCourses, String password, String jobTitle, float rating, String review) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.enrolledCourses = enrolledCourses;
        this.password = password;
        this.jobTitle = jobTitle;
        this.rating = rating;
        this.review = review;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(List<String> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getEnrolledCoursesCount() {
        return enrolledCourses != null ? enrolledCourses.size() : 0;
    }
}