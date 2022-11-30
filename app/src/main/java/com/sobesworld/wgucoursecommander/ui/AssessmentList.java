package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sobesworld.wgucoursecommander.MainActivity;
import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.AssessmentViewModel;
import com.sobesworld.wgucoursecommander.database.adapters.AssessmentAdapter;

public class AssessmentList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // confirms a user is currently logged in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            startActivity(new Intent(AssessmentList.this, MainActivity.class));
        } else {
            String userID = user.getUid();

            FloatingActionButton addAssessmentFab = findViewById(R.id.add_assessments_fab);
            addAssessmentFab.setOnClickListener(view -> {
                Intent intent = new Intent(AssessmentList.this, AssessmentDetail.class);
                intent.putExtra(NavMenu.EXTRA_REQUEST_ID, NavMenu.REQUEST_ADD);
                startActivity(intent);
            });

            RecyclerView recyclerView = findViewById(R.id.assessments_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            AssessmentAdapter adapter = new AssessmentAdapter();
            recyclerView.setAdapter(adapter);

            AssessmentViewModel assessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);
            assessmentViewModel.getAllAssessmentsByUserID(userID).observe(this, adapter::submitList);

            adapter.setOnItemClickListener(assessmentEntity -> {
                Intent intent = new Intent(AssessmentList.this, AssessmentDetail.class);
                intent.putExtra(NavMenu.EXTRA_REQUEST_ID, NavMenu.REQUEST_EDIT);
                intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_ID, assessmentEntity.getAssessmentID());
                intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_TITLE, assessmentEntity.getAssessmentTitle());
                intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_TYPE, assessmentEntity.getAssessmentType());
                intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_GOAL_DATE, assessmentEntity.getAssessmentGoalDate());
                intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_GOAL_ALERT, assessmentEntity.isAssessmentGoalAlert());
                intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_ALERT_ID, assessmentEntity.getAssessmentAlertID());
                intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_NOTES, assessmentEntity.getAssessmentNotes());
                intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_LINKED_COURSE_ID, assessmentEntity.getAssessmentLinkedCourseID());
                intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_USER_ID, assessmentEntity.getAssessmentUserID());
                startActivity(intent);
            });
        }
    }
}