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
import com.sobesworld.wgucoursecommander.database.entity.AssessmentEntity;
import com.sobesworld.wgucoursecommander.ui.AssessmentDetail;

public class AssessmentListAdapter extends ListAdapter<AssessmentEntity, AssessmentListAdapter.AssessmentViewHolder> {

    public AssessmentListAdapter(@NonNull DiffUtil.ItemCallback<AssessmentEntity> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public AssessmentListAdapter.AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new AssessmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentListAdapter.AssessmentViewHolder holder, int position) {
        AssessmentEntity current = getItem(position);
        holder.assessmentItemView.setText(current.getAssessmentTitle());
    }

    public static class AssessmentDiff extends DiffUtil.ItemCallback<AssessmentEntity> {

        @Override
        public boolean areItemsTheSame(@NonNull AssessmentEntity oldItem, @NonNull AssessmentEntity newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull AssessmentEntity oldItem, @NonNull AssessmentEntity newItem) {
            return oldItem.getAssessmentID() == newItem.getAssessmentID() && oldItem.getAssessmentTitle().equals(newItem.getAssessmentTitle()) &&
                    oldItem.getAssessmentType().equals(newItem.getAssessmentType()) &&
                    oldItem.getAssessmentGoalDate().equals(newItem.getAssessmentGoalDate()) &&
                    oldItem.isAssessmentGoalAlert() == newItem.isAssessmentGoalAlert() && oldItem.getAssessmentAlertID() == newItem.getAssessmentAlertID() &&
                    oldItem.getAssessmentNotes().equals(newItem.getAssessmentNotes()) && oldItem.getCourseID() == newItem.getCourseID();
        }
    }

    class AssessmentViewHolder extends RecyclerView.ViewHolder {

        private final TextView assessmentItemView;

        public AssessmentViewHolder(View itemView) {
            super(itemView);
            assessmentItemView = itemView.findViewById(R.id.item_view);

            itemView.setOnClickListener((view -> {
                int position = getAdapterPosition();
                Context context = view.getContext();
                final AssessmentEntity current = getItem(position);
                Intent intent = new Intent(context, AssessmentDetail.class);
                intent.putExtra(context.getString(R.string.is_new_record), false);
                intent.putExtra(context.getString(R.string.idnum), current.getAssessmentID());
                intent.putExtra(context.getString(R.string.title), current.getAssessmentTitle());
                intent.putExtra(context.getString(R.string.assessment_type), current.getAssessmentType());
                intent.putExtra(context.getString(R.string.completion_goal_date), current.getAssessmentGoalDate());
                intent.putExtra(context.getString(R.string.goal_alert), current.isAssessmentGoalAlert());
                intent.putExtra(context.getString(R.string.alert_id), current.getAssessmentAlertID());
                intent.putExtra(context.getString(R.string.notes), current.getAssessmentNotes());
                intent.putExtra(context.getString(R.string.courseID), current.getCourseID());
                context.startActivity(intent);
            }));
        }
    }
}
