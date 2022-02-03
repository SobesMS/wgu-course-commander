package com.sobesworld.wgucoursecommander.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sobesworld.wgucoursecommander.R;

public class CourseViewHolder extends RecyclerView.ViewHolder {

    private final TextView courseItemView;

    private CourseViewHolder(View itemView) {
        super(itemView);
        courseItemView = itemView.findViewById(R.id.course_item);
    }

    public void bind(String text) {
        courseItemView.setText(text);
    }

    public static CourseViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_recyclerview_item, parent, false);
        return new CourseViewHolder(view);
    }
}
