package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sobesworld.wgucoursecommander.R;

public class TermDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
    }

    public void goToAddCourse(View view) {
        Intent intent=new Intent(TermDetail.this, AddCourse.class);
        startActivity(intent);
    }

    public void editTerm(View view) {
        // TODO: write edit term function
    }
}