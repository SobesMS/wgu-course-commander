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
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.CourseViewModel;
import com.sobesworld.wgucoursecommander.database.adapters.CourseAdapter;
import com.sobesworld.wgucoursecommander.database.entity.CourseEntity;

import java.util.List;

public class CourseList extends AppCompatActivity {
    public static final String TAG = "CourseList";

    private CourseViewModel courseViewModel;
    private ActivityResultLauncher<Intent> activityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        FloatingActionButton addCourseFab = findViewById(R.id.add_course_fab);
        addCourseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseList.this, CourseDetail.class);
                intent.putExtra(MainActivity.EXTRA_REQUEST_ID, MainActivity.REQUEST_ADD_FROM_LIST);
                activityLauncher.launch(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.courses_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        CourseAdapter adapter = new CourseAdapter();
        recyclerView.setAdapter(adapter);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        courseViewModel.getAllCourses().observe(this, new Observer<List<CourseEntity>>() {
            @Override
            public void onChanged(List<CourseEntity> courseEntities) {
                adapter.submitList(courseEntities);
            }
        });

        adapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CourseEntity courseEntity) {
                Intent intent = new Intent(CourseList.this, CourseDetail.class);
                intent.putExtra(MainActivity.EXTRA_REQUEST_ID, MainActivity.REQUEST_EDIT);
                intent.putExtra(CourseDetail.EXTRA_COURSE_ID, courseEntity.getCourseID());
                intent.putExtra(CourseDetail.EXTRA_COURSE_TITLE, courseEntity.getCourseTitle());
                intent.putExtra(CourseDetail.EXTRA_COURSE_START_DATE, courseEntity.getCourseStartDate());
                intent.putExtra(CourseDetail.EXTRA_COURSE_END_DATE, courseEntity.getCourseEndDate());
                intent.putExtra(CourseDetail.EXTRA_COURSE_END_ALERT, courseEntity.isCourseEndAlert());
                intent.putExtra(CourseDetail.EXTRA_COURSE_ALERT_ID, courseEntity.getCourseAlertID());
                intent.putExtra(CourseDetail.EXTRA_COURSE_STATUS, courseEntity.getCourseStatus());
                intent.putExtra(CourseDetail.EXTRA_COURSE_MENTORS_NAME, courseEntity.getCourseMentorsName());
                intent.putExtra(CourseDetail.EXTRA_COURSE_MENTORS_PHONE, courseEntity.getCourseMentorsPhone());
                intent.putExtra(CourseDetail.EXTRA_COURSE_MENTORS_EMAIL, courseEntity.getCourseMentorsEmail());
                intent.putExtra(CourseDetail.EXTRA_COURSE_NOTES, courseEntity.getCourseNotes());
                intent.putExtra(CourseDetail.EXTRA_COURSE_LINKED_TERM_ID, courseEntity.getCourseLinkedTermID());
                activityLauncher.launch(intent);
            }
        });

        activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent intent = result.getData();
                        int resultCode = result.getResultCode();
                        Log.d(TAG, "onActivityResult: " + resultCode);

                        if (intent != null) {
                            int courseID = intent.getIntExtra(CourseDetail.EXTRA_COURSE_ID, -1);
                            String courseTitle = intent.getStringExtra(CourseDetail.EXTRA_COURSE_TITLE);
                            String courseStartDate = intent.getStringExtra(CourseDetail.EXTRA_COURSE_START_DATE);
                            String courseEndDate = intent.getStringExtra(CourseDetail.EXTRA_COURSE_END_DATE);
                            boolean courseEndAlert = intent.getBooleanExtra(CourseDetail.EXTRA_COURSE_END_ALERT, false);
                            int courseAlertID = intent.getIntExtra(CourseDetail.EXTRA_COURSE_ALERT_ID, -1);
                            String courseStatus = intent.getStringExtra(CourseDetail.EXTRA_COURSE_STATUS);
                            String courseMentorsName = intent.getStringExtra(CourseDetail.EXTRA_COURSE_MENTORS_NAME);
                            String courseMentorsPhone = intent.getStringExtra(CourseDetail.EXTRA_COURSE_MENTORS_PHONE);
                            String courseMentorsEmail = intent.getStringExtra(CourseDetail.EXTRA_COURSE_MENTORS_EMAIL);
                            String courseNotes = intent.getStringExtra(CourseDetail.EXTRA_COURSE_NOTES);
                            int courseLinkedTermID = intent.getIntExtra(CourseDetail.EXTRA_COURSE_LINKED_TERM_ID, -1);
                            if (resultCode == RESULT_OK) {
                                if (courseID == -1) {
                                    CourseEntity courseEntity = new CourseEntity(courseTitle, courseStartDate, courseEndDate,
                                            courseEndAlert, courseAlertID, courseStatus, courseMentorsName, courseMentorsPhone,
                                            courseMentorsEmail, courseNotes, courseLinkedTermID);
                                    courseViewModel.insert(courseEntity);
                                    Toast.makeText(CourseList.this, "Course added.", Toast.LENGTH_SHORT).show();
                                } else {
                                    CourseEntity courseEntity = new CourseEntity(courseTitle, courseStartDate, courseEndDate,
                                            courseEndAlert, courseAlertID, courseStatus, courseMentorsName, courseMentorsPhone,
                                            courseMentorsEmail, courseNotes, courseLinkedTermID);
                                    courseEntity.setCourseID(courseID);
                                    courseViewModel.update(courseEntity);
                                    Toast.makeText(CourseList.this, "Course updated.", Toast.LENGTH_SHORT).show();
                                }
                            } else if (resultCode == MainActivity.RESULT_DELETE) {
                                if (courseID == -1) {
                                    Toast.makeText(CourseList.this, "Course does not exist.", Toast.LENGTH_SHORT).show();
                                } else {
                                    courseViewModel.deleteUsingCourseID(courseID);
                                    Toast.makeText(getApplicationContext(), "Course permanently deleted.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
        );
    }
}