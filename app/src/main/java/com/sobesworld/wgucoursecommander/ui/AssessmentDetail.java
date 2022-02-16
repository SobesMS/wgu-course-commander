package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.Repository;
import com.sobesworld.wgucoursecommander.database.entity.AssessmentEntity;
import com.sobesworld.wgucoursecommander.database.entity.CourseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AssessmentDetail extends AppCompatActivity {

    private Repository repo;
    private boolean recordStatusNew;
    int assessmentID;
    EditText assessmentTitle;
    String assessmentType;
    EditText assessmentGoalDate;
    boolean assessmentGoalAlert;
    int assessmentAlertID;
    String assessmentNotes;
    int courseID;
    final Calendar goalCalendar = Calendar.getInstance();
    Spinner typeSpinner;
    Spinner courseSpinner;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
        recordStatusNew = getIntent().getBooleanExtra(getString(R.string.is_new_record), true);
        repo = new Repository(getApplication());
        assessmentID = getIntent().getIntExtra(getResources().getString(R.string.idnum), -1);

        // sets values of all fields upon record open
        assessmentTitle = findViewById(R.id.assessmentTitleEdit);
        assessmentGoalDate = findViewById(R.id.assessmentGoalDateEdit);
        assessmentTitle.setText(getIntent().getStringExtra(getResources().getString(R.string.title)));
        assessmentType = getIntent().getStringExtra(getResources().getString(R.string.assessment_type));
        assessmentGoalDate.setText(getIntent().getStringExtra(getResources().getString(R.string.completion_goal_date)));
        assessmentGoalAlert = getIntent().getBooleanExtra(getResources().getString(R.string.goal_alert), false);
        assessmentAlertID = getIntent().getIntExtra(getResources().getString(R.string.alert_id), -1);
        assessmentNotes = getIntent().getStringExtra(getResources().getString(R.string.notes));
        courseID = getIntent().getIntExtra(getResources().getString(R.string.courseID), -1);

        // sets assessment goal date from user's date picker selection
        DatePickerDialog.OnDateSetListener goalDateDialog = (datePicker, year, month, dayOfMonth) -> {
            goalCalendar.set(Calendar.YEAR, year);
            goalCalendar.set(Calendar.MONTH, month);
            goalCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            assessmentGoalDate.setText(sdf.format(goalCalendar.getTime()));
        };

        // onClickListener for the goal date field
        assessmentGoalDate.setOnClickListener(view -> {
            String info = assessmentGoalDate.getText().toString();
            if (!info.equals("")) {
                try {
                    goalCalendar.setTime(Objects.requireNonNull(sdf.parse(info)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            new DatePickerDialog(AssessmentDetail.this, goalDateDialog, goalCalendar.get(Calendar.YEAR),
                    goalCalendar.get(Calendar.MONTH), goalCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // notify switch toggle functionality
        SwitchCompat notifySwitch = findViewById(R.id.assessmentNotifySwitch);
        notifySwitch.setChecked(assessmentGoalAlert);
        notifySwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (assessmentGoalDate.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "You must set an end date before turning on notify.", Toast.LENGTH_LONG).show();
                notifySwitch.setChecked(assessmentGoalAlert);
            } else {
                assessmentGoalAlert = b;
            }
        });

        // set type spinner data
        typeSpinner = findViewById(R.id.assessmentType);
        List<String> typeOptions = new ArrayList<>();
        typeOptions.add("objective");
        typeOptions.add("performance");
        ArrayAdapter<String> typeSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeOptions);
        typeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeSpinnerAdapter);
        typeSpinner.setSelection(0);
        for (int i = 0; i < typeSpinnerAdapter.getCount(); i++) {
            if (typeSpinnerAdapter.getItem(i).equals(assessmentType)) {
                typeSpinner.setSelection(i);
            }
        }
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                assessmentType = (String) adapterView.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // set course spinner data
        courseSpinner = findViewById(R.id.linkedCourse);
        ArrayAdapter<CourseEntity> courseSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                repo.getAllCourses());
        courseSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(courseSpinnerAdapter);
        courseSpinner.setSelection(0);
        for (int i = 0; i < courseSpinnerAdapter.getCount(); i++) {
            if (courseSpinnerAdapter.getItem(i).getTermID() == courseID) {
                courseSpinner.setSelection(i);
            }
        }
        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CourseEntity course = (CourseEntity) adapterView.getSelectedItem();
                courseID = course.getTermID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // save button functionality
        Button saveButton = findViewById(R.id.assessmentSaveButton);
        saveButton.setOnClickListener(view -> {
            saveAssessment();
            Intent intent = new Intent(AssessmentDetail.this, AssessmentList.class);
            startActivity(intent);
        });

        // delete button functionality
        Button deleteButton = findViewById(R.id.assessmentDeleteButton);
        deleteButton.setOnClickListener( view -> {
            if (recordStatusNew) {
                Toast.makeText(getApplicationContext(), "New assessment entry. No saved record to delete.", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(AssessmentDetail.this, AssessmentList.class);
                deleteAssessment(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(AssessmentDetail.this, AssessmentList.class);
            applyUnsavedChanges(intent);
        }
        if (item.getItemId() == R.id.home_detail_menu) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            applyUnsavedChanges(intent);
        }
        if (item.getItemId() == R.id.note_detail_menu) {
            showNoteDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    // handles editing and sharing of notes
    void showNoteDialog() {
        final Dialog dialog = new Dialog(AssessmentDetail.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.note_dialog);
        final EditText note = dialog.findViewById(R.id.noteBody);
        note.setText(assessmentNotes);
        Button saveNoteButton = dialog.findViewById(R.id.saveNoteButton);
        Button shareNoteButton = dialog.findViewById(R.id.shareNoteButton);

        saveNoteButton.setOnClickListener(view -> {
            assessmentNotes = note.getText().toString();
            dialog.dismiss();
        });

        shareNoteButton.setOnClickListener(view -> {
            String title = assessmentTitle.getText().toString();
            String notes = title + " notes: " + note.getText().toString();
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

    // TODO: figure out why notifications don't work
    private void createAlert() {
        Date goalDate = null;
        try {
            goalDate=sdf.parse(assessmentGoalDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String title = assessmentTitle.getText().toString() + " Goal Today";
        String text = "Your goal to complete " + assessmentTitle.getText().toString() + " is today. Finish any remaining work.";
        long trigger = 0;
        if (goalDate != null) {
            trigger = goalDate.getTime();
        }
        Intent intent = new Intent(AssessmentDetail.this, CourseCommReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("text", text);
        intent.putExtra("id", assessmentAlertID);
        PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetail.this, assessmentAlertID, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
    }

    private void deleteAlert() {
        Intent intent = new Intent(AssessmentDetail.this, CourseCommReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetail.this, assessmentAlertID, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        sender.cancel();
    }

    private void applyUnsavedChanges(Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("COMMIT CHANGES")
                .setMessage("Click COMMIT to save any changes you have made. Click CANCEL to close this record without saving.")
                .setCancelable(false);
        builder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> startActivity(intent));
        builder.setPositiveButton(R.string.commit, (dialogInterface, i) -> {
            saveAssessment();
            startActivity(intent);
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void saveAssessment() {
        if (assessmentAlertID == -1 && assessmentGoalAlert) {
            int maxID = repo.getMaxAssessmentAlertID();
            if (maxID >= 20000) {
                assessmentAlertID = maxID + 1;
            } else {
                assessmentAlertID = 20000;
            }
            createAlert();
        } else if (assessmentAlertID > -1 && !assessmentGoalAlert) {
            deleteAlert();
            assessmentAlertID = -1;
        }
        if (recordStatusNew) {
            AssessmentEntity assessment = new AssessmentEntity(assessmentTitle.getText().toString(),
                    assessmentType, assessmentGoalDate.getText().toString(), assessmentGoalAlert, assessmentAlertID,
                    assessmentNotes, courseID);
            repo.insert(assessment);
            Toast.makeText(getApplicationContext(), "New assessment record created.", Toast.LENGTH_LONG).show();
        } else {
            AssessmentEntity assessment = new AssessmentEntity(assessmentID, assessmentTitle.getText().toString(),
                    assessmentType, assessmentGoalDate.getText().toString(), assessmentGoalAlert, assessmentAlertID,
                    assessmentNotes, courseID);
            repo.update(assessment);
            Toast.makeText(getApplicationContext(), "Assessment record updated.", Toast.LENGTH_LONG).show();
        }
    }

    // creates a delete confirmation dialog and deletes assessment if confirmed
    private void deleteAssessment(Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.assessment_delete_alert_title)
                .setMessage(R.string.assessment_delete_alert_message)
                .setCancelable(false);
        builder.setNegativeButton(R.string.abort, (dialogInterface, i) -> {
        });
        builder.setPositiveButton(R.string.confirm, (dialogInterface, i) -> {
            if (assessmentAlertID > -1) {
                deleteAlert();
            }
            repo.deleteAssessmentByID(assessmentID);
            Toast.makeText(getApplicationContext(), "Assessment record permanently deleted.", Toast.LENGTH_LONG).show();
            startActivity(intent);
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}