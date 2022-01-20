package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sobesworld.wgucoursecommander.R;

public class AddAssessment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);

        // adds back button to the Action Bar
        assert getSupportActionBar() != null; // null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // shows back button
    }

    // allows back button to return to previous activity
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}