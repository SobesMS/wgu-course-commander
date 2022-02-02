package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.sobesworld.wgucoursecommander.R;
// import com.sobesworld.wgucoursecommander.database.Repository;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToTermList(View view) {
        /*Intent intent= new Intent(MainActivity.this, TermList.class);
        startActivity(intent);
        Repository repo = new Repository(getApplication());

        // test term data
        Term term1 = new Term(1,"Term 1", "01/01/2022","06/30/2022");
        repo.insert(term1);
        Term term2 = new Term(2,"Term 2", "01/01/2022","06/30/2022");
        repo.insert(term2);
        Term term3 = new Term(3,"Term 3", "01/01/2022","06/30/2022");
        repo.insert(term3);*/
    }

    public void goToCourseList(View view) {
        /*Intent intent= new Intent(MainActivity.this, CourseList.class);
        startActivity(intent);
        Repository repo = new Repository(getApplication());

        // test course data
        Course course1 = new Course(1,"C123", "01/01/2022",false,"06/30/2022",
                false,"in progress","John Doe","216-555-5555",
                "john.doe@wgu.edu","These are course notes",1);
        repo.insert(course1);
        Course course2 = new Course(2,"C456", "01/01/2022",false,"06/30/2022",
                false,"in progress","John Doe","216-555-5555",
                "john.doe@wgu.edu","These are course notes",2);
        repo.insert(course2);
        Course course3 = new Course(3,"C789", "01/01/2022",false,"06/30/2022",
                false,"in progress","John Doe","216-555-5555",
                "john.doe@wgu.edu","These are course notes",3);
        repo.insert(course3);*/
    }

    public void goToAssessmentList(View view) {
        /*Intent intent= new Intent(MainActivity.this, AssessmentList.class);
        startActivity(intent);
        Repository repo = new Repository(getApplication());

        // test assessment data
        Assessment assessment1 = new Assessment(1,"Bake a cake","Objective","06/30/2022",
                true,1);
        repo.insert(assessment1);
        Assessment assessment2 = new Assessment(2,"Build a model","Objective","06/30/2022",
                true,2);
        repo.insert(assessment2);
        Assessment assessment3 = new Assessment(3,"Braise short ribs","Objective","06/30/2022",
                true,3);
        repo.insert(assessment3);*/
    }
}