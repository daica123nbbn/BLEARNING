package com.example.lms.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lms.R;
import com.example.lms.models.Course;

import java.util.List;

public class CourseContentAdapter extends RecyclerView.Adapter<CourseContentAdapter.ChapterViewHolder> {
    private Context context;
    private List<Course.Chapter> chapters;
    private String userId;
    private Course course;
    private OnMarkAsDoneListener markAsDoneListener;

    public interface OnMarkAsDoneListener {
        void onMarkAsDone(String lectureId, int chapterPosition, int lecturePosition);
    }

    public CourseContentAdapter(Context context, Course course, String userId, OnMarkAsDoneListener listener) {
        this.context = context;
        this.chapters = course.getCourseContent();
        this.userId = userId;
        this.course = course;
        this.markAsDoneListener = listener;
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chapter_item, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        Course.Chapter chapter = chapters.get(position);
        holder.tvChapterTitle.setText(chapter.getChapterTitle());

        // Hiển thị thông tin chi tiết (số bài giảng và thời lượng)
        int lectureCount = chapter.getChapterContent().size();
        int totalDuration = chapter.getTotalDuration();
        holder.tvChapterDetails.setText(lectureCount + " lectures • " + totalDuration + "m");

        // Xử lý mở rộng/thu gọn
        boolean isExpanded = holder.isExpanded;
        holder.lecturesContainer.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.ivChevron.setImageResource(isExpanded ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);

        holder.chapterHeader.setOnClickListener(v -> {
            holder.isExpanded = !holder.isExpanded;
            holder.lecturesContainer.setVisibility(holder.isExpanded ? View.VISIBLE : View.GONE);
            holder.ivChevron.setImageResource(holder.isExpanded ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);
        });

        // Xóa các lecture cũ và thêm mới
        holder.lecturesContainer.removeAllViews();

        // Thêm các lecture vào lecturesContainer
        for (int i = 0; i < chapter.getChapterContent().size(); i++) {
            Course.Lecture lecture = chapter.getChapterContent().get(i);
            View lectureView = LayoutInflater.from(context).inflate(R.layout.lecture_item, holder.lecturesContainer, false);

            TextView tvLectureTitle = lectureView.findViewById(R.id.lectureTitle);
            TextView tvLectureDuration = lectureView.findViewById(R.id.lectureDuration);
            Button btnMarkDone = lectureView.findViewById(R.id.btn_mark_done);

            tvLectureTitle.setText(lecture.getLectureTitle());
            tvLectureDuration.setText(lecture.getLectureDuration() + " mins");

            // Kiểm tra trạng thái hoàn thành của lecture
            int completedLectures = course.getUserCompletedLectures(userId);
            if (i < completedLectures) {
                btnMarkDone.setText("Done");
                btnMarkDone.setEnabled(false);
                btnMarkDone.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            } else {
                btnMarkDone.setText("Mark as done");
                btnMarkDone.setEnabled(true);
                btnMarkDone.setTextColor(context.getResources().getColor(android.R.color.holo_blue_light));
                int lecturePosition = i;
                btnMarkDone.setOnClickListener(v -> markAsDoneListener.onMarkAsDone(lecture.getLectureId(), position, lecturePosition));
            }

            // Thêm sự kiện click để mở link video
            lectureView.setOnClickListener(v -> {
                String lectureUrl = lecture.getLectureUrl();
                if (lectureUrl != null && !lectureUrl.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(lectureUrl));
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Không có link video cho bài giảng này", Toast.LENGTH_SHORT).show();
                }
            });

            holder.lecturesContainer.addView(lectureView);
        }
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView tvChapterTitle, tvChapterDetails;
        ImageView ivChevron;
        LinearLayout chapterHeader, lecturesContainer;
        boolean isExpanded;

        public ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChapterTitle = itemView.findViewById(R.id.chapterTitle);
            tvChapterDetails = itemView.findViewById(R.id.chapterDetails);
            ivChevron = itemView.findViewById(R.id.chevronIcon);
            chapterHeader = itemView.findViewById(R.id.chapterHeader);
            lecturesContainer = itemView.findViewById(R.id.lecturesContainer);
            isExpanded = false;
        }
    }
}