package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
    public static final String EXTRA_TERM_ID = "com.sobesworld.wgucoursecommander.EXTRA_TERM_ID";
    public static final String EXTRA_TERM_TITLE = "com.sobesworld.wgucoursecommander.EXTRA_TERM_TITLE";
    public static final String EXTRA_TERM_START_DATE = "com.sobesworld.wgucoursecommander.EXTRA_TERM_START_DATE";
    public static final String EXTRA_TERM_END_DATE = "com.sobesworld.wgucoursecommander.EXTRA_TERM_END_DATE";
    public static final String EXTRA_TERM_NOTES = "com.sobesworld.wgucoursecommander.EXTRA_TERM_NOTES";

    EditText editTextTermTitle;
    EditText editTextTermStartDate;
    EditText editTextTermEndDate;
    EditText editTextTermNotes;
    final Calendar startCalendar = Calendar.getInstance();
    final Calendar endCalendar = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
        recordStatusNew = getIntent().getBooleanExtra(getString(R.string.is_new_record), true);
        repo = new Repository(getApplication());
        termID = getIntent().getIntExtra(getResources().getString(R.string.idnum), -1);
        fillRecyclerView();

        // sets values of all fields upon record open
        editTextTermTitle = findViewById(R.id.termTitleEdit);
        editTextTermStartDate = findViewById(R.id.termStartDateEdit);
        editTextTermEndDate = findViewById(R.id.termEndDateEdit);
        editTextTermTitle.setText(getIntent().getStringExtra(getResources().getString(R.string.title)));
        editTextTermStartDate.setText(getIntent().getStringExtra(getResources().getString(R.string.start_date)));
        editTextTermEndDate.setText(getIntent().getStringExtra(getResources().getString(R.string.end_date)));
        editTextTermNotes = getIntent().getStringExtra(getResources().getString(R.string.notes));

        // sets term start date from user's date picker selection
        DatePickerDialog.OnDateSetListener startDateDialog = (datePicker, year, month, dayOfMonth) -> {
            startCalendar.set(Calendar.YEAR, year);
            startCalendar.set(Calendar.MONTH, month);
            startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            editTextTermStartDate.setText(sdf.format(startCalendar.getTime()));
        };

        // onClickListener for the term start date field
        editTextTermStartDate.setOnClickListener(view -> {
            String info = editTextTermStartDate.getText().toString();
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
            editTextTermEndDate.setText(sdf.format(endCalendar.getTime()));
        };

        // onClickListener for the term end date field
        editTextTermEndDate.setOnClickListener(view -> {
            String info = editTextTermEndDate.getText().toString();
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
            /*if (recordStatusNew) {
                saveTerm();
                recordStatusNew = false;
                termID = repo.getNewTermID();
            } else {
                saveTerm();
            }*/
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
        /*ImageView addCourse = findViewById(R.id.termAddCourseButton);
        addCourse.setOnClickListener(view -> {
            if (recordStatusNew) {
                Toast.makeText(TermDetail.this, "This record is new. You must save the record before adding a course.", Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(TermDetail.this, CourseDetail.class);
            intent.putExtra(getString(R.string.is_new_record), true);
            intent.putExtra(getString(R.string.termID), termID);
        });*/
    }

    @Override
    public void onResume() {
        super.onResume();
        fillRecyclerView();
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
        note.setText(editTextTermNotes);
        Button saveNoteButton = dialog.findViewById(R.id.saveNoteButton);
        Button shareNoteButton = dialog.findViewById(R.id.shareNoteButton);

        saveNoteButton.setOnClickListener(view -> {
            editTextTermNotes = note.getText().toString();
            dialog.dismiss();
        });

        shareNoteButton.setOnClickListener(view -> {
            String title = editTextTermTitle.getText().toString();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    private void saveTerm() {
        if (recordStatusNew) {
            TermEntity term = new TermEntity(editTextTermTitle.getText().toString(), editTextTermStartDate.getText().toString(),
                    editTextTermEndDate.getText().toString(), editTextTermNotes);
            repo.insert(term);
            Toast.makeText(getApplicationContext(), "New term record created.", Toast.LENGTH_LONG).show();
        } else {
            TermEntity term = new TermEntity(termID, editTextTermTitle.getText().toString(), editTextTermStartDate.getText().toString(),
                    editTextTermEndDate.getText().toString(), editTextTermNotes);
            repo.update(term);
            Toast.makeText(getApplicationContext(), "Term record updated.", Toast.LENGTH_LONG).show();
        }
    }

    // creates a delete confirmation dialog and deletes course if confirmed
    private void deleteTerm(Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.term_delete_alert_title)
                .setMessage(R.string.term_delete_alert_message)
                .setCancelable(false);
        builder.setNegativeButton(R.string.abort, (dialogInterface, i) -> {
        });
        builder.setPositiveButton(R.string.confirm, (dialogInterface, i) -> {
            repo.deleteTermByID(termID);
            repo.deleteLinkedCourses(termID);
            Toast.makeText(getApplicationContext(), "Term record permanently deleted.", Toast.LENGTH_LONG).show();
            startActivity(intent);
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void fillRecyclerView() {
        CourseAdapter adapter = new CourseAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.termCourseList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (!recordStatusNew) {
            adapter.setCourses(repo.getLinkedCourses(termID));
        }
    }
}