package com.sobesworld.wgucoursecommander.ui;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.sobesworld.wgucoursecommander.database.entity.CourseEntity;
import com.sobesworld.wgucoursecommander.viewmodel.CourseViewHolder;

public class CourseListAdapter extends ListAdapter<CourseEntity, CourseViewHolder> {

    public CourseListAdapter(@NonNull DiffUtil.ItemCallback<CourseEntity> diffCallBack) {
        super(diffCallBack);
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return CourseViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        CourseEntity current = getItem(position);
        holder.bind(current.getCourseTitle());
    }

    static class CourseDiff extends DiffUtil.ItemCallback<CourseEntity> {

        @Override
        public boolean areItemsTheSame(@NonNull CourseEntity oldItem, @NonNull CourseEntity newItem) {
            return oldItem.getCourseID() == newItem.getCourseID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull CourseEntity oldItem, @NonNull CourseEntity newItem) {
            return oldItem.getCourseTitle().equals(newItem.getCourseTitle());
        }
    }
}
