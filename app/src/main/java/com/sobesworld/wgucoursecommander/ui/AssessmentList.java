package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.AssessmentListAdapter;
import com.sobesworld.wgucoursecommander.database.AssessmentViewModel;

public class AssessmentList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);

        RecyclerView recyclerView = findViewById(R.id.assessments_recyclerview);
        final AssessmentListAdapter adapter = new AssessmentListAdapter(new AssessmentListAdapter.AssessmentDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(AssessmentList.this));

        AssessmentViewModel mAssessmentViewModel = new ViewModelProvider(AssessmentList.this).get(AssessmentViewModel.class);
        mAssessmentViewModel.getAllAssessments().observe(AssessmentList.this, adapter::submitList);

        FloatingActionButton fab = findViewById(R.id.assessments_fab_add);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(AssessmentList.this, AssessmentDetail.class);
            intent.putExtra(getString(R.string.is_new_record), true);
            startActivity(intent);
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home_menu) {
            Intent homeButton = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(homeButton);
        }
        return super.onOptionsItemSelected(item);
    }
}