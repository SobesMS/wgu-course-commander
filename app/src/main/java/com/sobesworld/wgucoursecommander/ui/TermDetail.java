package com.sobesworld.wgucoursecommander.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sobesworld.wgucoursecommander.MainActivity;
import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.CourseViewModel;
import com.sobesworld.wgucoursecommander.database.adapters.CourseAdapter;
import com.sobesworld.wgucoursecommander.database.entity.CourseEntity;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Objects;

public class TermDetail extends AppCompatActivity {
    public static final String EXTRA_TERM_ID = "com.sobesworld.wgucoursecommander.EXTRA_TERM_ID";
    public static final String EXTRA_TERM_TITLE = "com.sobesworld.wgucoursecommander.EXTRA_TERM_TITLE";
    public static final String EXTRA_TERM_START_DATE = "com.sobesworld.wgucoursecommander.EXTRA_TERM_START_DATE";
    public static final String EXTRA_TERM_END_DATE = "com.sobesworld.wgucoursecommander.EXTRA_TERM_END_DATE";
    private CourseAdapter courseAdapter;
    private CourseViewModel courseViewModel;
    private ActivityResultLauncher<Intent> activityLauncher;
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;

    private int termID;
    private EditText editTextTermTitle;
    private TextView textViewTermStartDate;
    private TextView textViewTermEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

        editTextTermTitle = findViewById(R.id.edit_text_term_title);
        textViewTermStartDate = findViewById(R.id.text_view_term_start_date);
        textViewTermEndDate = findViewById(R.id.text_view_term_end_date);

        Intent passedIntent = getIntent();
        termID = passedIntent.getIntExtra(EXTRA_TERM_ID, -1);

