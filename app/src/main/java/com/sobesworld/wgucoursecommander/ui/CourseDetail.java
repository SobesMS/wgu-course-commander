package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import com.sobesworld.wgucoursecommander.database.adapters.AssessmentAdapter;
import com.sobesworld.wgucoursecommander.database.entity.CourseEntity;
import com.sobesworld.wgucoursecommander.database.entity.TermEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CourseDetail extends AppCompatActivity {

    private Repository repo;
    private AlertDialog deleteDialog;
    private boolean recordStatusNew;
    int courseID;
    EditText courseTitle;
    EditText courseStartDate;
    EditText courseEndDate;
    Boolean courseEndAlert;
    int courseAlertID;
    String courseStatus;
    EditText courseMentorsName;
    EditText courseMentorsPhone;
    EditText courseMentorsEmail;
    String courseNotes;
    int termID;
    final Calendar startCalendar = Calendar.getInstance();
    final Calendar endCalendar = Calendar.getInstance();
    Spinner statusSpinner;
    Spinner termSpinner;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        recordStatusNew = getIntent().getBooleanExtra(getString(R.string.is_new_record), true);
        repo = new Repository(getApplication());
        AssessmentAdapter adapter = new AssessmentAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.courseAssessmentList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseID = getIntent().getIntExtra(getResources().getString(R.string.idnum), -1);

        // displays assessment list for current course
        if (!recordStatusNew) {
            adapter.setAssessments(repo.getLinkedAssessments(courseID));
        }

        courseTitle = findViewById(R.id.courseTitleEdit);
        courseTitle.setText(getIntent().getStringExtra(getResources().getString(R.string.title)));
        courseStartDate = findViewById(R.id.courseStartDateEdit);
        courseStartDate.setText(getIntent().getStringExtra(getResources().getString(R.string.start_date)));
        courseEndDate = findViewById(R.id.courseEndDateEdit);
        courseEndDate.setText(getIntent().getStringExtra(getResources().getString(R.string.end_date)));
        courseEndAlert = getIntent().getBooleanExtra(getResources().getString(R.string.end_alert), false);
        // TODO: courseAlertID from notification method
        //courseAlertID = findViewById();
        courseAlertID = getIntent().getIntExtra(getResources().getString(R.string.alert_id), -1);
        courseStatus = getIntent().getStringExtra(getResources().getString(R.string.status));
        courseMentorsName = findViewById(R.id.mentorNameEdit);
        courseMentorsName.setText(getIntent().getStringExtra(getResources().getString(R.string.mentor)));
        courseMentorsPhone = findViewById(R.id.mentorPhoneEdit);
        courseMentorsPhone.setText(getIntent().getStringExtra(getResources().getString(R.string.email)));
        courseMentorsEmail = findViewById(R.id.mentorEmailEdit);
        courseMentorsEmail.setText(getIntent().getStringExtra(getResources().getString(R.string.email)));
        courseNotes = getIntent().getStringExtra(getResources().getString(R.string.notes));
        termID = getIntent().getIntExtra(getResources().getString(R.string.termID), -1);

        // sets course start date from user's date picker selection
        DatePickerDialog.OnDateSetListener startDateDialog = (datePicker, year, month, dayOfMonth) -> {
            startCalendar.set(Calendar.YEAR, year);
            startCalendar.set(Calendar.MONTH, month);
            startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            courseStartDate.setText(sdf.format(startCalendar.getTime()));
        };

        // onClickListener for the course start date field
        courseStartDate.setOnClickListener(view -> {
            String info = courseStartDate.getText().toString();
            if (!info.equals("")) {
                try {
                    startCalendar.setTime(Objects.requireNonNull(sdf.parse(info)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            new DatePickerDialog(CourseDetail.this, startDateDialog, startCalendar.get(Calendar.YEAR),
                    startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // sets course end date from user's date picker selection
        DatePickerDialog.OnDateSetListener endDateDialog = (datePicker, year, month, dayOfMonth) -> {
            endCalendar.set(Calendar.YEAR, year);
            endCalendar.set(Calendar.MONTH, month);
            endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            courseEndDate.setText(sdf.format(endCalendar.getTime()));
        };

        // onClickListener for the course end date field
        courseEndDate.setOnClickListener(view -> {
            String info = courseEndDate.getText().toString();
            if (!info.equals("")) {
                try {
                    endCalendar.setTime(Objects.requireNonNull(sdf.parse(info)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            new DatePickerDialog(CourseDetail.this, endDateDialog, endCalendar.get(Calendar.YEAR),
                    endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // notify switch toggle functionality
        SwitchCompat notifySwitch = findViewById(R.id.courseNotifySwitch);
        notifySwitch.setChecked(courseEndAlert);
        notifySwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                courseEndAlert = true;
                // TODO build notification method
            } else {
                courseEndAlert = false;
            }
        });

        // set status spinner data
        statusSpinner = findViewById(R.id.courseStatus);
        List<String> statusOptions = new ArrayList<>();
        statusOptions.add("in progress");
        statusOptions.add("plan to take");
        statusOptions.add("completed");
        statusOptions.add("dropped");
        ArrayAdapter<String> statusSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, statusOptions);
        statusSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusSpinnerAdapter);
        statusSpinner.setSelection(0);
        for (int i = 0; i < statusSpinnerAdapter.getCount(); i++) {
            if (statusSpinnerAdapter.getItem(i).equals(courseStatus)) {
                statusSpinner.setSelection(i);
            }
        }
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                courseStatus = (String) adapterView.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // set term spinner data
        termSpinner = findViewById(R.id.linkedTerm);
        ArrayAdapter<TermEntity> termSpinnerAdapter = new ArrayAdapter<TermEntity>(this, android.R.layout.simple_spinner_item,
                repo.getAllTerms());
        termSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        termSpinner.setAdapter(termSpinnerAdapter);
        termSpinner.setSelection(0);
        for (int i = 0; i < termSpinnerAdapter.getCount(); i++) {
            if (termSpinnerAdapter.getItem(i).getTermID() == termID) {
                termSpinner.setSelection(i);
            }
        }
        termSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TermEntity term = (TermEntity) adapterView.getSelectedItem();
                termID = term.getTermID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // save button functionality
        Button saveButton = findViewById(R.id.courseSaveButton);
        saveButton.setOnClickListener(view -> {
            if (recordStatusNew) {
                // TODO: ensure courseEndAlert and courseAlertID are accurate
                CourseEntity course = new CourseEntity(courseTitle.getText().toString(), courseStartDate.getText().toString(),
                        courseEndDate.getText().toString(), courseEndAlert, courseAlertID, courseStatus,
                        courseMentorsName.getText().toString(), courseMentorsPhone.getText().toString(),
                        courseMentorsEmail.getText().toString(), courseNotes, termID);
                repo.insert(course);
                finish();
                Toast.makeText(getApplicationContext(), "New course record created.", Toast.LENGTH_LONG).show();
            } else {
                // TODO: ensure courseEndAlert and courseAlertID are accurate
                CourseEntity course = new CourseEntity(courseID, courseTitle.getText().toString(), courseStartDate.getText().toString(),
                        courseEndDate.getText().toString(), courseEndAlert, courseAlertID, courseStatus,
                        courseMentorsName.getText().toString(), courseMentorsPhone.getText().toString(),
                        courseMentorsEmail.getText().toString(), courseNotes, termID);
                repo.update(course);
                finish();
                Toast.makeText(getApplicationContext(), "Course record updated.", Toast.LENGTH_LONG).show();
            }
        });

        // delete button functionality
        Button deleteButton = findViewById(R.id.courseDeleteButton);
        deleteButton.setOnClickListener( view -> {
            if (recordStatusNew) {
                Toast.makeText(getApplicationContext(), "New course entry. No saved record to delete.", Toast.LENGTH_LONG).show();
            } else {
                deleteCourse();
                deleteDialog.show();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home_detail_menu) {
            Intent homeButton = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(homeButton);
        }
        if (item.getItemId() == R.id.note_detail_menu) {
            if (recordStatusNew) {
                Toast.makeText(getApplicationContext(),
                        "You are adding a new course. Course Record must be saved before creating notes.", Toast.LENGTH_LONG).show();
            } else {
                showNoteDialog();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // handles editing and sharing of notes
    void showNoteDialog() {
        final Dialog dialog = new Dialog(CourseDetail.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.note_dialog);
        final EditText note = dialog.findViewById(R.id.noteBody);
        note.setText(courseNotes);
        Button saveNoteButton = dialog.findViewById(R.id.saveNoteButton);
        Button shareNoteButton = dialog.findViewById(R.id.shareNoteButton);

        saveNoteButton.setOnClickListener(view -> {
            courseNotes = note.getText().toString();
            repo.updateCourseNotes(courseNotes, courseID);
            dialog.dismiss();
        });

        shareNoteButton.setOnClickListener(view -> {
            String title = courseTitle.getText().toString();
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

    // creates a delete confirmation dialog and deletes course if confirmed
    private void deleteCourse() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.course_delete_alert_title).setMessage(R.string.course_delete_alert_message);
        builder.setNegativeButton(R.string.abort, (dialogInterface, i) -> {
        });
        builder.setPositiveButton(R.string.confirm, (dialogInterface, i) -> {
            // TODO: ensure courseEndAlert, courseAlertID, courseStatus, and termID are accurate
            CourseEntity course = new CourseEntity(courseID, courseTitle.getText().toString(), courseStartDate.getText().toString(),
                    courseEndDate.getText().toString(), courseEndAlert, courseAlertID, courseStatus,
                    courseMentorsName.getText().toString(), courseMentorsPhone.getText().toString(),
                    courseMentorsEmail.getText().toString(), courseNotes, termID);
            repo.delete(course);
            repo.deleteLinkedAssessments(courseID);
            Toast.makeText(getApplicationContext(), "Course record permanently deleted.", Toast.LENGTH_LONG).show();
            finish();
        });
        deleteDialog = builder.create();
    }
}