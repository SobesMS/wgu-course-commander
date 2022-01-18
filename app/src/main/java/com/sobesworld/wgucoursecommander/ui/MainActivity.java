package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sobesworld.wgucoursecommander.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToTerms(View view) {
        Intent intent= new Intent(MainActivity.this, TermList.class);
        startActivity(intent);
    }

    public void goToCourses(View view) {
        Intent intent= new Intent(MainActivity.this, CourseList.class);
        startActivity(intent);
    }

    public void goToAssessments(View view) {
        Intent intent= new Intent(MainActivity.this, AssessmentList.class);
        startActivity(intent);
    }
}