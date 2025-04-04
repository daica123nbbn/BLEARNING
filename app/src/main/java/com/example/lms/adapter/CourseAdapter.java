package com.example.lms.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lms.R;
import com.example.lms.models.Course;
import com.example.lms.page.CourseDetail;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private List<Course> courseList;

    public CourseAdapter(List<Course> courseList) {
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = courseList.get(position);

        // Bind data to the views
        holder.courseTitle.setText(course.getCourseTitle());
        holder.educator.setText(course.getEducator());
        holder.courseRating.setText(String.format("%.1f", course.getAverageRating()));
        holder.ratingBar.setRating(course.getAverageRating());
        holder.ratingCount.setText("(" + course.getRatingCount() + ")");
        float discountedPrice = course.getCoursePrice() * (1 - course.getDiscount() / 100);
        holder.coursePrice.setText("$" + String.format("%.2f", discountedPrice));

        // Hiển thị ảnh
        holder.courseImage.setImageResource(Integer.parseInt(course.getCourseThumbnail()));

        // Xử lý sự kiện nhấn trực tiếp trong adapter
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), CourseDetail.class);
            intent.putExtra("courseId", course.getCourseId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return courseList != null ? courseList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView courseImage;
        TextView courseTitle;
        TextView educator;
        TextView courseRating;
        RatingBar ratingBar;
        TextView ratingCount;
        TextView coursePrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseImage = itemView.findViewById(R.id.courseImage);
            courseTitle = itemView.findViewById(R.id.courseTitle);
            educator = itemView.findViewById(R.id.educator);
            courseRating = itemView.findViewById(R.id.courseRating);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            ratingCount = itemView.findViewById(R.id.ratingCount);
            coursePrice = itemView.findViewById(R.id.coursePrice);
        }
    }
}