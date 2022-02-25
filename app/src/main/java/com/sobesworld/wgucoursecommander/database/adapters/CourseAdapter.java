package com.sobesworld.wgucoursecommander.database.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.entity.CourseEntity;

public class CourseAdapter extends ListAdapter<CourseEntity, CourseAdapter.CourseHolder> {
    private CourseAdapter.OnItemClickListener listener;

    public CourseAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<CourseEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<CourseEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull CourseEntity oldItem, @NonNull CourseEntity newItem) {
            return oldItem.getCourseID() == newItem.getCourseID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull CourseEntity oldItem, @NonNull CourseEntity newItem) {
            return oldItem.getCourseTitle().equals(newItem.getCourseTitle()) && oldItem.getCourseStartDate().equals(newItem.getCourseStartDate())
                    && oldItem.getCourseEndDate().equals(newItem.getCourseEndDate()) && oldItem.isCourseEndAlert() == newItem.isCourseEndAlert()
                    && oldItem.getCourseAlertID() == newItem.getCourseAlertID() && oldItem.getCourseMentorsName().equals(newItem.getCourseMentorsName())
                    && oldItem.getCourseMentorsPhone().equals(newItem.getCourseMentorsPhone())
                    && oldItem.getCourseMentorsEmail().equals(newItem.getCourseMentorsEmail())
                    && oldItem.getCourseNotes().equals(newItem.getCourseNotes()) && oldItem.getCourseLinkedTermID() == newItem.getCourseLinkedTermID();
        }
    };

    public CourseEntity getCourseEntityAt(int position) {
        return getItem(position);
    }

    @NonNull
    @Override
    public CourseAdapter.CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_item, parent, false);
        return new CourseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseHolder holder, int position) {
        CourseEntity current = getItem(position);
        holder.textViewCourseTitle.setText(current.getCourseTitle());
        String dates = current.getCourseStartDate() + " to " + current.getCourseEndDate();
        holder.textViewCourseDates.setText(dates);
        String status = "Status: " + current.getCourseStatus();
        holder.textViewCourseStatus.setText(status);
    }

    class CourseHolder extends RecyclerView.ViewHolder {
        private final TextView textViewCourseTitle;
        private final TextView textViewCourseDates;
        private final TextView textViewCourseStatus;

        private CourseHolder(@NonNull View itemView) {
            super(itemView);
            textViewCourseTitle = itemView.findViewById(R.id.text_view_course_title);
            textViewCourseDates = itemView.findViewById(R.id.text_view_course_dates);
            textViewCourseStatus = itemView.findViewById(R.id.text_view_course_status);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(CourseEntity courseEntity);
    }

    public void setOnItemClickListener(CourseAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
