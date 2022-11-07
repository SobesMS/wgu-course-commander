package com.sobesworld.wgucoursecommander;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sobesworld.wgucoursecommander.database.AssessmentViewModel;
import com.sobesworld.wgucoursecommander.database.CourseViewModel;
import com.sobesworld.wgucoursecommander.database.TermViewModel;
import com.sobesworld.wgucoursecommander.database.adapters.AssessmentAdapter;
import com.sobesworld.wgucoursecommander.database.adapters.CourseAdapter;
import com.sobesworld.wgucoursecommander.database.adapters.TermAdapter;
import com.sobesworld.wgucoursecommander.ui.AssessmentDetail;
import com.sobesworld.wgucoursecommander.ui.CourseDetail;
import com.sobesworld.wgucoursecommander.ui.NavMenu;
import com.sobesworld.wgucoursecommander.ui.TermDetail;

import java.util.Objects;

public class SearchActivity extends AppCompatActivity {
    private String userID;
    private EditText editTextSearch;
    private TextView textViewResultsHead;
    private TermViewModel termViewModel;
    private TermAdapter termAdapter;
    private CourseViewModel courseViewModel;
    private CourseAdapter courseAdapter;
    private AssessmentViewModel assessmentViewModel;
    private AssessmentAdapter assessmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editTextSearch = findViewById(R.id.edit_text_search);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewResultsHead = findViewById(R.id.results_head);

        RecyclerView termRecyclerView = findViewById(R.id.term_search_results);
        termRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        termAdapter = new TermAdapter();
        termRecyclerView.setAdapter(termAdapter);
        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        RecyclerView courseRecyclerView = findViewById(R.id.course_search_results);
        courseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter = new CourseAdapter();
        courseRecyclerView.setAdapter(courseAdapter);
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        RecyclerView assessmentRecyclerView = findViewById(R.id.assessment_search_results);
        assessmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        assessmentAdapter = new AssessmentAdapter();
        assessmentRecyclerView.setAdapter(assessmentAdapter);
        assessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // confirms a user is currently logged and builds search UI
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            startActivity(new Intent(SearchActivity.this, MainActivity.class));
        } else {
            userID = user.getUid();

            Button searchBtn = findViewById(R.id.search_btn);
            searchBtn.setOnClickListener(view -> {
                String searchText = editTextSearch.getText().toString().trim();
                String searchQuery = "%" + searchText + "%";
                search(searchQuery, userID);
            });
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void search(String searchQuery, String userID) {
        textViewResultsHead.setVisibility(View.VISIBLE);
        termViewModel.termSearch(searchQuery, userID).observe(this, termAdapter::submitList);
        courseViewModel.courseSearch(searchQuery, userID).observe(this, courseAdapter::submitList);
        assessmentViewModel.assessmentSearch(searchQuery, userID).observe(this, assessmentAdapter::submitList);

        termAdapter.setOnItemClickListener(termEntity -> {
            Intent intent = new Intent(SearchActivity.this, TermDetail.class);
            intent.putExtra(NavMenu.EXTRA_REQUEST_ID, NavMenu.REQUEST_EDIT);
            intent.putExtra(TermDetail.EXTRA_TERM_ID, termEntity.getTermID());
            intent.putExtra(TermDetail.EXTRA_TERM_TITLE, termEntity.getTermTitle());
            intent.putExtra(TermDetail.EXTRA_TERM_START_DATE, termEntity.getTermStartDate());
            intent.putExtra(TermDetail.EXTRA_TERM_END_DATE, termEntity.getTermEndDate());
            intent.putExtra(TermDetail.EXTRA_TERM_USER_ID, termEntity.getTermUserID());
            startActivity(intent);
        });

        courseAdapter.setOnItemClickListener(courseEntity -> {
            Intent intent = new Intent(SearchActivity.this, CourseDetail.class);
            intent.putExtra(NavMenu.EXTRA_REQUEST_ID, NavMenu.REQUEST_EDIT);
            intent.putExtra(CourseDetail.EXTRA_COURSE_ID, courseEntity.getCourseID());
            intent.putExtra(CourseDetail.EXTRA_COURSE_TITLE, courseEntity.getCourseTitle());
            intent.putExtra(CourseDetail.EXTRA_COURSE_START_DATE, courseEntity.getCourseStartDate());
            intent.putExtra(CourseDetail.EXTRA_COURSE_START_ALERT, courseEntity.isCourseStartAlert());
            intent.putExtra(CourseDetail.EXTRA_COURSE_START_ALERT_ID, courseEntity.getCourseStartAlertID());
            intent.putExtra(CourseDetail.EXTRA_COURSE_END_DATE, courseEntity.getCourseEndDate());
            intent.putExtra(CourseDetail.EXTRA_COURSE_END_ALERT, courseEntity.isCourseEndAlert());
            intent.putExtra(CourseDetail.EXTRA_COURSE_END_ALERT_ID, courseEntity.getCourseEndAlertID());
            intent.putExtra(CourseDetail.EXTRA_COURSE_STATUS, courseEntity.getCourseStatus());
            intent.putExtra(CourseDetail.EXTRA_COURSE_MENTORS_NAME, courseEntity.getCourseMentorsName());
            intent.putExtra(CourseDetail.EXTRA_COURSE_MENTORS_PHONE, courseEntity.getCourseMentorsPhone());
            intent.putExtra(CourseDetail.EXTRA_COURSE_MENTORS_EMAIL, courseEntity.getCourseMentorsEmail());
            intent.putExtra(CourseDetail.EXTRA_COURSE_NOTES, courseEntity.getCourseNotes());
            intent.putExtra(CourseDetail.EXTRA_COURSE_LINKED_TERM_ID, courseEntity.getCourseLinkedTermID());
            intent.putExtra(CourseDetail.EXTRA_COURSE_USER_ID, courseEntity.getCourseUserID());
            startActivity(intent);
        });

        assessmentAdapter.setOnItemClickListener(assessmentEntity -> {
            Intent intent = new Intent(SearchActivity.this, AssessmentDetail.class);
            intent.putExtra(NavMenu.EXTRA_REQUEST_ID, NavMenu.REQUEST_EDIT);
            intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_ID, assessmentEntity.getAssessmentID());
            intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_TITLE, assessmentEntity.getAssessmentTitle());
            intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_TYPE, assessmentEntity.getAssessmentType());
            intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_GOAL_DATE, assessmentEntity.getAssessmentGoalDate());
            intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_GOAL_ALERT, assessmentEntity.isAssessmentGoalAlert());
            intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_ALERT_ID, assessmentEntity.getAssessmentAlertID());
            intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_NOTES, assessmentEntity.getAssessmentNotes());
            intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_LINKED_COURSE_ID, assessmentEntity.getAssessmentLinkedCourseID());
            intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_USER_ID, assessmentEntity.getAssessmentUserID());
            startActivity(intent);
        });
    }
}