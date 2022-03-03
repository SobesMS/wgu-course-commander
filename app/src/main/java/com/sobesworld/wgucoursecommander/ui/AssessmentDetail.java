package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.CourseViewModel;
import com.sobesworld.wgucoursecommander.database.entity.CourseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AssessmentDetail extends AppCompatActivity {
    public static final String EXTRA_ASSESSMENT_ID = "com.sobesworld.wgucoursecommander.EXTRA_ASSESSMENT_ID";
    public static final String EXTRA_ASSESSMENT_TITLE = "com.sobesworld.wgucoursecommander.EXTRA_ASSESSMENT_TITLE";
    public static final String EXTRA_ASSESSMENT_TYPE = "com.sobesworld.wgucoursecommander.EXTRA_ASSESSMENT_TYPE";
    public static final String EXTRA_ASSESSMENT_GOAL_DATE = "com.sobesworld.wgucoursecommander.EXTRA_ASSESSMENT_GOAL_DATE";
    public static final String EXTRA_ASSESSMENT_GOAL_ALERT = "com.sobesworld.wgucoursecommander.EXTRA_ASSESSMENT_GOAL_ALERT";
    public static final String EXTRA_ASSESSMENT_ALERT_ID = "com.sobesworld.wgucoursecommander.EXTRA_ASSESSMENT_ALERT_ID";
    public static final String EXTRA_ASSESSMENT_NOTES = "com.sobesworld.wgucoursecommander.EXTRA_ASSESSMENT_ALERT_NOTES";
    public static final String EXTRA_ASSESSMENT_LINKED_COURSE_ID = "com.sobesworld.wgucoursecommander.EXTRA_ASSESSMENT_LINKED_COURSE_ID";

    private DatePickerDialog.OnDateSetListener goalDateSetListener;

    private int assessmentID;
    private EditText editTextAssessmentTitle;
    private String assessmentType;
    private TextView textViewAssessmentGoalDate;
    private boolean assessmentGoalAlert;
    private int assessmentAlertID;
    private String assessmentNotes;
    private int assessmentLinkedCourseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);

        editTextAssessmentTitle = findViewById(R.id.edit_text_assessment_title);
        textViewAssessmentGoalDate = findViewById(R.id.text_view_assessment_goal_date_detail);

        Intent passedIntent = getIntent();
        assessmentID = passedIntent.getIntExtra(EXTRA_ASSESSMENT_ID, -1);
        assessmentLinkedCourseID = passedIntent.getIntExtra(EXTRA_ASSESSMENT_LINKED_COURSE_ID, -1);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (passedIntent.getIntExtra(MainActivity.EXTRA_REQUEST_ID, -1) == MainActivity.REQUEST_ADD) {
            setTitle("Add Assessment");
            assessmentGoalAlert = false;
            assessmentAlertID = -1;
            assessmentNotes = getIntent().getStringExtra(EXTRA_ASSESSMENT_NOTES);
        } else {
            setTitle("Edit Assessment");
            editTextAssessmentTitle.setText(passedIntent.getStringExtra(EXTRA_ASSESSMENT_TITLE));
            assessmentType = passedIntent.getStringExtra(EXTRA_ASSESSMENT_TYPE);
            textViewAssessmentGoalDate.setText(passedIntent.getStringExtra(EXTRA_ASSESSMENT_GOAL_DATE));
            assessmentGoalAlert = passedIntent.getBooleanExtra(EXTRA_ASSESSMENT_GOAL_ALERT, false);
            assessmentAlertID = passedIntent.getIntExtra(EXTRA_ASSESSMENT_ALERT_ID, -1);
            assessmentNotes = passedIntent.getStringExtra(EXTRA_ASSESSMENT_NOTES);
        }

        // sets assessment goal date
        textViewAssessmentGoalDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            String date = textViewAssessmentGoalDate.getText().toString();
            if (!date.trim().isEmpty()) {
                try {
                    calendar.setTime(Objects.requireNonNull(MainActivity.sdf.parse(date)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            new DatePickerDialog(AssessmentDetail.this, goalDateSetListener, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        goalDateSetListener = (datePicker, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            textViewAssessmentGoalDate.setText(MainActivity.sdf.format(calendar.getTime()));
        };

        // alert switch toggle functionality
        SwitchCompat switchAssessmentGoalAlert = findViewById(R.id.switch_assessment_alert);
        switchAssessmentGoalAlert.setChecked(assessmentGoalAlert);
        switchAssessmentGoalAlert.setOnCheckedChangeListener((compoundButton, b) -> {
            if (textViewAssessmentGoalDate.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "You must set a goal date before turning on alert.", Toast.LENGTH_LONG).show();
                switchAssessmentGoalAlert.setChecked(assessmentGoalAlert);
            } else {
                assessmentGoalAlert = b;
            }
        });

        // set type spinner data
        Spinner spinnerAssessmentType = findViewById(R.id.spinner_assessment_type);
        List<String> typeOptions = new ArrayList<>();
        typeOptions.add("Performance");
        typeOptions.add("Objective");
        ArrayAdapter<String> typeSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeOptions);
        typeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssessmentType.setAdapter(typeSpinnerAdapter);
        for (int i = 0; i < typeSpinnerAdapter.getCount(); i++) {
            String item = typeSpinnerAdapter.getItem(i);
            if (item.equals(assessmentType)) {
                spinnerAssessmentType.setSelection(i);
            }
        }
        spinnerAssessmentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                assessmentType = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // set course spinner data
        Spinner spinnerLinkedCourse = findViewById(R.id.spinner_assessment_linked_course);
        CourseViewModel courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        courseViewModel.getAllCourses().observe(this, courseEntities -> {
            ArrayAdapter<CourseEntity> courseSpinnerAdapter = new ArrayAdapter<>(AssessmentDetail.this,
                    android.R.layout.simple_spinner_item, courseEntities);
            courseSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerLinkedCourse.setAdapter(courseSpinnerAdapter);
            for (int i = 0; i < courseSpinnerAdapter.getCount(); i++) {
                if (courseSpinnerAdapter.getItem(i).getCourseID() == assessmentLinkedCourseID) {
                    spinnerLinkedCourse.setSelection(i);
                }
            }
        });
        spinnerLinkedCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CourseEntity course = (CourseEntity) adapterView.getSelectedItem();
                assessmentLinkedCourseID = course.getCourseID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
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
            saveAssessment();
        }
        if (item.getItemId() == R.id.delete_course_assessment_detail_menu) {
            deleteAssessment();
        }
        return super.onOptionsItemSelected(item);
    }

    // handles editing and sharing of notes
    void showNoteDialog() {
        final Dialog dialog = new Dialog(AssessmentDetail.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.note_dialog);
        final TextView noteHeader = dialog.findViewById(R.id.note_header);
        String headerText = editTextAssessmentTitle.getText() + " Notes";
        noteHeader.setText(headerText);
        final EditText noteBody = dialog.findViewById(R.id.note_body);
        noteBody.setText(assessmentNotes);
        Button saveNoteButton = dialog.findViewById(R.id.save_note_button);
        Button shareNoteButton = dialog.findViewById(R.id.share_note_button);

        saveNoteButton.setOnClickListener(view -> {
            assessmentNotes = noteBody.getText().toString();
            dialog.dismiss();
        });

        shareNoteButton.setOnClickListener(view -> {
            String title = editTextAssessmentTitle.getText().toString();
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

    private void createAlert(int alertID, Date alertDate, String alertTitle, String alertBody) {
        if (alertID != -1) {
            long trigger = 0;
            if (alertDate != null) {
                trigger = alertDate.getTime();
            }
            Intent intent = new Intent(AssessmentDetail.this, CourseCommReceiver.class);
            intent.putExtra(CourseCommReceiver.EXTRA_NOTIFICATION_ID, alertID);
            intent.putExtra(CourseCommReceiver.EXTRA_NOTIFICATION_TITLE, alertTitle);
            intent.putExtra(CourseCommReceiver.EXTRA_NOTIFICATION_BODY, alertBody);
            PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetail.this, alertID, intent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
        }
    }

    private void deleteAlert(int id) {
        Intent intent = new Intent(AssessmentDetail.this, CourseCommReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetail.this, id, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        sender.cancel();
    }

    private void upButtonChangeValidation() {
        String assessmentTitle = editTextAssessmentTitle.getText().toString();
        String assessmentGoalDate = textViewAssessmentGoalDate.getText().toString();

        if (getIntent().getIntExtra(MainActivity.EXTRA_REQUEST_ID, 1) == MainActivity.REQUEST_ADD &&
                assessmentTitle.trim().isEmpty() && assessmentGoalDate.trim().isEmpty()) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (assessmentTitle.equals(getIntent().getStringExtra(EXTRA_ASSESSMENT_TITLE)) &&
                assessmentType.equals(getIntent().getStringExtra(EXTRA_ASSESSMENT_TYPE)) &&
                assessmentGoalDate.equals(getIntent().getStringExtra(EXTRA_ASSESSMENT_GOAL_DATE)) &&
                assessmentGoalAlert == getIntent().getBooleanExtra(EXTRA_ASSESSMENT_GOAL_ALERT, false) &&
                assessmentAlertID == getIntent().getIntExtra(EXTRA_ASSESSMENT_ALERT_ID, -1) &&
                assessmentNotes.equals(getIntent().getStringExtra(EXTRA_ASSESSMENT_NOTES)) &&
                assessmentLinkedCourseID == getIntent().getIntExtra(EXTRA_ASSESSMENT_LINKED_COURSE_ID, -1)) {
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
            builder.setPositiveButton("SAVE", (dialogInterface, i) -> saveAssessment());
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void saveAssessment() {
        String assessmentTitle = editTextAssessmentTitle.getText().toString();
        String assessmentGoalDate = textViewAssessmentGoalDate.getText().toString();

        if (assessmentTitle.trim().isEmpty() || assessmentGoalDate.trim().isEmpty()) {
            if (assessmentTitle.trim().isEmpty()) {
                editTextAssessmentTitle.setHintTextColor(ContextCompat.getColor(AssessmentDetail.this, R.color.red));
            }
            if (assessmentGoalDate.trim().isEmpty()) {
                textViewAssessmentGoalDate.setHintTextColor(ContextCompat.getColor(AssessmentDetail.this, R.color.red));
            }
            Toast.makeText(AssessmentDetail.this, "A required field is empty.", Toast.LENGTH_LONG).show();
        } else {
            SharedPreferences sharedPreferences = this.getSharedPreferences(MainActivity.SHARED_PREFS_FILENAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if (assessmentAlertID == -1 && assessmentGoalAlert) {
                int i = sharedPreferences.getInt(MainActivity.SHARED_PREFS_ALERT_ID_COUNTER, -1);
                assessmentAlertID = i;
                editor.putInt(MainActivity.SHARED_PREFS_ALERT_ID_COUNTER, i + 1).apply();
                Date alertDate = null;
                try {
                    alertDate = MainActivity.sdf.parse(textViewAssessmentGoalDate.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String alertTitle = editTextAssessmentTitle.getText().toString() + " Completion Goal Today";
                String alertBody = editTextAssessmentTitle.getText().toString() + " was scheduled to be completed today.";
                createAlert(assessmentAlertID, alertDate, alertTitle, alertBody);
            } else if (assessmentAlertID > 0 && assessmentGoalAlert &&
                    !textViewAssessmentGoalDate.toString().equals(getIntent().getStringExtra(EXTRA_ASSESSMENT_GOAL_DATE))) {
                deleteAlert(assessmentAlertID);
                Date alertDate = null;
                try {
                    alertDate = MainActivity.sdf.parse(textViewAssessmentGoalDate.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String alertTitle = editTextAssessmentTitle.getText().toString() + " Completion Goal Today";
                String alertBody = editTextAssessmentTitle.getText().toString() + " was scheduled to be completed today.";
                createAlert(assessmentAlertID, alertDate, alertTitle, alertBody);
            } else if (assessmentAlertID > 0 && !assessmentGoalAlert) {
                deleteAlert(assessmentAlertID);
                assessmentAlertID = -1;
            }

            Intent intent = new Intent();
            intent.putExtra(EXTRA_ASSESSMENT_ID, assessmentID);
            intent.putExtra(EXTRA_ASSESSMENT_TITLE, assessmentTitle);
            intent.putExtra(EXTRA_ASSESSMENT_TYPE, assessmentType);
            intent.putExtra(EXTRA_ASSESSMENT_GOAL_DATE, assessmentGoalDate);
            intent.putExtra(EXTRA_ASSESSMENT_GOAL_ALERT, assessmentGoalAlert);
            intent.putExtra(EXTRA_ASSESSMENT_ALERT_ID, assessmentAlertID);
            intent.putExtra(EXTRA_ASSESSMENT_NOTES, assessmentNotes);
            intent.putExtra(EXTRA_ASSESSMENT_LINKED_COURSE_ID, assessmentLinkedCourseID);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    // creates a delete confirmation dialog and deletes course if confirmed
    private void deleteAssessment() {
        if (assessmentID == -1) {
            setResult(RESULT_CANCELED);
            finish();
            Toast.makeText(this, "New assessment creation cancelled.", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("CONFIRM ASSESSMENT DELETION")
                    .setMessage("Select CONFIRM to delete the assessment. Select ABORT to cancel.\n\n(records are not recoverable)")
                    .setCancelable(false);
            builder.setNegativeButton("ABORT", (dialogInterface, i) -> {
            });
            builder.setPositiveButton("CONFIRM", (dialogInterface, i) -> {
                if (assessmentGoalAlert && assessmentAlertID != -1) {
                    deleteAlert(assessmentAlertID);
                }
                Intent intent = new Intent();
                intent.putExtra(EXTRA_ASSESSMENT_ID, assessmentID);
                setResult(MainActivity.RESULT_DELETE, intent);
                finish();
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}