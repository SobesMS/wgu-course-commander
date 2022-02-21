package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.adapters.CourseAdapter;
import com.sobesworld.wgucoursecommander.database.entity.TermEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class TermDetail extends AppCompatActivity {
    public static final String TAG = "TermDetail";
    public static final String EXTRA_TERM_ID = "com.sobesworld.wgucoursecommander.EXTRA_TERM_ID";
    public static final String EXTRA_TERM_TITLE = "com.sobesworld.wgucoursecommander.EXTRA_TERM_TITLE";
    public static final String EXTRA_TERM_START_DATE = "com.sobesworld.wgucoursecommander.EXTRA_TERM_START_DATE";
    public static final String EXTRA_TERM_END_DATE = "com.sobesworld.wgucoursecommander.EXTRA_TERM_END_DATE";
    public static final int RESULT_TERM_DELETE = 99;
    static final SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy", Locale.US);

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
        textViewTermStartDate = findViewById(R.id.text_view_start_date);
        textViewTermEndDate = findViewById(R.id.text_view_end_date);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent passedIntent = getIntent();
        termID = passedIntent.getIntExtra(EXTRA_TERM_ID, -1);

        if (passedIntent.getIntExtra(TermList.EXTRA_REQUEST_ID, -1) == 1) {
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
            Intent intent = new Intent(this, TermList.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.home_term_detail_menu) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.save_term_detail_menu) {
            saveTerm();
        }
        if (item.getItemId() == R.id.delete_term_detail_menu) {
            deleteTerm();
        }
        return super.onOptionsItemSelected(item);
    }

    /*private void applyUnsavedChanges(Intent intent) {
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
    }*/

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
            Toast.makeText(TermDetail.this, "A required field is empty.", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent();
            if (termID != -1) {
                intent.putExtra(EXTRA_TERM_ID, termID);
            }
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
            Toast.makeText(this, "Term entry cancelled.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            if (false) {
                Toast.makeText(this, "This term can't be deleted until linked courses are " +
                                "\ntransferred to another term or deleted.", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("CONFIRM TERM DELETION")
                        .setMessage("Select CONFIRM to delete the term record.\nSelect ABORT to cancel.\n\n(records are not recoverable)")
                        .setCancelable(false);
                builder.setNegativeButton(R.string.abort, (dialogInterface, i) -> {
                });
                builder.setPositiveButton(R.string.confirm, (dialogInterface, i) -> {
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_TERM_ID, termID);
                    setResult(RESULT_TERM_DELETE, intent);
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }
}