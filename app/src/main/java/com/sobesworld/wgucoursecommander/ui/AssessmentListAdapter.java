package com.sobesworld.wgucoursecommander.ui;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.sobesworld.wgucoursecommander.database.entity.AssessmentEntity;
import com.sobesworld.wgucoursecommander.viewmodel.AssessmentViewHolder;

public class AssessmentListAdapter extends ListAdapter<AssessmentEntity, AssessmentViewHolder> {

    public AssessmentListAdapter(@NonNull DiffUtil.ItemCallback<AssessmentEntity> diffCallBack) {
        super(diffCallBack);
    }

    @Override
    public AssessmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return AssessmentViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(AssessmentViewHolder holder, int position) {
        AssessmentEntity current = getItem(position);
        holder.bind(current.getAssessmentTitle());
    }

    static class AssessmentDiff extends DiffUtil.ItemCallback<AssessmentEntity> {

        @Override
        public boolean areItemsTheSame(@NonNull AssessmentEntity oldItem, @NonNull AssessmentEntity newItem) {
            return oldItem.getAssessmentID() == newItem.getAssessmentID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull AssessmentEntity oldItem, @NonNull AssessmentEntity newItem) {
            return oldItem.getAssessmentTitle().equals(newItem.getAssessmentTitle());
        }
    }
}
