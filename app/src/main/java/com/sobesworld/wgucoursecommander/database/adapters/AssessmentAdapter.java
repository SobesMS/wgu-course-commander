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
import com.sobesworld.wgucoursecommander.database.entity.AssessmentEntity;

public class AssessmentAdapter extends ListAdapter<AssessmentEntity, AssessmentAdapter.AssessmentHolder> {
    private AssessmentAdapter.OnItemClickListener listener;

    public AssessmentAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<AssessmentEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<AssessmentEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull AssessmentEntity oldItem, @NonNull AssessmentEntity newItem) {
            return oldItem.getAssessmentID() == newItem.getAssessmentID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull AssessmentEntity oldItem, @NonNull AssessmentEntity newItem) {
            return oldItem.getAssessmentTitle().equals(newItem.getAssessmentTitle())
                    && oldItem.getAssessmentType().equals(newItem.getAssessmentType())
                    && oldItem.getAssessmentGoalDate().equals(newItem.getAssessmentGoalDate())
                    && oldItem.isAssessmentGoalAlert() == newItem.isAssessmentGoalAlert()
                    && oldItem.getAssessmentAlertID() == newItem.getAssessmentAlertID()
                    && oldItem.getAssessmentNotes().equals(newItem.getAssessmentNotes())
                    && oldItem.getAssessmentLinkedCourseID() == newItem.getAssessmentLinkedCourseID();
        }
    };

    public AssessmentEntity getAssessmentEntityAt(int position) {
        return getItem(position);
    }

    @NonNull
    @Override
    public AssessmentAdapter.AssessmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assessment_item, parent, false);
        return new AssessmentHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapter.AssessmentHolder holder, int position) {
        AssessmentEntity current = getItem(position);
    }

    class AssessmentHolder extends RecyclerView.ViewHolder {
        private final TextView textViewAssessmentTitle;
        private final TextView textViewAssessmentGoalDate;

        private AssessmentHolder(@NonNull View itemView) {
            super(itemView);
            textViewAssessmentTitle = itemView.findViewById(R.id.text_view_assessment_title);
            textViewAssessmentGoalDate = itemView.findViewById(R.id.text_view_assessment_goal_date);
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
        void onItemClick(AssessmentEntity assessmentEntity);
    }

    public void setOnItemClickListener(AssessmentAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
