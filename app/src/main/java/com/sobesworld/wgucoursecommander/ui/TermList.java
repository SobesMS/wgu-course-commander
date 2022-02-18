package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.TermViewModel;
import com.sobesworld.wgucoursecommander.database.TermListAdapter;

public class TermList extends AppCompatActivity {

    public static final int NEW_TERM_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        RecyclerView recyclerView = findViewById(R.id.terms_recyclerview);
        final TermListAdapter adapter = new TermListAdapter(new TermListAdapter.TermDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(TermList.this));

        TermViewModel mTermViewModel = new ViewModelProvider(TermList.this).get(TermViewModel.class);
        mTermViewModel.getAllTerms().observe(TermList.this, adapter::submitList);

        FloatingActionButton fab = findViewById(R.id.terms_fab_add);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(TermList.this, TermDetail.class);
            intent.putExtra(getString(R.string.is_new_record), true);
            startActivity(intent);
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home_menu) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}