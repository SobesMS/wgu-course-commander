package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.CourseViewModel;
import com.sobesworld.wgucoursecommander.database.adapters.CourseAdapter;
import com.sobesworld.wgucoursecommander.database.entity.CourseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TermDetail extends AppCompatActivity {
    public static final String TAG = "TermDetail";
    public static final String EXTRA_TERM_ID = "com.sobesworld.wgucoursecommander.EXTRA_TERM_ID";
    public static final String EXTRA_TERM_TITLE = "com.sobesworld.wgucoursecommander.EXTRA_TERM_TITLE";
    public static final String EXTRA_TERM_START_DATE = "com.sobesworld.wgucoursecommander.EXTRA_TERM_START_DATE";
    public static final String EXTRA_TERM_END_DATE = "com.sobesworld.wgucoursecommander.EXTRA_TERM_END_DATE";
    public static final String EXTRA_REQUEST_ID = "com.sobesworld.wgucoursecommander.EXTRA_REQUEST_ID";
    public static final int REQUEST_ADD_COURSE_FROM_TERM = 13;
    public static final int RESULT_TERM_DELETE = 14;
    static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
    private CourseViewModel courseViewModel;
    private CourseAdapter courseAdapter;

    private int termID;
    private EditText editTextTermTitle;
    private TextView textViewTermStartDate;
    private TextView textViewTermEndDate;
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

        editTextTermTitle = findViewById(R.id.edit_text_term_title);
        textViewTermStartDate = findViewById(R.id.text_view_term_start_date);
        textViewTermEndDate = findViewById(R.id.text_view_end_date);

        Intent passedIntent = getIntent();
        termID = passedIntent.getIntExtra(EXTRA_TERM_ID, -1);

        RecyclerView recyclerView = findViewById(R.id.term_course_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        courseAdapter = new CourseAdapter();
        recyclerView.setAdapter(courseAdapter);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        courseViewModel.getLinkedCourses(termID).observe(this, new Observer<List<CourseEntity>>() {
            @Override
            public void onChanged(List<CourseEntity> courseEntities) {
                courseAdapter.submitList(courseEntities);
            }
        });

        courseAdapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CourseEntity courseEntity) {
                /*Intent intent = new Intent(TermDetail.this, CourseDetail.class);
                intent.putExtra(EXTRA_REQUEST_ID, REQUEST_EDIT_TERM);
                intent.putExtra(TermDetail.EXTRA_TERM_ID, termEntity.getTermID());
                intent.putExtra(TermDetail.EXTRA_TERM_TITLE, termEntity.getTermTitle());
                intent.putExtra(TermDetail.EXTRA_TERM_START_DATE, termEntity.getTermStartDate());
                intent.putExtra(TermDetail.EXTRA_TERM_END_DATE, termEntity.getTermEndDate());
                activityLauncher.launch(intent);*/
            }
        });

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (passedIntent.getIntExtra(TermList.EXTRA_REQUEST_ID, -1) == 11) {
            setTitle("Add Term");
        } else {
            setTitle("Edit Term");
            editTextTermTitle.setText(passedIntent.getStringExtra(EXTRA_TERM_TITLE));
            textViewTermStartDate.setText(passedIntent.getStringExtra(EXTRA_TERM_START_DATE));
            textViewTermEndDate.setText(passedIntent.getStringExtra(EXTRA_TERM_END_DATE));
        }

        textViewTermStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                String date = textViewTermStartDate.getText().toString();
                if (!date.trim().isEmpty()) {
                    try {
                        calendar.setTime(Objects.requireNonNull(sdf.parse(date)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                new DatePickerDialog(TermDetail.this, startDateSetListener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                textViewTermStartDate.setText(sdf.format(calendar.getTime()));
            }
        };

        textViewTermEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                String date = textViewTermEndDate.getText().toString();
                if (!date.trim().isEmpty()) {
                    try {
                        calendar.setTime(Objects.requireNonNull(sdf.parse(date)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                new DatePickerDialog(TermDetail.this, endDateSetListener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                textViewTermEndDate.setText(sdf.format(calendar.getTime()));
            }
        };
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

        if (getIntent().getIntExtra(TermList.EXTRA_REQUEST_ID, -1) == 11 && termTitle.trim().isEmpty()
                && termStartDate.trim().isEmpty() && termEndDate.trim().isEmpty()) {
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
            builder.setPositiveButton("SAVE", (dialogInterface, i) -> {
                saveTerm();
            });
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
                        .setMessage("Select CONFIRM to delete the term.\nSelect ABORT to cancel.\n\n(records are not recoverable)")
                        .setCancelable(false);
                builder.setNegativeButton("ABORT", (dialogInterface, i) -> {
                    setResult(RESULT_CANCELED);
                    finish();
                });
                builder.setPositiveButton("CONFIRM", (dialogInterface, i) -> {
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_TERM_ID, termID);
                    setResult(RESULT_TERM_DELETE, intent);
                    finish();
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }
}