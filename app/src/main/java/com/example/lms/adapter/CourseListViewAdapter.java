package com.example.lms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lms.R;
import com.example.lms.models.Course;

import java.util.List;

public class CourseListViewAdapter extends BaseAdapter {
    private Context context;
    private List<Course> courseList;
    private String currentUserEmail; // Email của user hiện tại

    public CourseListViewAdapter(Context context, List<Course> courseList, String currentUserEmail) {
        this.context = context;
        this.courseList = courseList;
        this.currentUserEmail = currentUserEmail;
    }

    @Override
    public int getCount() {
        return courseList.size();
    }

    @Override
    public Object getItem(int position) {
        return courseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.course_item, parent, false);
        }

        TextView courseTitle = convertView.findViewById(R.id.course_title);
        TextView courseEducator = convertView.findViewById(R.id.course_number);
        TextView coursePrice = convertView.findViewById(R.id.course_price);
        TextView courseCompleted = convertView.findViewById(R.id.course_completed);
        ImageView courseImage = convertView.findViewById(R.id.course_image);

        Course course = courseList.get(position);
        courseTitle.setText(course.getCourseTitle());
        courseEducator.setText("By " + course.getEducator());
        float price = course.getCoursePrice() * (1 - course.getDiscount());
        coursePrice.setText(String.format("$%.2f", price));
        courseImage.setImageResource(R.drawable.course_1);

        // Hiển thị phần trăm hoàn thành của user hiện tại
        float completionPercentage = course.getCompletionPercentage(currentUserEmail);
        courseCompleted.setText(String.format("%.0f%%", completionPercentage));

        return convertView;
    }
}