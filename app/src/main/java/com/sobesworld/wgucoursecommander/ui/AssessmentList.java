package com.sobesworld.wgucoursecommander.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import com.sobesworld.wgucoursecommander.database.AssessmentViewModel;

public class AssessmentList extends AppCompatActivity {
    public static final String TAG = "AssessmentList";
    public static final String EXTRA_REQUEST_ID = "com.sobesworld.wgucoursecommander.EXTRA_REQUEST_ID";
    public static final int REQUEST_ADD_COURSE = 31;
    public static final int REQUEST_EDIT_COURSE = 32;

    private AssessmentViewModel assessmentViewModel;
    private ActivityResultLauncher<Intent> activityLauncher;

    /*private AssessmentAdapter adapter;
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
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh_menu) {
            fillRecyclerView();
            Toast toast = Toast.makeText(getApplicationContext(), "Assessment list refreshed.", Toast.LENGTH_LONG);
            toast.show();
        }
        if (item.getItemId() == R.id.list_home_button) {
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
    }*/
}