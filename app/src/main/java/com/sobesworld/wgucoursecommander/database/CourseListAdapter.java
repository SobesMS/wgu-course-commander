package com.sobesworld.wgucoursecommander.database;

import android.content.Context;
import android.content.Intent;
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
import com.sobesworld.wgucoursecommander.ui.CourseDetail;

public class CourseListAdapter extends ListAdapter<CourseEntity, CourseListAdapter.CourseViewHolder> {

    public CourseListAdapter(@NonNull DiffUtil.ItemCallback<CourseEntity> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public CourseListAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        CourseEntity current = getItem(position);
        holder.courseItemView.setText(current.getCourseTitle());
    }

    public static class CourseDiff extends DiffUtil.ItemCallback<CourseEntity> {

        @Override
        public boolean areItemsTheSame(@NonNull CourseEntity oldItem, @NonNull CourseEntity newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull CourseEntity oldItem, @NonNull CourseEntity newItem) {
            return oldItem.getCourseID() == newItem.getCourseID() && oldItem.getCourseTitle().equals(newItem.getCourseTitle()) &&
                    oldItem.getCourseStartDate().equals(newItem.getCourseStartDate()) &&
                    oldItem.getCourseProjectedEndDate().equals(newItem.getCourseProjectedEndDate()) &&
                    oldItem.isCourseEndAlert() == newItem.isCourseEndAlert() && oldItem.getCourseAlertID() == newItem.getCourseAlertID() &&
                    oldItem.getCourseStatus().equals(newItem.getCourseStatus()) &&
                    oldItem.getCourseMentorsName().equals(newItem.getCourseMentorsName()) &&
                    oldItem.getCourseMentorsPhone().equals(newItem.getCourseMentorsPhone()) &&
                    oldItem.getCourseMentorsEmail().equals(newItem.getCourseMentorsEmail()) &&
                    oldItem.getCourseNotes().equals(newItem.getCourseNotes()) && oldItem.getTermID() == newItem.getTermID();
        }
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {

        private final TextView courseItemView;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseItemView = itemView.findViewById(R.id.item_view);

            itemView.setOnClickListener((view -> {
                int position = getAdapterPosition();
                Context context = view.getContext();
                final CourseEntity current = getItem(position);
                Intent intent = new Intent(context, CourseDetail.class);
                intent.putExtra(context.getString(R.string.is_new_record), false);
                intent.putExtra(context.getString(R.string.idnum), current.getCourseID());
                intent.putExtra(context.getString(R.string.title), current.getCourseTitle());
                intent.putExtra(context.getString(R.string.start_date), current.getCourseStartDate());
                intent.putExtra(context.getString(R.string.end_date), current.getCourseProjectedEndDate());
                intent.putExtra(context.getString(R.string.end_alert), current.isCourseEndAlert());
                intent.putExtra(context.getString(R.string.alert_id), current.getCourseAlertID());
                intent.putExtra(context.getString(R.string.status), current.getCourseStatus());
                intent.putExtra(context.getString(R.string.mentor), current.getCourseMentorsName());
                intent.putExtra(context.getString(R.string.phone), current.getCourseMentorsPhone());
                intent.putExtra(context.getString(R.string.email), current.getCourseMentorsEmail());
                intent.putExtra(context.getString(R.string.notes), current.getCourseNotes());
                intent.putExtra(context.getString(R.string.termID), current.getTermID());
                context.startActivity(intent);
            }));
        }
    }
}
