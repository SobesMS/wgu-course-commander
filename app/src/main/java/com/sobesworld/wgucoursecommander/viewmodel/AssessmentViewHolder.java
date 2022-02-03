package com.sobesworld.wgucoursecommander.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sobesworld.wgucoursecommander.R;

public class AssessmentViewHolder extends RecyclerView.ViewHolder {

    private final TextView assessmentItemView;

    private AssessmentViewHolder(View itemView) {
        super(itemView);
        assessmentItemView = itemView.findViewById(R.id.assessment_item);
    }

    public void bind(String text) {
        assessmentItemView.setText(text);
    }

    public static AssessmentViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assessment_recyclerview_item, parent, false);
        return new AssessmentViewHolder(view);
    }
}
