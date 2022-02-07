package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

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
            Intent intent = new Intent(AssessmentList.this, AssessmentAddEdit.class);
            intent.putExtra("is new record", true);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        return true;
    }

    // TODO: add assessment refresh button method

    private void fillRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.assessments_recyclerview);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setAssessments(repo.getAllAssessments());
    }
}