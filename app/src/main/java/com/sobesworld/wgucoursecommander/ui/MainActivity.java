package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.Repository;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Repository repo = new Repository(getApplication());

        // uncomment method on next line to pre-populate an empty database
        //repo.generateData();
    }

    public void goToTermList(View view) {
        Intent intent= new Intent(MainActivity.this, TermList.class);
        startActivity(intent);
    }

    public void goToCourseList(View view) {
        Intent intent= new Intent(MainActivity.this, CourseList.class);
        startActivity(intent);
    }

    public void goToAssessmentList(View view) {
        Intent intent= new Intent(MainActivity.this, AssessmentList.class);
        startActivity(intent);
    }
}