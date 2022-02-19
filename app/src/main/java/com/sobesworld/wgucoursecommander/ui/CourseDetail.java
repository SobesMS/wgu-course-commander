package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModel;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.AssessmentListAdapter;
import com.sobesworld.wgucoursecommander.database.AssessmentViewModel;
import com.sobesworld.wgucoursecommander.database.CourseListAdapter;
import com.sobesworld.wgucoursecommander.database.CourseViewModel;
import com.sobesworld.wgucoursecommander.database.Repository;
import com.sobesworld.wgucoursecommander.database.TermListAdapter;
import com.sobesworld.wgucoursecommander.database.TermViewModel;
import com.sobesworld.wgucoursecommander.database.entity.CourseEntity;
import com.sobesworld.wgucoursecommander.database.entity.TermEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CourseDetail extends AppCompatActivity {

    private CourseViewModel mCourseViewModel;
    private boolean recordStatusNew;
    private boolean fromTermDetail;
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
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
        recordStatusNew = getIntent().getBooleanExtra(getString(R.string.is_new_record), true);
        fromTermDetail = getIntent().getBooleanExtra(getResources().getString(R.string.from_term_detail), false);
        courseID = getIntent().getIntExtra(getResources().getString(R.string.idnum), -1);
        sp = getSharedPreferences("com.sobesworld.wgucoursecommander.prefs", Context.MODE_PRIVATE);
        mCourseViewModel = new ViewModelProvider(CourseDetail.this).get(CourseViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.courseAssessmentList);
        final AssessmentListAdapter adapter = new AssessmentListAdapter(new AssessmentListAdapter.AssessmentDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(CourseDetail.this));
        AssessmentViewModel mAssessmentViewModel = new ViewModelProvider(CourseDetail.this).get(AssessmentViewModel.class);
        mAssessmentViewModel.getLinkedAssessments(courseID).observe(CourseDetail.this, adapter::submitList);

        // sets values of all fields upon record open
        courseTitle = findViewById(R.id.courseTitleEdit);
        courseStartDate = findViewById(R.id.courseStartDateEdit);
        courseEndDate = findViewById(R.id.courseEndDateEdit);
        courseMentorsName = findViewById(R.id.mentorNameEdit);
        courseMentorsPhone = findViewById(R.id.mentorPhoneEdit);
        courseMentorsEmail = findViewById(R.id.mentorEmailEdit);
        courseTitle.setText(getIntent().getStringExtra(getResources().getString(R.string.title)));
        courseStartDate.setText(getIntent().getStringExtra(getResources().getString(R.string.start_date)));
        courseEndDate.setText(getIntent().getStringExtra(getResources().getString(R.string.end_date)));
        courseEndAlert = getIntent().getBooleanExtra(getResources().getString(R.string.end_alert), false);
        courseAlertID = getIntent().getIntExtra(getResources().getString(R.string.alert_id), -1);
        courseStatus = getIntent().getStringExtra(getResources().getString(R.string.status));
        courseMentorsName.setText(getIntent().getStringExtra(getResources().getString(R.string.mentor)));
        courseMentorsPhone.setText(getIntent().getStringExtra(getResources().getString(R.string.phone)));
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
            if (courseEndDate.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "You must set an end date before turning on notify.", Toast.LENGTH_LONG).show();
                notifySwitch.setChecked(courseEndAlert);
            } else {
                courseEndAlert = b;
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
        ArrayAdapter<TermEntity> termSpinnerAdapter = new ArrayAdapter<>(CourseDetail.this, android.R.layout.simple_spinner_item);
        TermViewModel mTermViewModel = new ViewModelProvider(CourseDetail.this).get(TermViewModel.class);
        mTermViewModel.getAllTerms().observe(CourseDetail.this, termSpinnerAdapter::addAll);
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
            saveCourse();
            Intent intent = new Intent(CourseDetail.this, CourseList.class);
            startActivity(intent);
        });

        // delete button functionality
        Button deleteButton = findViewById(R.id.courseDeleteButton);
        deleteButton.setOnClickListener( view -> {
            if (recordStatusNew) {
                Toast.makeText(getApplicationContext(), "New course entry. No saved record to delete.", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(CourseDetail.this, CourseList.class);
                deleteCourse(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(CourseDetail.this, CourseList.class);
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

    private void createAlert() {
        Date endDate = null;
        try {
            endDate=sdf.parse(courseEndDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String title = courseTitle.getText().toString() + " Ending Today";
        String text = courseTitle.getText().toString() + " was scheduled to be completed today. Finish and submit any remaining assessments.";
        long trigger = 0;
        if (endDate != null) {
            trigger = endDate.getTime();
        }
        Intent intent = new Intent(CourseDetail.this, CourseCommReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("text", text);
        intent.putExtra("id", courseAlertID);
        PendingIntent sender = PendingIntent.getBroadcast(CourseDetail.this, courseAlertID, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
    }

    private void deleteAlert(int id) {
        Intent intent = new Intent(CourseDetail.this, CourseCommReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(CourseDetail.this, id, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        sender.cancel();
    }

    private void applyUnsavedChanges(Intent intent) {
        if (true) {
            if (fromTermDetail) {
                finish();
            } else {
                startActivity(intent); // TODO: apply unsaved changes method
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("COMMIT CHANGES")
                    .setMessage("Click COMMIT to save any changes you have made. Click CANCEL to close this record without saving.")
                    .setCancelable(false);
            builder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                if (getIntent().getBooleanExtra(getResources().getString(R.string.from_term_detail), false)) {
                    finish();
                } else {
                    startActivity(intent);
                }
            });
            builder.setPositiveButton(R.string.commit, (dialogInterface, i) -> {
                if (getIntent().getBooleanExtra(getResources().getString(R.string.from_term_detail), false)) {
                    saveCourse();
                    finish();
                } else {
                    saveCourse();
                    startActivity(intent);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void saveCourse() {
        SharedPreferences.Editor editor = sp.edit();
        if (!sp.contains(getResources().getString(R.string.uniqueID))) {
            editor.putInt(getResources().getString(R.string.uniqueID), 100).apply();
        }
        if (courseAlertID == -1 && courseEndAlert) {
            int i = sp.getInt(getResources().getString(R.string.uniqueID), -1);
            if (i != -1) {
                courseAlertID = i + 1;
                editor.putInt(getResources().getString(R.string.uniqueID), courseAlertID).apply();
                createAlert();
            }
        } else if (courseAlertID > 0 && !courseEndAlert) {
            deleteAlert(courseAlertID);
            courseAlertID = -1;
        }
        if (recordStatusNew) {
            CourseEntity course = new CourseEntity(courseTitle.getText().toString(), courseStartDate.getText().toString(),
                    courseEndDate.getText().toString(), courseEndAlert, courseAlertID, courseStatus,
                    courseMentorsName.getText().toString(), courseMentorsPhone.getText().toString(),
                    courseMentorsEmail.getText().toString(), courseNotes, termID);
            mCourseViewModel.insert(course);
            Toast.makeText(getApplicationContext(), "New course record created.", Toast.LENGTH_LONG).show();
        } else {
            CourseEntity course = new CourseEntity(courseID, courseTitle.getText().toString(), courseStartDate.getText().toString(),
                    courseEndDate.getText().toString(), courseEndAlert, courseAlertID, courseStatus,
                    courseMentorsName.getText().toString(), courseMentorsPhone.getText().toString(),
                    courseMentorsEmail.getText().toString(), courseNotes, termID);
            mCourseViewModel.update(course);
            Toast.makeText(getApplicationContext(), "Course record updated.", Toast.LENGTH_LONG).show();
        }
    }

    // creates a delete confirmation dialog and deletes course if confirmed
    private void deleteCourse(Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.course_delete_alert_title)
                .setMessage(R.string.course_delete_alert_message)
                .setCancelable(false);
        builder.setNegativeButton(R.string.abort, (dialogInterface, i) -> {
        });
        builder.setPositiveButton(R.string.confirm, (dialogInterface, i) -> {
            if (courseAlertID > -1) {
                deleteAlert(courseAlertID);
            }
            //repo.deleteCourseByID(courseID);
            mCourseViewModel.deleteTermByID(courseID);
            //repo.deleteLinkedAssessments(courseID); TODO: add delete linked assessments method
            Toast.makeText(getApplicationContext(), "Course record permanently deleted.", Toast.LENGTH_LONG).show();
            startActivity(intent);
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /*private void fillRecyclerView() {
        AssessmentAdapter adapter = new AssessmentAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.courseAssessmentList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (!recordStatusNew) {
            adapter.setAssessments(repo.getLinkedAssessments(courseID));
        }
    }*/
}