        RecyclerView recyclerView = findViewById(R.id.term_course_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter = new CourseAdapter();
        recyclerView.setAdapter(courseAdapter);
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
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
            activityLauncher.launch(intent);
        });

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (passedIntent.getIntExtra(NavMenu.EXTRA_REQUEST_ID, 1) == NavMenu.REQUEST_ADD) {
            setTitle("Add Term");
        } else {
            setTitle("Edit Term");
            editTextTermTitle.setText(passedIntent.getStringExtra(EXTRA_TERM_TITLE));
            textViewTermStartDate.setText(passedIntent.getStringExtra(EXTRA_TERM_START_DATE));
            textViewTermEndDate.setText(passedIntent.getStringExtra(EXTRA_TERM_END_DATE));
        }

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

        // add course button
        ImageView imageViewTermAddCourse = findViewById(R.id.image_view_term_add_course);
        imageViewTermAddCourse.setOnClickListener(view -> {
            Intent intent = new Intent(TermDetail.this, CourseDetail.class);
            intent.putExtra(NavMenu.EXTRA_REQUEST_ID, NavMenu.REQUEST_ADD);
            intent.putExtra(CourseDetail.EXTRA_COURSE_LINKED_TERM_ID, termID);
            activityLauncher.launch(intent);
        });

        activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent intent = result.getData();
                    int resultCode = result.getResultCode();
                    if (intent != null) {
                        int courseID = intent.getIntExtra(CourseDetail.EXTRA_COURSE_ID, -1);
                        String courseTitle = intent.getStringExtra(CourseDetail.EXTRA_COURSE_TITLE);
                        String courseStartDate = intent.getStringExtra(CourseDetail.EXTRA_COURSE_START_DATE);
                        boolean courseStartAlert = intent.getBooleanExtra(CourseDetail.EXTRA_COURSE_START_ALERT, false);
                        int courseStartAlertID = intent.getIntExtra(CourseDetail.EXTRA_COURSE_START_ALERT_ID, -1);
                        String courseEndDate = intent.getStringExtra(CourseDetail.EXTRA_COURSE_END_DATE);
                        boolean courseEndAlert = intent.getBooleanExtra(CourseDetail.EXTRA_COURSE_END_ALERT, false);
                        int courseEndAlertID = intent.getIntExtra(CourseDetail.EXTRA_COURSE_END_ALERT_ID, -1);
                        String courseStatus = intent.getStringExtra(CourseDetail.EXTRA_COURSE_STATUS);
                        String courseMentorsName = intent.getStringExtra(CourseDetail.EXTRA_COURSE_MENTORS_NAME);
                        String courseMentorsPhone = intent.getStringExtra(CourseDetail.EXTRA_COURSE_MENTORS_PHONE);
                        String courseMentorsEmail = intent.getStringExtra(CourseDetail.EXTRA_COURSE_MENTORS_EMAIL);
                        String courseNotes = intent.getStringExtra(CourseDetail.EXTRA_COURSE_NOTES);
                        int courseLinkedTermID = intent.getIntExtra(CourseDetail.EXTRA_COURSE_LINKED_TERM_ID, -1);
                        if (resultCode == RESULT_OK) {
                            CourseEntity courseEntity = new CourseEntity(courseTitle, courseStartDate, courseStartAlert, courseStartAlertID,
                                    courseEndDate, courseEndAlert, courseEndAlertID, courseStatus, courseMentorsName, courseMentorsPhone,
                                    courseMentorsEmail, courseNotes, courseLinkedTermID);
                            if (courseID == -1) {
                                courseViewModel.insert(courseEntity);
                                Toast.makeText(TermDetail.this, "Course added.", Toast.LENGTH_SHORT).show();
                            } else {
                                courseEntity.setCourseID(courseID);
                                courseViewModel.update(courseEntity);
                                Toast.makeText(TermDetail.this, "Course updated.", Toast.LENGTH_SHORT).show();
                            }
                        } else if (resultCode == NavMenu.RESULT_DELETE) {
                            if (courseID == -1) {
                                Toast.makeText(TermDetail.this, "Course does not exist.", Toast.LENGTH_SHORT).show();
                            } else {
                                courseViewModel.deleteUsingCourseID(courseID);
                                Toast.makeText(getApplicationContext(), "Course permanently deleted.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            Intent intent = new Intent(TermDetail.this, MainActivity.class);
            startActivity(intent);
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

    private void upButtonChangeValidation() {
        String termTitle = editTextTermTitle.getText().toString();
        String termStartDate = textViewTermStartDate.getText().toString();
        String termEndDate = textViewTermEndDate.getText().toString();

        if (getIntent().getIntExtra(NavMenu.EXTRA_REQUEST_ID, 1) == NavMenu.REQUEST_ADD
                && termTitle.trim().isEmpty() && termStartDate.trim().isEmpty() && termEndDate.trim().isEmpty()) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (termTitle.equals(getIntent().getStringExtra(EXTRA_TERM_TITLE)) &&
                termStartDate.equals(getIntent().getStringExtra(EXTRA_TERM_START_DATE)) &&
                termEndDate.equals(getIntent().getStringExtra(EXTRA_TERM_END_DATE))) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("WARNING: UNSAVED CHANGES")
                    .setMessage("You have unsaved changes. Click SAVE to save and close. Click CANCEL to close without saving.")
                    .setCancelable(false);
            builder.setNegativeButton("CANCEL", (dialogInterface, i) -> {
                setResult(RESULT_CANCELED);
                finish();
            });
            builder.setPositiveButton("SAVE", (dialogInterface, i) -> saveTerm());
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void saveTerm() {
        String termTitle = editTextTermTitle.getText().toString();
        String termStartDate = textViewTermStartDate.getText().toString();
        String termEndDate = textViewTermEndDate.getText().toString();

        if (termTitle.trim().isEmpty() || termStartDate.trim().isEmpty() || termEndDate.trim().isEmpty()) {
            if (termTitle.trim().isEmpty()) {
                editTextTermTitle.setHintTextColor(ContextCompat.getColor(TermDetail.this, R.color.red));
            }
            if (termStartDate.trim().isEmpty()) {
                textViewTermStartDate.setHintTextColor(ContextCompat.getColor(TermDetail.this, R.color.red));
            }
            if (termEndDate.trim().isEmpty()) {
                textViewTermEndDate.setHintTextColor(ContextCompat.getColor(TermDetail.this, R.color.red));
            }
            Toast.makeText(TermDetail.this, "A required field is empty.", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_TERM_ID, termID);
            intent.putExtra(EXTRA_TERM_TITLE, termTitle);
            intent.putExtra(EXTRA_TERM_START_DATE, termStartDate);
            intent.putExtra(EXTRA_TERM_END_DATE, termEndDate);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    // creates a delete confirmation dialog and deletes course if confirmed
    private void deleteTerm() {
        if (termID == -1) {
            setResult(RESULT_CANCELED);
            finish();
            Toast.makeText(this, "New term creation cancelled.", Toast.LENGTH_SHORT).show();
        } else {
            if (courseAdapter.getItemCount() > 0) {
                Toast.makeText(this, "This term can't be deleted until linked courses are " +
                                "deleted or transferred to another term.", Toast.LENGTH_LONG).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("CONFIRM TERM DELETION")
                        .setMessage("Select CONFIRM to delete the term. Select ABORT to cancel.\n\n(records are not recoverable)")
                        .setCancelable(false);
                builder.setNegativeButton("ABORT", (dialogInterface, i) -> {
                });
                builder.setPositiveButton("CONFIRM", (dialogInterface, i) -> {
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_TERM_ID, termID);
                    setResult(NavMenu.RESULT_DELETE, intent);
                    finish();
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }
}