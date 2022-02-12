package com.sobesworld.wgucoursecommander.ui;

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

    private Repository repo;
    private AlertDialog deleteDialog;
    private boolean recordStatusNew;
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
        recordStatusNew = getIntent().getBooleanExtra(getString(R.string.is_new_record), true);
        repo = new Repository(getApplication());
        CourseAdapter adapter = new CourseAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.termCourseList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // displays course list for current term
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
        termNotes = getIntent().getStringExtra(getResources().getString(R.string.notes));

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

        // save button functionality
        Button saveButton = findViewById(R.id.termSaveButton);
        saveButton.setOnClickListener(view -> {
            if (recordStatusNew) {
                TermEntity term = new TermEntity(termTitle.getText().toString(), termStartDate.getText().toString(),
                        termEndDate.getText().toString(), termNotes);
                repo.insert(term);
                finish();
                Toast.makeText(getApplicationContext(), "New term record created.", Toast.LENGTH_LONG).show();
            } else {
                TermEntity term = new TermEntity(getIntent().getIntExtra(getResources().getString(R.string.idnum), -1),
                        termTitle.getText().toString(), termStartDate.getText().toString(), termEndDate.getText().toString(),
                        termNotes);
                repo.update(term);
                finish();
                Toast.makeText(getApplicationContext(), "Term record updated.", Toast.LENGTH_LONG).show();
            }
        });

        // delete button functionality
        Button deleteButton = findViewById(R.id.termDeleteButton);
        deleteButton.setOnClickListener( view -> {
            if (recordStatusNew) {
                Toast.makeText(getApplicationContext(), "New term entry. No saved record to delete.", Toast.LENGTH_LONG).show();
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
        if (item.getItemId() == R.id.note_detail_menu) {
            if (recordStatusNew) {
                Toast.makeText(getApplicationContext(),
                        "You are adding a new term. Term Record must be saved before creating notes.", Toast.LENGTH_LONG).show();
            } else {
                showNoteDialog();
            }
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
            repo.updateTermNotes(termNotes, getIntent().getIntExtra(getResources().getString(R.string.idnum), -1));
            dialog.dismiss();
        });

        shareNoteButton.setOnClickListener(view -> {
            String term = getIntent().getStringExtra(getResources().getString(R.string.title));
            String notes = term + " notes: " + note.getText().toString();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TITLE, "Notes from " + term);
            sendIntent.putExtra(Intent.EXTRA_TEXT, notes);
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });
        dialog.show();
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
                    termNotes);
            repo.delete(term);
            repo.deleteLinkedCourses(getIntent().getIntExtra(getResources().getString(R.string.idnum), -1));
            Toast.makeText(getApplicationContext(), "Term record permanently deleted.", Toast.LENGTH_LONG).show();
            finish();
        });
        deleteDialog = builder.create();
    }
}