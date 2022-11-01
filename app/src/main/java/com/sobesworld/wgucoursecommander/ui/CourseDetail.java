package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sobesworld.wgucoursecommander.MainActivity;
import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.AssessmentViewModel;
import com.sobesworld.wgucoursecommander.database.CourseViewModel;
import com.sobesworld.wgucoursecommander.database.TermViewModel;
import com.sobesworld.wgucoursecommander.database.adapters.AssessmentAdapter;
import com.sobesworld.wgucoursecommander.database.entity.CourseEntity;
import com.sobesworld.wgucoursecommander.database.entity.TermEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CourseDetail extends AppCompatActivity {
    public static final String EXTRA_COURSE_ID = "com.sobesworld.wgucoursecommander.EXTRA_COURSE_ID";
    public static final String EXTRA_COURSE_TITLE = "com.sobesworld.wgucoursecommander.EXTRA_COURSE_TITLE";
    public static final String EXTRA_COURSE_START_DATE = "com.sobesworld.wgucoursecommander.EXTRA_COURSE_START_DATE";
    public static final String EXTRA_COURSE_START_ALERT = "com.sobesworld.wgucoursecommander.EXTRA_COURSE_START_ALERT";
    public static final String EXTRA_COURSE_START_ALERT_ID = "com.sobesworld.wgucoursecommander.EXTRA_COURSE_START_ALERT_ID";
    public static final String EXTRA_COURSE_END_DATE = "com.sobesworld.wgucoursecommander.EXTRA_COURSE_END_DATE";
    public static final String EXTRA_COURSE_END_ALERT = "com.sobesworld.wgucoursecommander.EXTRA_COURSE_END_ALERT";
    public static final String EXTRA_COURSE_END_ALERT_ID = "com.sobesworld.wgucoursecommander.EXTRA_COURSE_END_ALERT_ID";
    public static final String EXTRA_COURSE_STATUS = "com.sobesworld.wgucoursecommander.EXTRA_COURSE_STATUS";
    public static final String EXTRA_COURSE_MENTORS_NAME = "com.sobesworld.wgucoursecommander.EXTRA_COURSE_MENTORS_NAME";
    public static final String EXTRA_COURSE_MENTORS_PHONE = "com.sobesworld.wgucoursecommander.EXTRA_COURSE_MENTORS_PHONE";
    public static final String EXTRA_COURSE_MENTORS_EMAIL = "com.sobesworld.wgucoursecommander.EXTRA_COURSE_MENTORS_EMAIL";
    public static final String EXTRA_COURSE_NOTES = "com.sobesworld.wgucoursecommander.EXTRA_COURSE_NOTES";
    public static final String EXTRA_COURSE_LINKED_TERM_ID = "com.sobesworld.wgucoursecommander.EXTRA_COURSE_LINKED_TERM_ID";

    private AssessmentAdapter assessmentAdapter;
    private DatePickerDialog.OnDateSetListener startDateSetListener, endDateSetListener;

    private int courseID, courseStartAlertID, courseEndAlertID, courseLinkedTermID;
    private EditText editTextCourseTitle, editTextCourseMentorsName, editTextCourseMentorsPhone, editTextCourseMentorsEmail;
    private TextView textViewCourseStartDate, textViewCourseEndDate;
    private boolean courseStartAlert, courseEndAlert;
    private String courseStatus, courseNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        editTextCourseTitle = findViewById(R.id.edit_text_course_title);
        textViewCourseStartDate = findViewById(R.id.text_view_course_start_date);
        textViewCourseEndDate = findViewById(R.id.text_view_course_end_date);
        editTextCourseMentorsName = findViewById(R.id.edit_text_course_mentors_name);
        editTextCourseMentorsPhone = findViewById(R.id.edit_text_course_mentors_phone);
        editTextCourseMentorsEmail = findViewById(R.id.edit_text_course_mentors_email);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent passedIntent = getIntent();

        assessmentAdapter = new AssessmentAdapter();
        courseLinkedTermID = getIntent().getIntExtra(EXTRA_COURSE_LINKED_TERM_ID, -1);

        // determines whether adding a new assessment or editing an existing and sets UI appropriately
        if (passedIntent.getIntExtra(NavMenu.EXTRA_REQUEST_ID, 1) == NavMenu.REQUEST_ADD) {
            setTitle("Add Course");
            courseID = 0;
            courseNotes = "";
            courseStartAlert = false;
            courseStartAlertID = -1;
            courseEndAlert = false;
            courseEndAlertID = -1;
        } else {
            setTitle("Edit Course");
            courseID = getIntent().getIntExtra(EXTRA_COURSE_ID, -1);
            if (courseID == -1) {
                Toast.makeText(CourseDetail.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                finish();
            } else {
                editTextCourseTitle.setText(getIntent().getStringExtra(EXTRA_COURSE_TITLE));
                textViewCourseStartDate.setText(getIntent().getStringExtra(EXTRA_COURSE_START_DATE));
                courseStartAlert = getIntent().getBooleanExtra(EXTRA_COURSE_START_ALERT, false);
                courseStartAlertID = getIntent().getIntExtra(EXTRA_COURSE_START_ALERT_ID, -1);
                textViewCourseEndDate.setText(getIntent().getStringExtra(EXTRA_COURSE_END_DATE));
                courseEndAlert = getIntent().getBooleanExtra(EXTRA_COURSE_END_ALERT, false);
                courseEndAlertID = getIntent().getIntExtra(EXTRA_COURSE_END_ALERT_ID, -1);
                courseStatus = getIntent().getStringExtra(EXTRA_COURSE_STATUS);
                editTextCourseMentorsName.setText(getIntent().getStringExtra(EXTRA_COURSE_MENTORS_NAME));
                editTextCourseMentorsPhone.setText(getIntent().getStringExtra(EXTRA_COURSE_MENTORS_PHONE));
                editTextCourseMentorsEmail.setText(getIntent().getStringExtra(EXTRA_COURSE_MENTORS_EMAIL));
                courseNotes = getIntent().getStringExtra(EXTRA_COURSE_NOTES);

                TextView assessmentListTitle = findViewById(R.id.assessment_list_label);
                assessmentListTitle.setVisibility(View.VISIBLE);

                // add assessment button
                ImageView imageViewCourseAddAssessment = findViewById(R.id.image_view_course_add_assessment);
                imageViewCourseAddAssessment.setVisibility(View.VISIBLE);
                imageViewCourseAddAssessment.setOnClickListener(view -> {
                    Intent intent = new Intent(CourseDetail.this, AssessmentDetail.class);
                    intent.putExtra(NavMenu.EXTRA_REQUEST_ID, NavMenu.REQUEST_ADD);
                    startActivity(intent);
                });

                RecyclerView recyclerView = findViewById(R.id.course_assessment_list);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(assessmentAdapter);
                AssessmentViewModel assessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);
                assessmentViewModel.getLinkedAssessments(courseID).observe(this, assessmentEntities -> assessmentAdapter.submitList(assessmentEntities));

                assessmentAdapter.setOnItemClickListener(assessmentEntity -> {
                    Intent intent = new Intent(CourseDetail.this, AssessmentDetail.class);
                    intent.putExtra(NavMenu.EXTRA_REQUEST_ID, NavMenu.REQUEST_EDIT);
                    intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_ID, assessmentEntity.getAssessmentID());
                    intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_TITLE, assessmentEntity.getAssessmentTitle());
                    intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_TYPE, assessmentEntity.getAssessmentType());
                    intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_GOAL_DATE, assessmentEntity.getAssessmentGoalDate());
                    intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_GOAL_ALERT, assessmentEntity.isAssessmentGoalAlert());
                    intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_ALERT_ID, assessmentEntity.getAssessmentAlertID());
                    intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_NOTES, assessmentEntity.getAssessmentNotes());
                    intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_LINKED_COURSE_ID, assessmentEntity.getAssessmentLinkedCourseID());
                    startActivity(intent);
                });
            }
        }

        // logic for date selectors
        textViewCourseStartDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            String date = textViewCourseStartDate.getText().toString();
            if (!date.trim().isEmpty()) {
                try {
                    calendar.setTime(Objects.requireNonNull(NavMenu.sdf.parse(date)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            new DatePickerDialog(CourseDetail.this, startDateSetListener, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        startDateSetListener = (datePicker, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            textViewCourseStartDate.setText(NavMenu.sdf.format(calendar.getTime()));
        };

        textViewCourseEndDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            String date = textViewCourseEndDate.getText().toString();
            if (!date.trim().isEmpty()) {
                try {
                    calendar.setTime(Objects.requireNonNull(NavMenu.sdf.parse(date)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            new DatePickerDialog(CourseDetail.this, endDateSetListener, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        endDateSetListener = (datePicker, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            textViewCourseEndDate.setText(NavMenu.sdf.format(calendar.getTime()));
        };

        // logic for alert switches
        SwitchCompat switchCourseStartAlert = findViewById(R.id.switch_course_start_alert);
        switchCourseStartAlert.setChecked(courseStartAlert);
        switchCourseStartAlert.setOnCheckedChangeListener((compoundButton, b) -> {
            if (textViewCourseStartDate.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "You must set a start date before turning on alert.", Toast.LENGTH_LONG).show();
                switchCourseStartAlert.setChecked(courseStartAlert);
            } else {
                courseStartAlert = b;
            }
        });

        SwitchCompat switchCourseEndAlert = findViewById(R.id.switch_course_end_alert);
        switchCourseEndAlert.setChecked(courseEndAlert);
        switchCourseEndAlert.setOnCheckedChangeListener((compoundButton, b) -> {
            if (textViewCourseEndDate.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "You must set an end date before turning on alert.", Toast.LENGTH_LONG).show();
                switchCourseEndAlert.setChecked(courseEndAlert);
            } else {
                courseEndAlert = b;
            }
        });

        // logic for spinners
        Spinner spinnerCourseStatus = findViewById(R.id.spinner_course_status);
        List<String> statusOptions = new ArrayList<>();
        statusOptions.add("in progress");
        statusOptions.add("plan to take");
        statusOptions.add("completed");
        statusOptions.add("dropped");
        ArrayAdapter<String> statusSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusOptions);
        statusSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourseStatus.setAdapter(statusSpinnerAdapter);
        for (int i = 0; i < statusSpinnerAdapter.getCount(); i++) {
            String item = statusSpinnerAdapter.getItem(i);
            if (item.equals(courseStatus)) {
                spinnerCourseStatus.setSelection(i);
            }
        }
        spinnerCourseStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                courseStatus = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Spinner spinnerLinkedTerm = findViewById(R.id.spinner_linked_term);
        TermViewModel termViewModel = new ViewModelProvider(this).get(TermViewModel.class);
        termViewModel.getAllTerms().observe(this, termEntities -> {
            ArrayAdapter<TermEntity> termSpinnerAdapter = new ArrayAdapter<>(CourseDetail.this,
                    android.R.layout.simple_spinner_item, termEntities);
            termSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerLinkedTerm.setAdapter(termSpinnerAdapter);
            for (int i = 0; i < termSpinnerAdapter.getCount(); i++) {
                if (termSpinnerAdapter.getItem(i).getTermID() == courseLinkedTermID) {
                    spinnerLinkedTerm.setSelection(i);
                }
            }
        });
        spinnerLinkedTerm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TermEntity term = (TermEntity) adapterView.getSelectedItem();
                courseLinkedTermID = term.getTermID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // confirms a user is currently logged in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            startActivity(new Intent(CourseDetail.this, MainActivity.class));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_assessment_detail_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            upButtonChangeValidation();
        }
        if (item.getItemId() == R.id.note_course_assessment_detail_menu) {
            showNoteDialog();
        }
        if (item.getItemId() == R.id.save_course_assessment_detail_menu) {
            saveCourse();
        }
        if (item.getItemId() == R.id.delete_course_assessment_detail_menu) {
            deleteCourse();
        }
        return super.onOptionsItemSelected(item);
    }

    // handles editing and sharing of notes
    void showNoteDialog() {
        if (courseNotes == null) {
            courseNotes = "";
        }

        final Dialog dialog = new Dialog(CourseDetail.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.note_dialog);
        final TextView noteHeader = dialog.findViewById(R.id.note_header);
        String headerText = editTextCourseTitle.getText() + " Notes";
        noteHeader.setText(headerText);
        final EditText noteBody = dialog.findViewById(R.id.note_body);
        noteBody.setText(courseNotes);
        Button saveNoteButton = dialog.findViewById(R.id.save_note_button);
        Button shareNoteButton = dialog.findViewById(R.id.share_note_button);

        saveNoteButton.setOnClickListener(view -> {
            courseNotes = noteBody.getText().toString();
            dialog.dismiss();
        });

        shareNoteButton.setOnClickListener(view -> {
            String title = editTextCourseTitle.getText().toString();
            String notes = title + " notes: " + noteBody.getText().toString();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TITLE, "Notes from " + title);
            sendIntent.putExtra(Intent.EXTRA_TEXT, notes);
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });
        dialog.show();
    }

    private void createAlert(int alertID, Date alertDate, String alertTitle, String alertText) {
        if (alertID != -1) {
            long trigger = 0;
            if (alertDate != null) {
                trigger = alertDate.getTime();
            }
            Intent intent = new Intent(CourseDetail.this, CourseCommReceiver.class);
            intent.setAction(CourseCommReceiver.ACTION_DATE_ALERT);
            intent.putExtra(CourseCommReceiver.EXTRA_NOTIFICATION_ID, alertID);
            intent.putExtra(CourseCommReceiver.EXTRA_NOTIFICATION_TITLE, alertTitle);
            intent.putExtra(CourseCommReceiver.EXTRA_NOTIFICATION_TEXT, alertText);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(CourseDetail.this, alertID, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
        }
    }

    private void deleteAlert(int alertID) {
        Intent intent = new Intent(CourseDetail.this, CourseCommReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(CourseDetail.this, alertID, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    private void upButtonChangeValidation() {
        String courseTitle = editTextCourseTitle.getText().toString().trim();
        String courseStartDate = textViewCourseStartDate.getText().toString().trim();
        String courseEndDate = textViewCourseEndDate.getText().toString().trim();
        String courseMentorsName = editTextCourseMentorsName.getText().toString().trim();
        String courseMentorsPhone = editTextCourseMentorsPhone.getText().toString().trim();
        String courseMentorsEmail = editTextCourseMentorsEmail.getText().toString().trim();

        if (getIntent().getIntExtra(NavMenu.EXTRA_REQUEST_ID, 1) == NavMenu.REQUEST_ADD &&
                courseTitle.isEmpty() && courseStartDate.isEmpty() && courseEndDate.isEmpty() &&
                courseMentorsName.isEmpty() && courseMentorsPhone.isEmpty() && courseMentorsEmail.isEmpty()) {
            finish();
        } else if (courseTitle.equals(getIntent().getStringExtra(EXTRA_COURSE_TITLE)) &&
                courseStartDate.equals(getIntent().getStringExtra(EXTRA_COURSE_START_DATE)) &&
                courseStartAlert == getIntent().getBooleanExtra(EXTRA_COURSE_START_ALERT, false) &&
                courseStartAlertID == getIntent().getIntExtra(EXTRA_COURSE_START_ALERT_ID, -1) &&
                courseEndDate.equals(getIntent().getStringExtra(EXTRA_COURSE_END_DATE)) &&
                courseEndAlert == getIntent().getBooleanExtra(EXTRA_COURSE_END_ALERT, false) &&
                courseEndAlertID == getIntent().getIntExtra(EXTRA_COURSE_END_ALERT_ID, -1) &&
                courseStatus.equals(getIntent().getStringExtra(EXTRA_COURSE_STATUS)) &&
                courseMentorsName.equals(getIntent().getStringExtra(EXTRA_COURSE_MENTORS_NAME)) &&
                courseMentorsPhone.equals(getIntent().getStringExtra(EXTRA_COURSE_MENTORS_PHONE)) &&
                courseMentorsEmail.equals(getIntent().getStringExtra(EXTRA_COURSE_MENTORS_EMAIL)) &&
                courseNotes.equals(getIntent().getStringExtra(EXTRA_COURSE_NOTES)) &&
                courseLinkedTermID == getIntent().getIntExtra(EXTRA_COURSE_LINKED_TERM_ID, -1)) {
            finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("WARNING: UNSAVED CHANGES")
                    .setMessage("You have unsaved changes. Click SAVE to save and close. Click CANCEL to close without saving.")
                    .setCancelable(false)
                    .setNegativeButton("CANCEL", (dialogInterface, i) -> finish())
                    .setPositiveButton("SAVE", (dialogInterface, i) -> saveCourse())
                    .show();
        }
    }

    private void saveCourse() {
        String courseTitle = editTextCourseTitle.getText().toString().trim();
        String courseStartDate = textViewCourseStartDate.getText().toString().trim();
        String courseEndDate = textViewCourseEndDate.getText().toString().trim();
        String courseMentorsName = editTextCourseMentorsName.getText().toString().trim();
        String courseMentorsPhone = editTextCourseMentorsPhone.getText().toString().trim();
        String courseMentorsEmail = editTextCourseMentorsEmail.getText().toString().trim();
        CourseViewModel courseViewModel = new ViewModelProvider(CourseDetail.this).get(CourseViewModel.class);

        if (courseTitle.isEmpty() || courseStartDate.isEmpty() || courseEndDate.isEmpty()) {
            if (courseTitle.isEmpty()) {
                editTextCourseTitle.setError("Title is required.");
                editTextCourseTitle.setHintTextColor(ContextCompat.getColor(CourseDetail.this, R.color.red));
            }
            if (courseStartDate.isEmpty()) {
                textViewCourseStartDate.setError("Start date is required.");
                textViewCourseStartDate.setHintTextColor(ContextCompat.getColor(CourseDetail.this, R.color.red));
            }
            if (courseEndDate.isEmpty()) {
                textViewCourseEndDate.setError("End date is required");
                textViewCourseEndDate.setHintTextColor(ContextCompat.getColor(CourseDetail.this, R.color.red));
            }
        } else {
            SharedPreferences sharedPreferences = this.getSharedPreferences(NavMenu.SHARED_PREFS_FILENAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // start date alert validation
            if (courseStartAlertID == -1 && courseStartAlert) {
                int i = sharedPreferences.getInt(NavMenu.SHARED_PREFS_ALERT_ID_COUNTER, -1);
                courseStartAlertID = i;
                editor.putInt(NavMenu.SHARED_PREFS_ALERT_ID_COUNTER, i + 1).apply();
                Date alertDate = null;
                try {
                    alertDate = NavMenu.sdf.parse(courseStartDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String alertTitle = "Course Starts Today";
                String alertBody = editTextCourseTitle.getText().toString() + " is scheduled to start today.";
                createAlert(courseStartAlertID, alertDate, alertTitle, alertBody);
            } else if (courseStartAlertID > 0 && courseStartAlert && !courseStartDate.equals(getIntent().getStringExtra(EXTRA_COURSE_START_DATE))) {
                deleteAlert(courseStartAlertID);
                Date alertDate = null;
                try {
                    alertDate = NavMenu.sdf.parse(courseStartDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String alertTitle = "Course Starts Today";
                String alertBody = editTextCourseTitle.getText().toString() + " is scheduled to start today.";
                createAlert(courseStartAlertID, alertDate, alertTitle, alertBody);
            } else if (courseStartAlertID > 0 && !courseStartAlert) {
                deleteAlert(courseStartAlertID);
                courseStartAlertID = -1;
            }

            // end date alert validation
            if (courseEndAlertID == -1 && courseEndAlert) {
                int i = sharedPreferences.getInt(NavMenu.SHARED_PREFS_ALERT_ID_COUNTER, -1);
                courseEndAlertID = i;
                editor.putInt(NavMenu.SHARED_PREFS_ALERT_ID_COUNTER, i + 1).apply();
                Date alertDate = null;
                try {
                    alertDate = NavMenu.sdf.parse(courseEndDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String alertTitle = "Course Ends Today";
                String alertBody = editTextCourseTitle.getText().toString() + " is scheduled to finish today.";
                createAlert(courseEndAlertID, alertDate, alertTitle, alertBody);
            } else if (courseEndAlertID > 0 && courseEndAlert && !courseEndDate.equals(getIntent().getStringExtra(EXTRA_COURSE_END_DATE))) {
                deleteAlert(courseEndAlertID);
                Date alertDate = null;
                try {
                    alertDate = NavMenu.sdf.parse(courseEndDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String alertTitle = "Course Ends Today";
                String alertBody = editTextCourseTitle.getText().toString() + " is scheduled to finish today.";
                createAlert(courseEndAlertID, alertDate, alertTitle, alertBody);
                Toast.makeText(this, "end alert date change successful", Toast.LENGTH_SHORT).show();
            } else if (courseEndAlertID > 0 && !courseEndAlert) {
                deleteAlert(courseEndAlertID);
                courseEndAlertID = -1;
            }

            CourseEntity course = new CourseEntity(courseTitle, courseStartDate, courseStartAlert, courseStartAlertID,
                    courseEndDate, courseEndAlert, courseStartAlertID, courseStatus, courseMentorsName, courseMentorsPhone,
                    courseMentorsEmail, courseNotes, courseLinkedTermID);
            if (courseID == 0) {
                courseViewModel.insert(course);
                Toast.makeText(CourseDetail.this, "New course created.", Toast.LENGTH_LONG).show();
                finish();
            } else {
                course.setCourseID(courseID);
                courseViewModel.update(course);
                Toast.makeText(CourseDetail.this, "Course updated.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void deleteCourse() {
        if (courseID == 0) {
            Toast.makeText(CourseDetail.this, "New course creation cancelled.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            if (assessmentAdapter.getItemCount() > 0) {
                Toast.makeText(CourseDetail.this, "This course can't be deleted until linked assessments are " +
                        "deleted or transferred to another course.", Toast.LENGTH_LONG).show();
            } else {
                CourseViewModel courseViewModel = new ViewModelProvider(CourseDetail.this).get(CourseViewModel.class);
                AlertDialog.Builder builder = new AlertDialog.Builder(CourseDetail.this);
                builder.setTitle("CONFIRM COURSE DELETION")
                        .setMessage("Select CONFIRM to delete the course and all related assessments. Select ABORT to cancel.\n\n(records are not recoverable)")
                        .setCancelable(false)
                        .setNegativeButton("ABORT", (dialogInterface, i) -> {
                        })
                        .setPositiveButton("CONFIRM", (dialogInterface, i) -> {
                            if (courseStartAlert && courseStartAlertID != -1) {
                                deleteAlert(courseStartAlertID);
                            }
                            if (courseEndAlert && courseEndAlertID != -1) {
                                deleteAlert(courseEndAlertID);
                            }
                            courseViewModel.deleteUsingCourseID(courseID);
                            Toast.makeText(CourseDetail.this, "Course deleted.", Toast.LENGTH_LONG).show();
                            finish();
                        })
                        .show();
            }
        }
    }
}