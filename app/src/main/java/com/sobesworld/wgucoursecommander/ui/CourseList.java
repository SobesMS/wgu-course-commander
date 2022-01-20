package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sobesworld.wgucoursecommander.R;

public class CourseList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
    }

    public void goToAddCourse(View view) {
        Intent intent=new Intent(CourseList.this, AddCourse.class);
        startActivity(intent);
    }

    // TODO: DELETE THIS CODE (Test Course Detail)
    public void testCourseDetail(View view) {
        Intent intent=new Intent(CourseList.this, CourseDetail.class);
        startActivity(intent);
    }
}