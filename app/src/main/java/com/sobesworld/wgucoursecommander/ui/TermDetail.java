package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sobesworld.wgucoursecommander.MainActivity;
import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.CourseViewModel;
import com.sobesworld.wgucoursecommander.database.TermViewModel;
import com.sobesworld.wgucoursecommander.database.adapters.CourseAdapter;
import com.sobesworld.wgucoursecommander.database.entity.TermEntity;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Objects;

public class TermDetail extends AppCompatActivity {
    public static final String EXTRA_TERM_ID = "com.sobesworld.wgucoursecommander.EXTRA_TERM_ID";
    public static final String EXTRA_TERM_TITLE = "com.sobesworld.wgucoursecommander.EXTRA_TERM_TITLE";
    public static final String EXTRA_TERM_START_DATE = "com.sobesworld.wgucoursecommander.EXTRA_TERM_START_DATE";
    public static final String EXTRA_TERM_END_DATE = "com.sobesworld.wgucoursecommander.EXTRA_TERM_END_DATE";

    private CourseAdapter courseAdapter;
    private DatePickerDialog.OnDateSetListener startDateSetListener, endDateSetListener;

    private int termID;
    private EditText editTextTermTitle;
    private TextView textViewTermStartDate, textViewTermEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

        // UI elements
        editTextTermTitle = findViewById(R.id.edit_text_term_title);
        textViewTermStartDate = findViewById(R.id.text_view_term_start_date);
        textViewTermEndDate = findViewById(R.id.text_view_term_end_date);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        courseAdapter = new CourseAdapter();

        // contains data passed from terms list
        Intent passedIntent = getIntent();

