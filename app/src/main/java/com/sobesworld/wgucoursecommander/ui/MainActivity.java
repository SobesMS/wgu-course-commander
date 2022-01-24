package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.Repository;
import com.sobesworld.wgucoursecommander.entities.Assessment;
import com.sobesworld.wgucoursecommander.entities.Course;
import com.sobesworld.wgucoursecommander.entities.Term;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // test data
        /* Repository repo = new Repository(getApplication());
        Term term = new Term(1,"Term 1", "01/01/2022","06/30/2022");
        Course course = new Course(1,"C123", "01/01/2022",false,"06/30/2022",
                false,"in progress","John Doe","216-555-5555",
                "john.doe@wgu.edu","These are course notes",1);
        Assessment assessment = new Assessment(1,"Bake a cake","Objective","06/30/2022",
                true,1);
        repo.insert(term);
        repo.insert(course);
        repo.insert(assessment); */
    }

    public void goToTermList(View view) {
        Intent intent= new Intent(MainActivity.this, TermList.class);
        startActivity(intent);

        // test data
        Repository repo = new Repository(getApplication());
        Term term = new Term(1,"Term 1", "01/01/2022","06/30/2022");
        repo.insert(term);
    }

    public void goToCourseList(View view) {
        Intent intent= new Intent(MainActivity.this, CourseList.class);
        startActivity(intent);

        // test data
        Repository repo = new Repository(getApplication());
        Course course = new Course(1,"C123", "01/01/2022",false,"06/30/2022",
                false,"in progress","John Doe","216-555-5555",
                "john.doe@wgu.edu","These are course notes",1);
        repo.insert(course);
    }

    public void goToAssessmentList(View view) {
        Intent intent= new Intent(MainActivity.this, AssessmentList.class);
        startActivity(intent);

        // test data
        Repository repo = new Repository(getApplication());
        Assessment assessment = new Assessment(1,"Bake a cake","Objective","06/30/2022",
                true,1);
        repo.insert(assessment);
    }
}