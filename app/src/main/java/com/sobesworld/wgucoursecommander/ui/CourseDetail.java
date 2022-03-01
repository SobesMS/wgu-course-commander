package com.sobesworld.wgucoursecommander.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.AssessmentViewModel;
import com.sobesworld.wgucoursecommander.database.TermViewModel;
import com.sobesworld.wgucoursecommander.database.adapters.AssessmentAdapter;
import com.sobesworld.wgucoursecommander.database.entity.AssessmentEntity;
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
    public static final String EXTRA_COURSE_END_DATE = "com.sobesworld.wgucoursecommander.EXTRA_COURSE_END_DATE";
    public static final String EXTRA_COURSE_END_ALERT = "com.sobesworld.wgucoursecommander.EXTRA_COURSE_END_ALERT";
    public static final String EXTRA_COURSE_ALERT_ID = "com.sobesworld.wgucoursecommander.EXTRA_COURSE_ALERT_ID";
    public static final String EXTRA_COURSE_STATUS = "com.sobesworld.wgucoursecommander.EXTRA_COURSE_STATUS";
    public static final String EXTRA_COURSE_MENTORS_NAME = "com.sobesworld.wgucoursecommander.EXTRA_COURSE_MENTORS_NAME";
    public static final String EXTRA_COURSE_MENTORS_PHONE = "com.sobesworld.wgucoursecommander.EXTRA_COURSE_MENTORS_PHONE";
    public static final String EXTRA_COURSE_MENTORS_EMAIL = "com.sobesworld.wgucoursecommander.EXTRA_COURSE_MENTORS_EMAIL";
    public static final String EXTRA_COURSE_NOTES = "com.sobesworld.wgucoursecommander.EXTRA_COURSE_NOTES";
    public static final String EXTRA_COURSE_LINKED_TERM_ID = "com.sobesworld.wgucoursecommander.EXTRA_COURSE_LINKED_TERM_ID";
    private AssessmentAdapter assessmentAdapter;
    private AssessmentViewModel assessmentViewModel;
    private SharedPreferences sharedPreferences;
    private ActivityResultLauncher<Intent> activityLauncher;
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;

    private int courseID;
    private EditText editTextCourseTitle;
    private TextView textViewCourseStartDate;
    private TextView textViewCourseEndDate;
    private boolean courseEndAlert;
    private int courseAlertID;
    private String courseStatus;
    private EditText editTextCourseMentorsName;
    private EditText editTextCourseMentorsPhone;
    private EditText editTextCourseMentorsEmail;
    private String courseNotes;
    private int courseLinkedTermID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        sharedPreferences = getSharedPreferences("com.sobesworld.wgucoursecommander.prefs", Context.MODE_PRIVATE);

        editTextCourseTitle = findViewById(R.id.edit_text_course_title);
        textViewCourseStartDate = findViewById(R.id.text_view_course_start_date);
        textViewCourseEndDate = findViewById(R.id.text_view_course_end_date);
        editTextCourseMentorsName = findViewById(R.id.edit_text_course_mentors_name);
        editTextCourseMentorsPhone = findViewById(R.id.edit_text_course_mentors_phone);
        editTextCourseMentorsEmail = findViewById(R.id.edit_text_course_mentors_email);

        Intent passedIntent = getIntent();
        courseID = passedIntent.getIntExtra(EXTRA_COURSE_ID, -1);
        courseLinkedTermID = passedIntent.getIntExtra(EXTRA_COURSE_LINKED_TERM_ID, -1);

        RecyclerView recyclerView = findViewById(R.id.course_assessment_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        assessmentAdapter = new AssessmentAdapter();
        recyclerView.setAdapter(assessmentAdapter);
        assessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);
        assessmentViewModel.getLinkedAssessments(courseID).observe(this, assessmentEntities -> assessmentAdapter.submitList(assessmentEntities));

        assessmentAdapter.setOnItemClickListener(assessmentEntity -> {
            Intent intent = new Intent(CourseDetail.this, AssessmentDetail.class);
            intent.putExtra(MainActivity.EXTRA_REQUEST_ID, MainActivity.REQUEST_EDIT);
            intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_ID, assessmentEntity.getAssessmentID());
            intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_TITLE, assessmentEntity.getAssessmentTitle());
            intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_TYPE, assessmentEntity.getAssessmentType());
            intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_GOAL_DATE, assessmentEntity.getAssessmentGoalDate());
            intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_GOAL_ALERT, assessmentEntity.isAssessmentGoalAlert());
            intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_ALERT_ID, assessmentEntity.getAssessmentAlertID());
            intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_NOTES, assessmentEntity.getAssessmentNotes());
            intent.putExtra(AssessmentDetail.EXTRA_ASSESSMENT_LINKED_COURSE_ID, assessmentEntity.getAssessmentLinkedCourseID());
            activityLauncher.launch(intent);
        });

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (passedIntent.getIntExtra(MainActivity.EXTRA_REQUEST_ID, 1) == MainActivity.REQUEST_ADD) {
            setTitle("Add Course");
        } else {
            setTitle("Edit Course");
            editTextCourseTitle.setText(passedIntent.getStringExtra(EXTRA_COURSE_TITLE));
            textViewCourseStartDate.setText(passedIntent.getStringExtra(EXTRA_COURSE_START_DATE));
            textViewCourseEndDate.setText(passedIntent.getStringExtra(EXTRA_COURSE_END_DATE));
            courseEndAlert = passedIntent.getBooleanExtra(EXTRA_COURSE_END_ALERT, false);
            courseAlertID = passedIntent.getIntExtra(EXTRA_COURSE_ALERT_ID, -1);
            courseStatus = passedIntent.getStringExtra(EXTRA_COURSE_STATUS);
            editTextCourseMentorsName.setText(passedIntent.getStringExtra(EXTRA_COURSE_MENTORS_NAME));
            editTextCourseMentorsPhone.setText(passedIntent.getStringExtra(EXTRA_COURSE_MENTORS_PHONE));
            editTextCourseMentorsEmail.setText(passedIntent.getStringExtra(EXTRA_COURSE_MENTORS_EMAIL));
            courseNotes = passedIntent.getStringExtra(EXTRA_COURSE_NOTES);
        }

        // sets course start date
        textViewCourseStartDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            String date = textViewCourseStartDate.getText().toString();
            if (!date.trim().isEmpty()) {
                try {
                    calendar.setTime(Objects.requireNonNull(MainActivity.sdf.parse(date)));
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
            textViewCourseStartDate.setText(MainActivity.sdf.format(calendar.getTime()));
        };

        // sets course end date
        textViewCourseEndDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            String date = textViewCourseEndDate.getText().toString();
            if (!date.trim().isEmpty()) {
                try {
                    calendar.setTime(Objects.requireNonNull(MainActivity.sdf.parse(date)));
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
            textViewCourseEndDate.setText(MainActivity.sdf.format(calendar.getTime()));
        };

        // alert switch toggle functionality
        SwitchCompat switchCourseAlert = findViewById(R.id.switch_course_alert);
        switchCourseAlert.setChecked(courseEndAlert);
        switchCourseAlert.setOnCheckedChangeListener((compoundButton, b) -> {
            if (textViewCourseEndDate.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "You must set an end date before turning on alert.", Toast.LENGTH_LONG).show();
                switchCourseAlert.setChecked(courseEndAlert);
            } else {
                courseEndAlert = b;
            }
        });

        // set status spinner data
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

        // set term spinner data
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

        // add assessment button
        ImageView imageViewCourseAddAssessment = findViewById(R.id.image_view_course_add_assessment);
        imageViewCourseAddAssessment.setOnClickListener(view -> {
            Intent intent = new Intent(CourseDetail.this, AssessmentDetail.class);
            intent.putExtra(MainActivity.EXTRA_REQUEST_ID, MainActivity.REQUEST_ADD);
            activityLauncher.launch(intent);
        });

        activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent intent = result.getData();
                    int resultCode = result.getResultCode();
                    if (intent != null) {
                        int assessmentID = intent.getIntExtra(AssessmentDetail.EXTRA_ASSESSMENT_ID, -1);
                        String assessmentTitle = intent.getStringExtra(AssessmentDetail.EXTRA_ASSESSMENT_TITLE);
                        String assessmentType = intent.getStringExtra(AssessmentDetail.EXTRA_ASSESSMENT_TYPE);
                        String assessmentGoalDate = intent.getStringExtra(AssessmentDetail.EXTRA_ASSESSMENT_GOAL_DATE);
                        boolean assessmentGoalAlert = intent.getBooleanExtra(AssessmentDetail.EXTRA_ASSESSMENT_GOAL_ALERT, false);
                        int assessmentAlertID = intent.getIntExtra(AssessmentDetail.EXTRA_ASSESSMENT_ALERT_ID, -1);
                        String assessmentNotes = intent.getStringExtra(AssessmentDetail.EXTRA_ASSESSMENT_NOTES);
                        int assessmentLinkedCourseID = intent.getIntExtra(AssessmentDetail.EXTRA_ASSESSMENT_LINKED_COURSE_ID, -1);
                        if (resultCode == RESULT_OK) {
                            AssessmentEntity assessmentEntity = new AssessmentEntity(assessmentTitle, assessmentType, assessmentGoalDate,
                                    assessmentGoalAlert, assessmentAlertID, assessmentNotes, assessmentLinkedCourseID);
                            if (assessmentID == -1) {
                                assessmentViewModel.insert(assessmentEntity);
                                Toast.makeText(CourseDetail.this, "Assessment added.", Toast.LENGTH_SHORT).show();
                            } else {
                                assessmentEntity.setAssessmentID(assessmentID);
                                assessmentViewModel.update(assessmentEntity);
                                Toast.makeText(CourseDetail.this, "Assessment updated.", Toast.LENGTH_SHORT).show();
                            }
                        } else if (resultCode == MainActivity.RESULT_DELETE) {
                            if (assessmentID == -1) {
                                Toast.makeText(CourseDetail.this, "Assessment does not exist.", Toast.LENGTH_SHORT).show();
                            } else {
                                assessmentViewModel.deleteUsingAssessmentID(assessmentID);
                                Toast.makeText(getApplicationContext(), "Assessment permanently deleted.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );
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

    private void createAlert() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!sharedPreferences.contains(getResources().getString(R.string.uniqueID))) {
            editor.putInt(getResources().getString(R.string.uniqueID), 100).apply();
        }
        int i = sharedPreferences.getInt(getResources().getString(R.string.uniqueID), -1);
        if (i != -1) {
            courseAlertID = i + 1;
            editor.putInt(getResources().getString(R.string.uniqueID), courseAlertID).apply();
            Date endDate = null;
            try {
                endDate=MainActivity.sdf.parse(textViewCourseEndDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String title = editTextCourseTitle.getText().toString() + " Ending Today";
            String text = editTextCourseTitle.getText().toString() + " was scheduled to be completed today. Finish and submit any remaining assessments.";
            long trigger = 0;
            if (endDate != null) {
                trigger = endDate.getTime();
            }
            Intent intent = new Intent(CourseDetail.this, CourseCommReceiver.class);
            intent.putExtra(CourseCommReceiver.EXTRA_BROADCAST_ALERT_ID, courseAlertID);
            intent.putExtra(CourseCommReceiver.EXTRA_BROADCAST_TITLE, title);
            intent.putExtra(CourseCommReceiver.EXTRA_BROADCAST_TEXT, text);
            PendingIntent sender = PendingIntent.getBroadcast(CourseDetail.this, courseAlertID, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
        }
    }

    private void deleteAlert(int id) {
        Intent intent = new Intent(CourseDetail.this, CourseCommReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(CourseDetail.this, id, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        sender.cancel();
    }

    private void upButtonChangeValidation() {
        String courseTitle = editTextCourseTitle.getText().toString();
        String courseStartDate = textViewCourseStartDate.getText().toString();
        String courseEndDate = textViewCourseEndDate.getText().toString();
        String courseMentorsName = editTextCourseMentorsName.getText().toString();
        String courseMentorsPhone = editTextCourseMentorsPhone.getText().toString();
        String courseMentorsEmail = editTextCourseMentorsEmail.getText().toString();

        if (getIntent().getIntExtra(MainActivity.EXTRA_REQUEST_ID, 1) == MainActivity.REQUEST_ADD &&
                courseTitle.trim().isEmpty() && courseStartDate.trim().isEmpty() && courseEndDate.trim().isEmpty() &&
                courseMentorsName.trim().isEmpty() && courseMentorsPhone.trim().isEmpty() && courseMentorsEmail.trim().isEmpty()) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (courseTitle.equals(getIntent().getStringExtra(EXTRA_COURSE_TITLE)) &&
                courseStartDate.equals(getIntent().getStringExtra(EXTRA_COURSE_START_DATE)) &&
                courseEndDate.equals(getIntent().getStringExtra(EXTRA_COURSE_END_DATE)) &&
                courseEndAlert == getIntent().getBooleanExtra(EXTRA_COURSE_END_ALERT, false) &&
                courseAlertID == getIntent().getIntExtra(EXTRA_COURSE_ALERT_ID, -1) &&
                courseStatus.equals(getIntent().getStringExtra(EXTRA_COURSE_STATUS)) &&
                courseMentorsName.equals(getIntent().getStringExtra(EXTRA_COURSE_MENTORS_NAME)) &&
                courseMentorsPhone.equals(getIntent().getStringExtra(EXTRA_COURSE_MENTORS_PHONE)) &&
                courseMentorsEmail.equals(getIntent().getStringExtra(EXTRA_COURSE_MENTORS_EMAIL)) &&
                courseNotes.equals(getIntent().getStringExtra(EXTRA_COURSE_NOTES)) &&
                courseLinkedTermID == getIntent().getIntExtra(EXTRA_COURSE_LINKED_TERM_ID, -1)) {
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
            builder.setPositiveButton("SAVE", (dialogInterface, i) -> saveCourse());
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void saveCourse() {
        if (courseAlertID == -1 && courseEndAlert) {
            createAlert();
        } else if (courseAlertID > 0 && !courseEndAlert) {
            deleteAlert(courseAlertID);
            courseAlertID = -1;
        }

        String courseTitle = editTextCourseTitle.getText().toString();
        String courseStartDate = textViewCourseStartDate.getText().toString();
        String courseEndDate = textViewCourseEndDate.getText().toString();
        String courseMentorsName = editTextCourseMentorsName.getText().toString();
        String courseMentorsPhone = editTextCourseMentorsPhone.getText().toString();
        String courseMentorsEmail = editTextCourseMentorsEmail.getText().toString();

        if (courseTitle.trim().isEmpty() || courseStartDate.trim().isEmpty() || courseEndDate.trim().isEmpty()) {
            if (courseTitle.trim().isEmpty()) {
                editTextCourseTitle.setHintTextColor(ContextCompat.getColor(CourseDetail.this, R.color.red));
            }
            if (courseStartDate.trim().isEmpty()) {
                textViewCourseStartDate.setHintTextColor(ContextCompat.getColor(CourseDetail.this, R.color.red));
            }
            if (courseEndDate.trim().isEmpty()) {
                textViewCourseEndDate.setHintTextColor(ContextCompat.getColor(CourseDetail.this, R.color.red));
            }
            Toast.makeText(CourseDetail.this, "A required field is empty.", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_COURSE_ID, courseID);
            intent.putExtra(EXTRA_COURSE_TITLE, courseTitle);
            intent.putExtra(EXTRA_COURSE_START_DATE, courseStartDate);
            intent.putExtra(EXTRA_COURSE_END_DATE, courseEndDate);
            intent.putExtra(EXTRA_COURSE_END_ALERT, courseEndAlert);
            intent.putExtra(EXTRA_COURSE_ALERT_ID, courseAlertID);
            intent.putExtra(EXTRA_COURSE_STATUS, courseStatus);
            intent.putExtra(EXTRA_COURSE_MENTORS_NAME, courseMentorsName);
            intent.putExtra(EXTRA_COURSE_MENTORS_PHONE, courseMentorsPhone);
            intent.putExtra(EXTRA_COURSE_MENTORS_EMAIL, courseMentorsEmail);
            intent.putExtra(EXTRA_COURSE_NOTES, courseNotes);
            intent.putExtra(EXTRA_COURSE_LINKED_TERM_ID, courseLinkedTermID);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    // creates a delete confirmation dialog and deletes course if confirmed
    private void deleteCourse() {
        if (courseID == -1) {
            setResult(RESULT_CANCELED);
            finish();
            Toast.makeText(this, "New course creation cancelled.", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("CONFIRM COURSE DELETION")
                    .setMessage("Select CONFIRM to delete the course and all related assessments." +
                            "\nSelect ABORT to cancel.\n\n(records are not recoverable)")
                    .setCancelable(false);
            builder.setNegativeButton("ABORT", (dialogInterface, i) -> {
            });
            builder.setPositiveButton("CONFIRM", (dialogInterface, i) -> {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_COURSE_ID, courseID);
                setResult(MainActivity.RESULT_DELETE, intent);
                finish();
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}