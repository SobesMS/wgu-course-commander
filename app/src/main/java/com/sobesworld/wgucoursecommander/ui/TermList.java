package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.Repository;
import com.sobesworld.wgucoursecommander.database.adapters.TermAdapter;

public class TermList extends AppCompatActivity {

    private TermAdapter adapter;
    private Repository repo;
    //private List<TermEntity> allTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        repo = new Repository(getApplication());
        adapter = new TermAdapter(this);
        //allTerms = repo.getAllTerms(); // TODO: delete if setTerms works in fillRecyclerView
        fillRecyclerView();

        FloatingActionButton fab = findViewById(R.id.terms_fab_add);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(TermList.this, TermAddEdit.class);
            intent.putExtra("is new record", true);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        return true;
    }

    /*public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: create term refresh button method
        return super.onOptionsItemSelected(item);
    }*/

    private void fillRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.terms_recyclerview);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setTerms(repo.getAllTerms());
    }
}