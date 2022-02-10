package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.Repository;
import com.sobesworld.wgucoursecommander.database.adapters.AssessmentAdapter;

public class AssessmentList extends AppCompatActivity {

    private AssessmentAdapter adapter;
    private Repository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);
        repo = new Repository(getApplication());
        adapter = new AssessmentAdapter(this);
        fillRecyclerView();

        FloatingActionButton fab = findViewById(R.id.assessments_fab_add);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(AssessmentList.this, AssessmentDetail.class);
            intent.putExtra("is new record", true);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fillRecyclerView();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_refresh_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh_menu) {
            fillRecyclerView();
            Toast toast = Toast.makeText(getApplicationContext(), "Assessment list refreshed.", Toast.LENGTH_LONG);
            toast.show();
        }
        if (item.getItemId() == R.id.home_refresh_menu) {
            Intent homeButton = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(homeButton);
        }
        return super.onOptionsItemSelected(item);
    }

    private void fillRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.assessments_recyclerview);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setAssessments(repo.getAllAssessments());
    }
}