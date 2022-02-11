package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.Repository;
import com.sobesworld.wgucoursecommander.database.adapters.CourseAdapter;
import com.sobesworld.wgucoursecommander.database.entity.TermEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class TermDetail extends AppCompatActivity {

    private Repository repo;
    private AlertDialog deleteDialog;
    private boolean recordStatusNew;
    EditText termTitle;
    EditText termStartDate;
    EditText termEndDate;
    EditText termNotes;
    final Calendar startCalendar = Calendar.getInstance();
    final Calendar endCalendar = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        recordStatusNew = getIntent().getBooleanExtra(getString(R.string.is_new_record), true);
        repo = new Repository(getApplication());
        CourseAdapter adapter = new CourseAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.termCourseList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // shows
        if (!recordStatusNew) {
            int i = getIntent().getIntExtra(getResources().getString(R.string.idnum), -1);
            adapter.setCourses(repo.getLinkedCourses(i));
        }

        termTitle = findViewById(R.id.termTitleEdit);
        termTitle.setText(getIntent().getStringExtra(getResources().getString(R.string.title)));
        termStartDate = findViewById(R.id.termStartDateEdit);
        termStartDate.setText(getIntent().getStringExtra(getResources().getString(R.string.start_date)));
        termEndDate = findViewById(R.id.termEndDateEdit);
        termEndDate.setText(getIntent().getStringExtra(getResources().getString(R.string.end_date)));
        termNotes = findViewById(R.id.termNoteEdit);
        termNotes.setText(getIntent().getStringExtra(getResources().getString(R.string.notes)));

        // sets term end date from user's date picker selection
        DatePickerDialog.OnDateSetListener startDateDialog = (datePicker, year, month, dayOfMonth) -> {
            startCalendar.set(Calendar.YEAR, year);
            startCalendar.set(Calendar.MONTH, month);
            startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            termStartDate.setText(sdf.format(startCalendar.getTime()));
        };

        // onClickListener for the term start date field
        termStartDate.setOnClickListener(view -> {
            String info = termStartDate.getText().toString();
            if (!info.equals("")) {
                try {
                    startCalendar.setTime(Objects.requireNonNull(sdf.parse(info)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            new DatePickerDialog(TermDetail.this, startDateDialog, startCalendar.get(Calendar.YEAR),
                    startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // sets term end date from user's date picker selection
        DatePickerDialog.OnDateSetListener endDateDialog = (datePicker, year, month, dayOfMonth) -> {
            endCalendar.set(Calendar.YEAR, year);
            endCalendar.set(Calendar.MONTH, month);
            endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            termEndDate.setText(sdf.format(endCalendar.getTime()));
        };

        // onClickListener for the term start date field
        termEndDate.setOnClickListener(view -> {
            String info = termEndDate.getText().toString();
            if (!info.equals("")) {
                try {
                    endCalendar.setTime(Objects.requireNonNull(sdf.parse(info)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            new DatePickerDialog(TermDetail.this, endDateDialog, endCalendar.get(Calendar.YEAR),
                    endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        Button saveButton = findViewById(R.id.termSaveButton);
        saveButton.setOnClickListener(view -> {
            if (recordStatusNew) {
                TermEntity term = new TermEntity(termTitle.getText().toString(), termStartDate.getText().toString(),
                        termEndDate.getText().toString(), termNotes.getText().toString());
                repo.insert(term);
                finish();
                Toast toast = Toast.makeText(getApplicationContext(), "New term record created.", Toast.LENGTH_LONG);
                toast.show();
            } else {
                TermEntity term = new TermEntity(getIntent().getIntExtra(getResources().getString(R.string.idnum), -1),
                        termTitle.getText().toString(), termStartDate.getText().toString(), termEndDate.getText().toString(),
                        termNotes.getText().toString());
                repo.update(term);
                finish();
                Toast toast = Toast.makeText(getApplicationContext(), "Term record updated.", Toast.LENGTH_LONG);
                toast.show();
            }
        });
        
        Button deleteButton = findViewById(R.id.termDeleteButton);
        deleteButton.setOnClickListener( view -> {
            if (recordStatusNew) {
                Toast toast = Toast.makeText(getApplicationContext(), "New term entry. No saved record to delete.", Toast.LENGTH_LONG);
                toast.show();
            } else {
                deleteTerm();
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
        if (item.getItemId() == R.id.share_detail_menu) {
            Toast toast = Toast.makeText(getApplicationContext(), "Hi, you just pressed share!", Toast.LENGTH_LONG);
            toast.show();
        }
        if (item.getItemId() == R.id.note_detail_menu) {
            Toast toast = Toast.makeText(getApplicationContext(), "Hi, you just pressed note!", Toast.LENGTH_LONG);
            toast.show();
        }
        return super.onOptionsItemSelected(item);
    }

    // creates a delete confirmation dialog and deletes course if confirmed
    private void deleteTerm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.term_delete_alert_title).setMessage(R.string.term_delete_alert_message);
        builder.setNegativeButton(R.string.abort, (dialogInterface, i) -> {
        });
        builder.setPositiveButton(R.string.confirm, (dialogInterface, i) -> {
            TermEntity term = new TermEntity(getIntent().getIntExtra(getResources().getString(R.string.idnum), -1),
                    termTitle.getText().toString(), termStartDate.getText().toString(), termEndDate.getText().toString(),
                    termNotes.getText().toString());
            repo.delete(term);
            repo.deleteLinkedCourses(getIntent().getIntExtra(getResources().getString(R.string.idnum), -1));
            Toast toast = Toast.makeText(getApplicationContext(), "Term record permanently deleted.", Toast.LENGTH_LONG);
            toast.show();
            finish();
        });
        deleteDialog = builder.create();
    }
}