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
import com.sobesworld.wgucoursecommander.database.CourseViewModel;
import com.sobesworld.wgucoursecommander.database.adapters.CourseAdapter;

public class CourseList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        FloatingActionButton addCourseFab = findViewById(R.id.add_course_fab);
        addCourseFab.setOnClickListener(view -> {
            Intent intent = new Intent(CourseList.this, CourseDetail.class);
            intent.putExtra(NavMenu.EXTRA_REQUEST_ID, NavMenu.REQUEST_ADD);
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.courses_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        CourseAdapter adapter = new CourseAdapter();
        recyclerView.setAdapter(adapter);

        CourseViewModel courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
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
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // confirms a user is currently logged in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            startActivity(new Intent(CourseList.this, MainActivity.class));
        }
    }
}