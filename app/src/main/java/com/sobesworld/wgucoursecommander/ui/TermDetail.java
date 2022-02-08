package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.Repository;
import com.sobesworld.wgucoursecommander.database.adapters.CourseAdapter;

public class TermDetail extends AppCompatActivity {

    private CourseAdapter adapter;
    private Repository repo;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        repo = new Repository(getApplication());
        adapter = new CourseAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.termCourseList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setCourses(repo.getAllCourses());

        Button deleteButton = findViewById(R.id.termDeleteButton);
        deleteButton.setOnClickListener( view -> {
            deleteTerm();
            dialog.show();
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
            // TODO: write share method
            System.out.println("Hi, you just pressed share!");
        }
        if (item.getItemId() == R.id.notify_detail_menu) {
            // TODO: write notify method
            System.out.println("Hi, you just pressed notify!");
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteTerm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.term_delete_alert_title).setMessage(R.string.term_delete_alert_message);
        builder.setPositiveButton(R.string.abort, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast toast = Toast.makeText(getApplicationContext(), "Ok, we won't do that.", Toast.LENGTH_LONG);
                toast.show();
            }
        });
        builder.setNegativeButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast toast = Toast.makeText(getApplicationContext(), "Fooled you!!!", Toast.LENGTH_LONG);
                toast.show();
                finish();
            }
        });
        dialog = builder.create();
    }
}