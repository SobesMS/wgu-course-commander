package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.CourseViewModel;
import com.sobesworld.wgucoursecommander.database.CourseListAdapter;

public class CourseList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        RecyclerView recyclerView = findViewById(R.id.courses_recyclerview);
        final CourseListAdapter adapter = new CourseListAdapter(new CourseListAdapter.CourseDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(CourseList.this));

        CourseViewModel mCourseViewModel = new ViewModelProvider(CourseList.this).get(CourseViewModel.class);
        mCourseViewModel.getAllCourses().observe(CourseList.this, adapter::submitList);

        FloatingActionButton fab = findViewById(R.id.courses_fab_add);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(CourseList.this, CourseDetail.class);
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
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}