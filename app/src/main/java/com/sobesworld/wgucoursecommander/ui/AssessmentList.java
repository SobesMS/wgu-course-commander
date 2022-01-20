package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sobesworld.wgucoursecommander.R;

public class AssessmentList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);
    }

    public void goToAddAssessment(View view) {
        Intent intent=new Intent(AssessmentList.this, AddAssessment.class);
        startActivity(intent);
    }

    // TODO: DELETE THIS CODE (Test Assessment Detail)
    public void testAssessmentDetail(View view) {
        Intent intent=new Intent(AssessmentList.this, AssessmentDetail.class);
        startActivity(intent);
    }
}