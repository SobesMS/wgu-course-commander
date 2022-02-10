package com.sobesworld.wgucoursecommander.database.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.entity.AssessmentEntity;
import com.sobesworld.wgucoursecommander.ui.AssessmentDetail;

import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {

    private List<AssessmentEntity> mAssessments;
    private final Context context;

    public AssessmentAdapter(Context context) {
        this.context = context;
    }

    class AssessmentViewHolder extends RecyclerView.ViewHolder {

        private final TextView assessmentItemView;

        private AssessmentViewHolder(View itemView) {
            super(itemView);
            assessmentItemView = itemView.findViewById(R.id.item_view);
            itemView.setOnClickListener((view -> {
                int position = getAdapterPosition();
                final AssessmentEntity current = mAssessments.get(position);
                Intent intent = new Intent(context, AssessmentDetail.class);
                intent.putExtra(context.getString(R.string.is_new_record), false);
                intent.putExtra("id", current.getAssessmentID());
                context.startActivity(intent);
            }));
        }
    }

    @NonNull
    @Override
    public AssessmentAdapter.AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new AssessmentAdapter.AssessmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapter.AssessmentViewHolder holder, int position) {
        if (mAssessments != null) {
            AssessmentEntity current = mAssessments.get(position);
            String title = current.getAssessmentTitle();
            holder.assessmentItemView.setText(title);
        } else {
            holder.assessmentItemView.setText(R.string.no_assessment_title);
        }
    }

    @Override
    public int getItemCount() {
        if (mAssessments != null) {
            return mAssessments.size();
        } else {
            return 0;
        }
    }

    public void setAssessments(List<AssessmentEntity> assessments) {
        mAssessments = assessments;
        notifyDataSetChanged();
    }
}
