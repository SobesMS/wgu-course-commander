package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.Repository;
import com.sobesworld.wgucoursecommander.database.adapters.AssessmentAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CourseDetail extends AppCompatActivity {

    private Repository repo;
    private AlertDialog deleteDialog;
    private boolean recordStatusNew;
    EditText courseTitle;
    EditText courseStartDate;
    EditText courseEndDate;
    Boolean courseEndAlert;
    int courseAlertID;
    String courseStatus;
    EditText courseMentorsName;
    EditText courseMentorsPhone;
    EditText courseMentorsEmail;
    String courseNotes;
    int termID;
    final Calendar startCalendar = Calendar.getInstance();
    final Calendar endCalendar = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        recordStatusNew = getIntent().getBooleanExtra(getString(R.string.is_new_record), true);
        repo = new Repository(getApplication());
        AssessmentAdapter adapter = new AssessmentAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.courseAssessmentList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // displays course list for current term
        if (!recordStatusNew) {
            int i = getIntent().getIntExtra(getResources().getString(R.string.idnum), -1);
            adapter.setAssessments(repo.getLinkedAssessments(i));
        }

        
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home_detail_menu) {
            Intent homeButton = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(homeButton);
        }
        if (item.getItemId() == R.id.note_detail_menu) {
            Toast.makeText(getApplicationContext(), "Hi, you just pressed note!", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}