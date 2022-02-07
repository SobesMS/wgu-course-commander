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
import com.sobesworld.wgucoursecommander.database.adapters.CourseAdapter;

public class CourseList extends AppCompatActivity {

    private CourseAdapter adapter;
    private Repository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        repo = new Repository(getApplication());
        adapter = new CourseAdapter(this);
        fillRecyclerView();

        FloatingActionButton fab = findViewById(R.id.courses_fab_add);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(CourseList.this, CourseAddEdit.class);
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

    // TODO: add course refresh button method

    private void fillRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.courses_recyclerview);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setCourses(repo.getAllCourses());
    }
}