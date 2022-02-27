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
    private OnItemClickListener listener;

    public CourseAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<CourseEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<CourseEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull CourseEntity oldItem, @NonNull CourseEntity newItem) {
            return newItem.getCourseID() == oldItem.getCourseID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull CourseEntity oldItem, @NonNull CourseEntity newItem) {
            return newItem.getCourseTitle().equalsIgnoreCase(oldItem.getCourseTitle()) && newItem.getCourseStartDate().equals(oldItem.getCourseStartDate())
                    && newItem.getCourseEndDate().equals(oldItem.getCourseEndDate()) && newItem.isCourseEndAlert() == oldItem.isCourseEndAlert()
                    && newItem.getCourseAlertID() == oldItem.getCourseAlertID() && newItem.getCourseMentorsName().equalsIgnoreCase(oldItem.getCourseMentorsName())
                    && newItem.getCourseMentorsPhone().equalsIgnoreCase(oldItem.getCourseMentorsPhone())
                    && newItem.getCourseMentorsEmail().equalsIgnoreCase(oldItem.getCourseMentorsEmail())
                    && newItem.getCourseNotes().equalsIgnoreCase(oldItem.getCourseNotes()) && newItem.getCourseLinkedTermID() == oldItem.getCourseLinkedTermID();
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
    }

    class CourseHolder extends RecyclerView.ViewHolder {
        private final TextView textViewCourseTitle;
        private final TextView textViewCourseDates;

        private CourseHolder(@NonNull View itemView) {
            super(itemView);
            textViewCourseTitle = itemView.findViewById(R.id.text_view_course_title);
            textViewCourseDates = itemView.findViewById(R.id.text_view_course_dates);
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
