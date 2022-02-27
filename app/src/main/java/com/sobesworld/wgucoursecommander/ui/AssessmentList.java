package com.sobesworld.wgucoursecommander.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.AssessmentViewModel;
import com.sobesworld.wgucoursecommander.database.adapters.AssessmentAdapter;
import com.sobesworld.wgucoursecommander.database.entity.AssessmentEntity;

import java.util.List;

public class AssessmentList extends AppCompatActivity {
    public static final String TAG = "AssessmentList";

    private AssessmentViewModel assessmentViewModel;
    private ActivityResultLauncher<Intent> activityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);

        FloatingActionButton addAssessmentFab = findViewById(R.id.add_assessments_fab);
        addAssessmentFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AssessmentList.this, AssessmentDetail.class);
                intent.putExtra(MainActivity.EXTRA_REQUEST_ID, MainActivity.REQUEST_ADD);
                activityLauncher.launch(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.assessments_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AssessmentAdapter adapter = new AssessmentAdapter();
        recyclerView.setAdapter(adapter);

        assessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);
        assessmentViewModel.getAllAssessments().observe(this, new Observer<List<AssessmentEntity>>() {
            @Override
            public void onChanged(List<AssessmentEntity> assessmentEntities) {
                adapter.submitList(assessmentEntities);
            }
        });

        adapter.setOnItemClickListener(new AssessmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AssessmentEntity assessmentEntity) {
                Intent intent = new Intent(AssessmentList.this, AssessmentDetail.class);
                intent.putExtra(MainActivity.EXTRA_REQUEST_ID, MainActivity.REQUEST_EDIT);
                intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_ID, assessmentEntity.getAssessmentID());
                intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_TITLE, assessmentEntity.getAssessmentTitle());
                intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_TYPE, assessmentEntity.getAssessmentType());
                intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_GOAL_DATE, assessmentEntity.getAssessmentGoalDate());
                intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_GOAL_ALERT, assessmentEntity.isAssessmentGoalAlert());
                intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_ALERT_ID, assessmentEntity.getAssessmentAlertID());
                intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_NOTES, assessmentEntity.getAssessmentNotes());
                intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_LINKED_COURSE_ID, assessmentEntity.getAssessmentLinkedCourseID());
                activityLauncher.launch(intent);
            }
        });

        activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent intent = result.getData();
                        int resultCode = result.getResultCode();
                        if (intent != null) {
                            int assessmentID = intent.getIntExtra(AssessmentDetail.EXTRA_ASSESSMENT_ID, -1);
                            String assessmentTitle = intent.getStringExtra(AssessmentDetail.EXTRA_ASSESSMENT_TITLE);
                            String assessmentType = intent.getStringExtra(AssessmentDetail.EXTRA_ASSESSMENT_TYPE);
                            String assessmentGoalDate = intent.getStringExtra(AssessmentDetail.EXTRA_ASSESSMENT_GOAL_DATE);
                            boolean assessmentGoalAlert = intent.getBooleanExtra(AssessmentDetail.EXTRA_ASSESSMENT_GOAL_ALERT, false);
                            int assessmentAlertID = intent.getIntExtra(AssessmentDetail.EXTRA_ASSESSMENT_ALERT_ID, -1);
                            String assessmentNotes = intent.getStringExtra(AssessmentDetail.EXTRA_ASSESSMENT_NOTES);
                            int assessmentLinkedCourseID = intent.getIntExtra(AssessmentDetail.EXTRA_ASSESSMENT_LINKED_COURSE_ID, -1);
                            if (resultCode == RESULT_OK) {
                                AssessmentEntity assessmentEntity = new AssessmentEntity(assessmentTitle, assessmentType, assessmentGoalDate,
                                        assessmentGoalAlert, assessmentAlertID, assessmentNotes, assessmentLinkedCourseID);
                                if (assessmentID == -1) {
                                    assessmentViewModel.insert(assessmentEntity);
                                    Toast.makeText(AssessmentList.this, "Assessment added.", Toast.LENGTH_SHORT).show();
                                } else {
                                    assessmentEntity.setAssessmentID(assessmentID);
                                    assessmentViewModel.update(assessmentEntity);
                                    Toast.makeText(AssessmentList.this, "Assessment updated.", Toast.LENGTH_SHORT).show();
                                }
                            } else if (resultCode == MainActivity.RESULT_DELETE) {
                                if (assessmentID == -1) {
                                    Toast.makeText(AssessmentList.this, "Assessment does not exist.", Toast.LENGTH_SHORT).show();
                                } else {
                                    assessmentViewModel.deleteUsingAssessmentID(assessmentID);
                                    Toast.makeText(getApplicationContext(), "Assessment permanently deleted.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
        );
    }
}