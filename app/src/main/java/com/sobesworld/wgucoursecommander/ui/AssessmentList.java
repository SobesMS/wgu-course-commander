package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.viewmodel.AssessmentViewModel;

public class AssessmentList extends AppCompatActivity {

    private AssessmentViewModel mAssessmentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);

        RecyclerView recyclerView = findViewById(R.id.assessments_recyclerview);
        final AssessmentListAdapter adapter = new AssessmentListAdapter(new AssessmentListAdapter.AssessmentDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAssessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);
        mAssessmentViewModel.getAllAssessments().observe(this, assessments -> {
            adapter.submitList(assessments);
        });
    }
}