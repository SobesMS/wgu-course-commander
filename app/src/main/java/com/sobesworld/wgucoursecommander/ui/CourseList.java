package com.sobesworld.wgucoursecommander.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sobesworld.wgucoursecommander.MainActivity;
import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.AssessmentViewModel;
import com.sobesworld.wgucoursecommander.database.CourseViewModel;
import com.sobesworld.wgucoursecommander.database.adapters.CourseAdapter;
import com.sobesworld.wgucoursecommander.database.entity.CourseEntity;

public class CourseList extends AppCompatActivity {
    private CourseViewModel courseViewModel;
    private AssessmentViewModel assessmentViewModel;
    private ActivityResultLauncher<Intent> activityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        FloatingActionButton addCourseFab = findViewById(R.id.add_course_fab);
        addCourseFab.setOnClickListener(view -> {
            Intent intent = new Intent(CourseList.this, CourseDetail.class);
            intent.putExtra(NavMenu.EXTRA_REQUEST_ID, NavMenu.REQUEST_ADD);
            activityLauncher.launch(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.courses_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        CourseAdapter adapter = new CourseAdapter();
        recyclerView.setAdapter(adapter);

        assessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        courseViewModel.getAllCourses().observe(this, adapter::submitList);

        adapter.setOnItemClickListener(courseEntity -> {
            Intent intent = new Intent(CourseList.this, CourseDetail.class);
            intent.putExtra(NavMenu.EXTRA_REQUEST_ID, NavMenu.REQUEST_EDIT);
            intent.putExtra(CourseDetail.EXTRA_COURSE_ID, courseEntity.getCourseID());
            intent.putExtra(CourseDetail.EXTRA_COURSE_TITLE, courseEntity.getCourseTitle());
            intent.putExtra(CourseDetail.EXTRA_COURSE_START_DATE, courseEntity.getCourseStartDate());
            intent.putExtra(CourseDetail.EXTRA_COURSE_START_ALERT, courseEntity.isCourseStartAlert());
            intent.putExtra(CourseDetail.EXTRA_COURSE_START_ALERT_ID, courseEntity.getCourseStartAlertID());
            intent.putExtra(CourseDetail.EXTRA_COURSE_END_DATE, courseEntity.getCourseEndDate());
            intent.putExtra(CourseDetail.EXTRA_COURSE_END_ALERT, courseEntity.isCourseEndAlert());
            intent.putExtra(CourseDetail.EXTRA_COURSE_END_ALERT_ID, courseEntity.getCourseEndAlertID());
            intent.putExtra(CourseDetail.EXTRA_COURSE_STATUS, courseEntity.getCourseStatus());
            intent.putExtra(CourseDetail.EXTRA_COURSE_MENTORS_NAME, courseEntity.getCourseMentorsName());
            intent.putExtra(CourseDetail.EXTRA_COURSE_MENTORS_PHONE, courseEntity.getCourseMentorsPhone());
            intent.putExtra(CourseDetail.EXTRA_COURSE_MENTORS_EMAIL, courseEntity.getCourseMentorsEmail());
            intent.putExtra(CourseDetail.EXTRA_COURSE_NOTES, courseEntity.getCourseNotes());
            intent.putExtra(CourseDetail.EXTRA_COURSE_LINKED_TERM_ID, courseEntity.getCourseLinkedTermID());
            activityLauncher.launch(intent);
        });

        activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent intent = result.getData();
                    int resultCode = result.getResultCode();
                    if (intent != null) {
                        if (resultCode == RESULT_OK) {
                            int courseID = intent.getIntExtra(CourseDetail.EXTRA_COURSE_ID, -1);
                            String courseTitle = intent.getStringExtra(CourseDetail.EXTRA_COURSE_TITLE);
                            String courseStartDate = intent.getStringExtra(CourseDetail.EXTRA_COURSE_START_DATE);
                            boolean courseStartAlert = intent.getBooleanExtra(CourseDetail.EXTRA_COURSE_START_ALERT, false);
                            int courseStartAlertID = intent.getIntExtra(CourseDetail.EXTRA_COURSE_START_ALERT_ID, -1);
                            String courseEndDate = intent.getStringExtra(CourseDetail.EXTRA_COURSE_END_DATE);
                            boolean courseEndAlert = intent.getBooleanExtra(CourseDetail.EXTRA_COURSE_END_ALERT, false);
                            int courseEndAlertID = intent.getIntExtra(CourseDetail.EXTRA_COURSE_END_ALERT_ID, -1);
                            String courseStatus = intent.getStringExtra(CourseDetail.EXTRA_COURSE_STATUS);
                            String courseMentorsName = intent.getStringExtra(CourseDetail.EXTRA_COURSE_MENTORS_NAME);
                            String courseMentorsPhone = intent.getStringExtra(CourseDetail.EXTRA_COURSE_MENTORS_PHONE);
                            String courseMentorsEmail = intent.getStringExtra(CourseDetail.EXTRA_COURSE_MENTORS_EMAIL);
                            String courseNotes = intent.getStringExtra(CourseDetail.EXTRA_COURSE_NOTES);
                            int courseLinkedTermID = intent.getIntExtra(CourseDetail.EXTRA_COURSE_LINKED_TERM_ID, -1);
                            CourseEntity courseEntity = new CourseEntity(courseTitle, courseStartDate, courseStartAlert, courseStartAlertID,
                                    courseEndDate, courseEndAlert, courseEndAlertID, courseStatus, courseMentorsName, courseMentorsPhone,
                                    courseMentorsEmail, courseNotes, courseLinkedTermID);
                            if (courseID == -1) {
                                courseViewModel.insert(courseEntity);
                                Toast.makeText(CourseList.this, "Course added.", Toast.LENGTH_SHORT).show();
                            } else {
                                courseEntity.setCourseID(courseID);
                                courseViewModel.update(courseEntity);
                                Toast.makeText(CourseList.this, "Course updated.", Toast.LENGTH_SHORT).show();
                            }
                        } else if (resultCode == NavMenu.RESULT_DELETE) {
                            int courseID = intent.getIntExtra(CourseDetail.EXTRA_COURSE_ID, -1);
                            if (courseID == -1) {
                                Toast.makeText(CourseList.this, "Course does not exist.", Toast.LENGTH_SHORT).show();
                            } else {
                                courseViewModel.deleteUsingCourseID(courseID);
                                assessmentViewModel.deleteLinkedAssessments(courseID);
                                Toast.makeText(getApplicationContext(), "Course permanently deleted.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            Intent intent = new Intent(CourseList.this, MainActivity.class);
            startActivity(intent);
        }
    }
}