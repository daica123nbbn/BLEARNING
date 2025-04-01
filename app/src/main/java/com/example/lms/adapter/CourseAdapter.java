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
import com.example.lms.page.CourseDetail; // We'll create this activity next

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
        holder.coursePrice.setText("$" + String.format("%.2f", course.getCoursePrice()));

        // For now, we'll use a placeholder image for the thumbnail
        // In a real app, you might use Glide or Picasso to load the image from course.getCourseThumbnail()
        holder.courseImage.setImageResource(Integer.parseInt(course.getCourseThumbnail()));

        // Set click listener to navigate to the detailed view
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), CourseDetail.class);
            intent.putExtra("courseId", course.getCourseId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
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