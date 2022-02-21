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
            Intent intent = new Intent(CourseList.this, CourseDetail.class);
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
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh_menu) {
            fillRecyclerView();
            Toast.makeText(getApplicationContext(), "Course list refreshed.", Toast.LENGTH_LONG).show();
        }
        if (item.getItemId() == R.id.list_home_button) {
            Intent homeButton = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(homeButton);
        }
        return super.onOptionsItemSelected(item);
    }

    private void fillRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.courses_recyclerview);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setCourses(repo.getAllCourses());
    }
}