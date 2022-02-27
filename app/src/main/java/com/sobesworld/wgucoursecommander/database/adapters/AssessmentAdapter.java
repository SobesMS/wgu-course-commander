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
    private OnItemClickListener listener;

    public AssessmentAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<AssessmentEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<AssessmentEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull AssessmentEntity oldItem, @NonNull AssessmentEntity newItem) {
            return newItem.getAssessmentID() == oldItem.getAssessmentID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull AssessmentEntity oldItem, @NonNull AssessmentEntity newItem) {
            return newItem.getAssessmentTitle().equalsIgnoreCase(oldItem.getAssessmentTitle())
                    && newItem.getAssessmentType().equalsIgnoreCase(oldItem.getAssessmentType())
                    && newItem.getAssessmentGoalDate().equals(oldItem.getAssessmentGoalDate())
                    && newItem.isAssessmentGoalAlert() == oldItem.isAssessmentGoalAlert()
                    && newItem.getAssessmentAlertID() == oldItem.getAssessmentAlertID()
                    && newItem.getAssessmentNotes().equalsIgnoreCase(oldItem.getAssessmentNotes())
                    && newItem.getAssessmentLinkedCourseID() == oldItem.getAssessmentLinkedCourseID();
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
        holder.textViewAssessmentTitle.setText(current.getAssessmentTitle());
        String goal = "Completion Goal: " + current.getAssessmentGoalDate();
        holder.textViewAssessmentGoalDate.setText(goal);
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
