package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.viewmodel.CourseViewModel;

public class CourseList extends AppCompatActivity {

    private CourseViewModel mCourseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        RecyclerView recyclerView = findViewById(R.id.courses_recyclerview);
        final CourseListAdapter adapter = new CourseListAdapter(new CourseListAdapter.CourseDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mCourseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        mCourseViewModel.getAllCourses().observe(this, courses -> {
            adapter.submitList(courses);
        });
    }
}