package com.sobesworld.wgucoursecommander.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.Observer;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.sobesworld.wgucoursecommander.database.entity.CourseEntity;
import com.sobesworld.wgucoursecommander.database.entity.TermEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CourseDetail extends AppCompatActivity {
    public static final String TAG = "CourseDetail";
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
    private SharedPreferences sharedPreferences;
    private ActivityResultLauncher<Intent> activityLauncher;

    private int requestID;
    private int courseID;
    private EditText editTextCourseTitle;
    private TextView textViewCourseStartDate;
    private TextView textViewCourseEndDate;
    private SwitchCompat switchCourseAlert;
    private Boolean courseEndAlert;
    private int courseAlertID;
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;
    private String courseStatus;
    private Spinner spinnerLinkedTerm;
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
        switchCourseAlert = findViewById(R.id.switch_course_alert);
        spinnerLinkedTerm = findViewById(R.id.spinner_linked_term);
        editTextCourseMentorsName = findViewById(R.id.edit_text_course_mentors_name);
        editTextCourseMentorsPhone = findViewById(R.id.edit_text_course_mentors_phone);
        editTextCourseMentorsEmail = findViewById(R.id.edit_text_course_mentors_email);

        Intent passedIntent = getIntent();
        requestID = passedIntent.getIntExtra(MainActivity.EXTRA_REQUEST_ID, -1);
        courseID = passedIntent.getIntExtra(EXTRA_COURSE_ID, -1);

        RecyclerView recyclerView = findViewById(R.id.course_assessment_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        assessmentAdapter = new AssessmentAdapter();
        recyclerView.setAdapter(assessmentAdapter);

        AssessmentViewModel assessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);
        assessmentViewModel.getLinkedAssessments(courseID).observe(this, new Observer<List<AssessmentEntity>>() {
            @Override
            public void onChanged(List<AssessmentEntity> assessmentEntities) {
                assessmentAdapter.submitList(assessmentEntities);
            }
        });

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (passedIntent.getIntExtra(MainActivity.EXTRA_REQUEST_ID, -1) == 1) {
            setTitle("Add Course");
        } else {
            setTitle("Edit Course");
            editTextCourseTitle.setText(passedIntent.getStringExtra(EXTRA_COURSE_TITLE));
            textViewCourseStartDate.setText(passedIntent.getStringExtra(EXTRA_COURSE_START_DATE));
            textViewCourseEndDate.setText(passedIntent.getStringExtra(EXTRA_COURSE_END_DATE));
            switchCourseAlert.setChecked(passedIntent.getBooleanExtra(EXTRA_COURSE_END_ALERT, false);
            editTextCourseMentorsName.setText(passedIntent.getStringExtra(EXTRA_COURSE_MENTORS_NAME));
            editTextCourseMentorsPhone.setText(passedIntent.getStringExtra(EXTRA_COURSE_MENTORS_PHONE));
            editTextCourseMentorsEmail.setText(passedIntent.getStringExtra(EXTRA_COURSE_MENTORS_EMAIL));
        }

        // sets course start date
        textViewCourseStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });

        startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                textViewCourseStartDate.setText(MainActivity.sdf.format(calendar.getTime()));
            }
        };

        // sets course end date
        textViewCourseEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });

        endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                textViewCourseEndDate.setText(MainActivity.sdf.format(calendar.getTime()));
            }
        };

        // alert switch toggle functionality
        switchCourseAlert.setChecked(courseEndAlert);
        switchCourseAlert.setOnCheckedChangeListener((compoundButton, b) -> {
            if (textViewCourseEndDate.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "You must set an end date before turning on notify.", Toast.LENGTH_LONG).show();
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
        spinnerCourseStatus.setSelection(0);
        for (int i = 0; i < statusSpinnerAdapter.getCount(); i++) {
            if (statusSpinnerAdapter.getItem(i).equals(courseStatus)) {
                spinnerCourseStatus.setSelection(i);
            }
        }
        spinnerCourseStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                courseStatus = (String) adapterView.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // set term spinner data
        spinnerLinkedTerm = findViewById(R.id.spinner_linked_term);
        TermViewModel termViewModel = new ViewModelProvider(this).get(TermViewModel.class);
        termViewModel.getAllTerms().observe(this, new Observer<List<TermEntity>>() {
            @Override
            public void onChanged(List<TermEntity> termEntities) {
                ArrayAdapter<TermEntity> termSpinnerAdapter = new ArrayAdapter<TermEntity>(CourseDetail.this,
                        android.R.layout.simple_spinner_item, termEntities);
                termSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerLinkedTerm.setAdapter(termSpinnerAdapter);
                spinnerLinkedTerm.setSelection(0);
                for (int i = 0; i < termSpinnerAdapter.getCount(); i++) {
                    if (termSpinnerAdapter.getItem(i).getTermID() == courseLinkedTermID) {
                        spinnerLinkedTerm.setSelection(i);
                    }
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
        imageViewCourseAddAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseDetail.this, AssessmentDetail.class);
                intent.putExtra(MainActivity.EXTRA_REQUEST_ID, MainActivity.REQUEST_ADD_FROM_DETAIL);
                activityLauncher.launch(intent);
            }
        });

        activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
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
                                if (assessmentID == -1) {
                                    AssessmentEntity assessmentEntity = new AssessmentEntity(assessmentTitle, assessmentType, assessmentGoalDate,
                                            assessmentGoalAlert, assessmentAlertID, assessmentNotes, assessmentLinkedCourseID);
                                    assessmentViewModel.insert(assessmentEntity);
                                    Toast.makeText(CourseDetail.this, "Assessment added.", Toast.LENGTH_SHORT).show();
                                } else {
                                    AssessmentEntity assessmentEntity = new AssessmentEntity(assessmentTitle, assessmentType, assessmentGoalDate,
                                            assessmentGoalAlert, assessmentAlertID, assessmentNotes, assessmentLinkedCourseID);
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
                }
        );
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_assessment_detail_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent;
            if (requestID == MainActivity.REQUEST_ADD_FROM_LIST) {
                intent = new Intent(CourseDetail.this, CourseList.class);
            } else if (requestID == MainActivity.REQUEST_ADD_FROM_DETAIL) {
                intent = new Intent(CourseDetail.this, TermDetail.class);
            } else {
                intent = new Intent(CourseDetail.this, MainActivity.class);
                Toast.makeText(this, "Can't determine previous page. Returning to home.", Toast.LENGTH_SHORT).show();
            }
            applyUnsavedChanges(intent);
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
        final EditText note = dialog.findViewById(R.id.noteBody);
        note.setText(courseNotes);
        Button saveNoteButton = dialog.findViewById(R.id.saveNoteButton);
        Button shareNoteButton = dialog.findViewById(R.id.shareNoteButton);

        saveNoteButton.setOnClickListener(view -> {
            courseNotes = note.getText().toString();
            dialog.dismiss();
        });

        shareNoteButton.setOnClickListener(view -> {
            String title = editTextCourseTitle.getText().toString();
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

    private void deleteAlert(int id) {
        Intent intent = new Intent(CourseDetail.this, CourseCommReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(CourseDetail.this, id, intent, 0);
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
            saveCourse();
            startActivity(intent);
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void saveCourse() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!sharedPreferences.contains(getResources().getString(R.string.uniqueID))) {
            editor.putInt(getResources().getString(R.string.uniqueID), 100).apply();
        }
        if (courseAlertID == -1 && courseEndAlert) {
            int i = sharedPreferences.getInt(getResources().getString(R.string.uniqueID), -1);
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
            CourseEntity course = new CourseEntity(editTextCourseTitle.getText().toString(), textViewCourseStartDate.getText().toString(),
                    textViewCourseEndDate.getText().toString(), courseEndAlert, courseAlertID, courseStatus,
                    editTextCourseMentorsName.getText().toString(), editTextCourseMentorsPhone.getText().toString(),
                    editTextCourseMentorsEmail.getText().toString(), courseNotes, courseLinkedTermID);
            repo.insert(course);
            Toast.makeText(getApplicationContext(), "New course record created.", Toast.LENGTH_LONG).show();
        } else {
            CourseEntity course = new CourseEntity(courseID, editTextCourseTitle.getText().toString(), textViewCourseStartDate.getText().toString(),
                    textViewCourseEndDate.getText().toString(), courseEndAlert, courseAlertID, courseStatus,
                    editTextCourseMentorsName.getText().toString(), editTextCourseMentorsPhone.getText().toString(),
                    editTextCourseMentorsEmail.getText().toString(), courseNotes, courseLinkedTermID);
            repo.update(course);
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
            repo.deleteCourseByID(courseID);
            repo.deleteLinkedAssessments(courseID);
            Toast.makeText(getApplicationContext(), "Course record permanently deleted.", Toast.LENGTH_LONG).show();
            startActivity(intent);
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}