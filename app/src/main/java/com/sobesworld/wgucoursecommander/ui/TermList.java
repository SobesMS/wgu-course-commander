package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.viewmodel.TermViewModel;

public class TermList extends AppCompatActivity {

    private TermViewModel mTermViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        RecyclerView recyclerView = findViewById(R.id.terms_recyclerview);
        final TermListAdapter adapter = new TermListAdapter(new TermListAdapter.TermDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTermViewModel = new ViewModelProvider(this).get(TermViewModel.class);
        mTermViewModel.getAllTerms().observe(this, terms -> {
            adapter.submitList(terms);
        });
    }
}