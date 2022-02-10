package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.Repository;
import com.sobesworld.wgucoursecommander.database.adapters.TermAdapter;

public class TermList extends AppCompatActivity {

    private TermAdapter adapter;
    private Repository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        repo = new Repository(getApplication());
        adapter = new TermAdapter(this);
        fillRecyclerView();

        FloatingActionButton fab = findViewById(R.id.terms_fab_add);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(TermList.this, TermDetail.class);
            intent.putExtra(getString(R.string.is_new_record), true);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fillRecyclerView();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_refresh_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh_menu) {
            fillRecyclerView();
            Toast toast = Toast.makeText(getApplicationContext(), "Term list refreshed.", Toast.LENGTH_LONG);
            toast.show();
        }
        if (item.getItemId() == R.id.home_refresh_menu) {
            Intent homeButton = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(homeButton);
        }
        return super.onOptionsItemSelected(item); // TODO: ask instructor what this return statement does
    }

    private void fillRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.terms_recyclerview);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setTerms(repo.getAllTerms());
    }
}