package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sobesworld.wgucoursecommander.R;

public class CourseDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
    }

    public void goToAddAssessment(View view) {
        Intent intent=new Intent(CourseDetail.this, AddAssessment.class);
        startActivity(intent);
    }

    public void editCourse(View view) {
        // TODO: write edit course function
    }
}