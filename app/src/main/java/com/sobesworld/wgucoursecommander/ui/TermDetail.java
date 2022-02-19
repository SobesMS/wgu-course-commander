package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.CourseListAdapter;
import com.sobesworld.wgucoursecommander.database.CourseViewModel;
import com.sobesworld.wgucoursecommander.database.TermViewModel;
import com.sobesworld.wgucoursecommander.database.entity.TermEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class TermDetail extends AppCompatActivity {

    private TermViewModel mTermViewModel;
    private boolean recordStatusNew;
    int termID;
    EditText termTitle;
    EditText termStartDate;
    EditText termEndDate;
    String termNotes;
    final Calendar startCalendar = Calendar.getInstance();
    final Calendar endCalendar = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
        recordStatusNew = getIntent().getBooleanExtra(getString(R.string.is_new_record), false);
        termID = getIntent().getIntExtra(getResources().getString(R.string.idnum), -1);
        mTermViewModel = new ViewModelProvider(TermDetail.this).get(TermViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.termCourseList);
        final CourseListAdapter adapter = new CourseListAdapter(new CourseListAdapter.CourseDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(TermDetail.this));
        CourseViewModel mCourseViewModel = new ViewModelProvider(TermDetail.this).get(CourseViewModel.class);
        mCourseViewModel.getLinkedCourses(termID).observe(TermDetail.this, adapter::submitList);

        // sets values of all fields upon record open
        termTitle = findViewById(R.id.termTitleEdit);
        termStartDate = findViewById(R.id.termStartDateEdit);
        termEndDate = findViewById(R.id.termEndDateEdit);
        termTitle.setText(getIntent().getStringExtra(getResources().getString(R.string.title)));
        termStartDate.setText(getIntent().getStringExtra(getResources().getString(R.string.start_date)));
        termEndDate.setText(getIntent().getStringExtra(getResources().getString(R.string.end_date)));
        termNotes = getIntent().getStringExtra(getResources().getString(R.string.notes));

        // sets term start date from user's date picker selection
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

        // onClickListener for the term end date field
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

        // save button functionality
        Button saveButton = findViewById(R.id.termSaveButton);
        saveButton.setOnClickListener(view -> {
            saveTerm();
            Intent intent = new Intent(TermDetail.this, TermList.class);
            startActivity(intent);
        });

        // delete button functionality
        Button deleteButton = findViewById(R.id.termDeleteButton);
        deleteButton.setOnClickListener( view -> {
            if (recordStatusNew) {
                Toast.makeText(getApplicationContext(), "New term entry. No saved record to delete.", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(this, TermList.class);
                deleteTerm(intent);
            }
        });

        // add course button TODO: create add course button on term detail (ties in with new save functionality)
        ImageView addCourse = findViewById(R.id.termAddCourseButton);
        addCourse.setOnClickListener(view -> {
            if (recordStatusNew) {
                Toast.makeText(TermDetail.this, "This record is new. You must save the record before adding a course.", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(TermDetail.this, CourseDetail.class);
                intent.putExtra(getString(R.string.is_new_record), true);
                intent.putExtra(getString(R.string.from_term_detail), true);
                intent.putExtra(getString(R.string.termID), termID);
                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, TermList.class);
            applyUnsavedChanges(intent);
        }
        if (item.getItemId() == R.id.home_detail_menu) {
            Intent intent = new Intent(this, MainActivity.class);
            applyUnsavedChanges(intent);
        }
        if (item.getItemId() == R.id.note_detail_menu) {
            showNoteDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    // handles editing and sharing of notes
    void showNoteDialog() {
        final Dialog dialog = new Dialog(TermDetail.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.note_dialog);
        final EditText note = dialog.findViewById(R.id.noteBody);
        note.setText(termNotes);
        Button saveNoteButton = dialog.findViewById(R.id.saveNoteButton);
        Button shareNoteButton = dialog.findViewById(R.id.shareNoteButton);

        saveNoteButton.setOnClickListener(view -> {
            termNotes = note.getText().toString();
            dialog.dismiss();
        });

        shareNoteButton.setOnClickListener(view -> {
            String title = termTitle.getText().toString();
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

    private void applyUnsavedChanges(Intent intent) {
        String titleOld = getIntent().getStringExtra(getResources().getString(R.string.title));
        String startDateOld = getIntent().getStringExtra(getResources().getString(R.string.start_date));
        String endDateOld = getIntent().getStringExtra(getResources().getString(R.string.end_date));
        String notesOld = getIntent().getStringExtra(getResources().getString(R.string.notes));

        if (termTitle.getText().toString().equals(titleOld) && termStartDate.getText().toString().equals(startDateOld) &&
                termEndDate.getText().toString().equals(endDateOld) && notesOld.equals(termNotes)) {
            startActivity(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(TermDetail.this);
            builder.setTitle("COMMIT CHANGES")
                    .setMessage("Click COMMIT to save any changes you have made. Click CANCEL to close this record without saving.")
                    .setCancelable(false);
            builder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> startActivity(intent));
            builder.setPositiveButton(R.string.commit, (dialogInterface, i) -> {
                saveTerm();
                startActivity(intent);
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void saveTerm() {
        if (recordStatusNew) {
            TermEntity term = new TermEntity(termTitle.getText().toString(), termStartDate.getText().toString(),
                    termEndDate.getText().toString(), termNotes);
            mTermViewModel.insert(term);
            Toast.makeText(getApplicationContext(), "New term record created.", Toast.LENGTH_LONG).show();
        } else {
            TermEntity term = new TermEntity(termID, termTitle.getText().toString(), termStartDate.getText().toString(),
                    termEndDate.getText().toString(), termNotes);
            mTermViewModel.insert(term);
            Toast.makeText(getApplicationContext(), "Term record updated.", Toast.LENGTH_LONG).show();
        }
    }

    // creates a delete confirmation dialog and deletes course if confirmed
    private void deleteTerm(Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TermDetail.this);
        builder.setTitle(R.string.term_delete_alert_title)
                .setMessage(R.string.term_delete_alert_message)
                .setCancelable(false);
        builder.setNegativeButton(R.string.abort, (dialogInterface, i) -> {
        });
        builder.setPositiveButton(R.string.confirm, (dialogInterface, i) -> {
            mTermViewModel.deleteTermByID(termID);
            Toast.makeText(getApplicationContext(), "Term record permanently deleted.", Toast.LENGTH_LONG).show();
            startActivity(intent);
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}