        // determines whether adding a new course or editing an existing and sets UI appropriately
        if (passedIntent.getIntExtra(NavMenu.EXTRA_REQUEST_ID, 1) == NavMenu.REQUEST_ADD) {
            setTitle("Add Term");
            termID = 0;
        } else {
            setTitle("Edit Term");
            termID = passedIntent.getIntExtra(EXTRA_TERM_ID, -1);
            if (termID == -1) {
                Toast.makeText(TermDetail.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                finish();
            } else {
                editTextTermTitle.setText(passedIntent.getStringExtra(EXTRA_TERM_TITLE));
                textViewTermStartDate.setText(passedIntent.getStringExtra(EXTRA_TERM_START_DATE));
                textViewTermEndDate.setText(passedIntent.getStringExtra(EXTRA_TERM_END_DATE));

                TextView courseListTitle = findViewById(R.id.course_list_label);
                courseListTitle.setVisibility(View.VISIBLE);

                // add course button
                ImageView imageViewTermAddCourse = findViewById(R.id.image_view_term_add_course);
                imageViewTermAddCourse.setVisibility(View.VISIBLE);
                imageViewTermAddCourse.setOnClickListener(view -> {
                    Intent intent = new Intent(TermDetail.this, CourseDetail.class);
                    intent.putExtra(NavMenu.EXTRA_REQUEST_ID, NavMenu.REQUEST_ADD);
                    intent.putExtra(CourseDetail.EXTRA_COURSE_LINKED_TERM_ID, termID);
                    startActivity(intent);
                });

                RecyclerView recyclerView = findViewById(R.id.term_course_list);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(courseAdapter);
                CourseViewModel courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
                courseViewModel.getLinkedCourses(termID).observe(this, courseEntities -> courseAdapter.submitList(courseEntities));

                courseAdapter.setOnItemClickListener(courseEntity -> {
                    Intent intent = new Intent(TermDetail.this, CourseDetail.class);
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
                    startActivity(intent);
                });
            }
        }

        // logic for date selectors
        textViewTermStartDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            String date = textViewTermStartDate.getText().toString();
            if (!date.trim().isEmpty()) {
                try {
                    calendar.setTime(Objects.requireNonNull(NavMenu.sdf.parse(date)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            new DatePickerDialog(TermDetail.this, startDateSetListener, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        startDateSetListener = (datePicker, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            textViewTermStartDate.setText(NavMenu.sdf.format(calendar.getTime()));
        };

        textViewTermEndDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            String date = textViewTermEndDate.getText().toString();
            if (!date.trim().isEmpty()) {
                try {
                    calendar.setTime(Objects.requireNonNull(NavMenu.sdf.parse(date)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            new DatePickerDialog(TermDetail.this, endDateSetListener, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        endDateSetListener = (datePicker, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            textViewTermEndDate.setText(NavMenu.sdf.format(calendar.getTime()));
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        // confirms a user is currently logged in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            startActivity(new Intent(TermDetail.this, MainActivity.class));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.term_detail_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            upButtonChangeValidation();
        }
        if (item.getItemId() == R.id.save_term_detail_menu) {
            saveTerm();
        }
        if (item.getItemId() == R.id.delete_term_detail_menu) {
            deleteTerm();
        }
        return super.onOptionsItemSelected(item);
    }

    // validates if changes were made when selecting the back/close button
    private void upButtonChangeValidation() {
        String termTitle = editTextTermTitle.getText().toString().trim();
        String termStartDate = textViewTermStartDate.getText().toString().trim();
        String termEndDate = textViewTermEndDate.getText().toString().trim();

        if (getIntent().getIntExtra(NavMenu.EXTRA_REQUEST_ID, 1) == NavMenu.REQUEST_ADD
                && termTitle.isEmpty() && termStartDate.isEmpty() && termEndDate.isEmpty()) {
            finish();
        } else if (termTitle.equals(getIntent().getStringExtra(EXTRA_TERM_TITLE))
                && termStartDate.equals(getIntent().getStringExtra(EXTRA_TERM_START_DATE))
                && termEndDate.equals(getIntent().getStringExtra(EXTRA_TERM_END_DATE))) {
            finish();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(TermDetail.this);
            dialog.setTitle("WARNING: UNSAVED CHANGES")
                    .setMessage("You have unsaved changes. Click SAVE to save and close. Click CANCEL to close without saving.")
                    .setCancelable(false)
                    .setNegativeButton("CANCEL", (dialogInterface, i) -> finish())
                    .setPositiveButton("SAVE", (dialogInterface, i) -> saveTerm())
                    .show();
        }
    }

    private void saveTerm() {
        String termTitle = editTextTermTitle.getText().toString().trim();
        String termStartDate = textViewTermStartDate.getText().toString().trim();
        String termEndDate = textViewTermEndDate.getText().toString().trim();
        TermViewModel termViewModel = new ViewModelProvider(TermDetail.this).get(TermViewModel.class);

        if (termTitle.isEmpty() || termStartDate.isEmpty() || termEndDate.isEmpty()) {
            if (termTitle.isEmpty()) {
                editTextTermTitle.setError("Title is required.");
                editTextTermTitle.setHintTextColor(ContextCompat.getColor(TermDetail.this, R.color.red));
            }
            if (termStartDate.isEmpty()) {
                textViewTermStartDate.setError("Start date is required.");
                textViewTermStartDate.setHintTextColor(ContextCompat.getColor(TermDetail.this, R.color.red));
            }
            if (termEndDate.isEmpty()) {
                textViewTermEndDate.setError("End date is required.");
                textViewTermEndDate.setHintTextColor(ContextCompat.getColor(TermDetail.this, R.color.red));
            }
        } else {
            TermEntity term = new TermEntity(termTitle, termStartDate, termEndDate);
            if (termID == 0) {
                termViewModel.insert(term);
                Toast.makeText(TermDetail.this, "New term created.", Toast.LENGTH_LONG).show();
                finish();
            } else {
                term.setTermID(termID);
                termViewModel.update(term);
                Toast.makeText(TermDetail.this, "Term updated.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void deleteTerm() {
        if (termID == 0) {
            Toast.makeText(TermDetail.this, "New term creation cancelled.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            if (courseAdapter.getItemCount() > 0) {
                Toast.makeText(TermDetail.this, "This term can't be deleted until linked courses are " +
                                "deleted or transferred to another term.", Toast.LENGTH_LONG).show();
            } else {
                TermViewModel termViewModel = new ViewModelProvider(TermDetail.this).get(TermViewModel.class);
                AlertDialog.Builder dialog = new AlertDialog.Builder(TermDetail.this);
                dialog.setTitle("CONFIRM TERM DELETION")
                        .setMessage("Select CONFIRM to delete the term. Select ABORT to cancel.\n\n(records are not recoverable)")
                        .setCancelable(false)
                        .setNegativeButton("ABORT", (dialogInterface, i) -> {
                        })
                        .setPositiveButton("CONFIRM", (dialogInterface, i) -> {
                            termViewModel.deleteUsingTermID(termID);
                            Toast.makeText(TermDetail.this, "Term deleted.", Toast.LENGTH_LONG).show();
                            finish();
                        })
                        .show();
            }
        }
    }
}