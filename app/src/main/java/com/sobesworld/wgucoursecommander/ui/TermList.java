package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sobesworld.wgucoursecommander.MainActivity;
import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.TermViewModel;
import com.sobesworld.wgucoursecommander.database.adapters.TermAdapter;

public class TermList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        // UI elements
        FloatingActionButton addTermFab = findViewById(R.id.add_term_fab);
        addTermFab.setOnClickListener(view -> {
            Intent intent = new Intent(TermList.this, TermDetail.class);
            intent.putExtra(NavMenu.EXTRA_REQUEST_ID, NavMenu.REQUEST_ADD);
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.terms_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TermAdapter adapter = new TermAdapter();
        recyclerView.setAdapter(adapter);

        TermViewModel termViewModel = new ViewModelProvider(this).get(TermViewModel.class);
        termViewModel.getAllTerms().observe(this, adapter::submitList);

        adapter.setOnItemClickListener(termEntity -> {
            Intent intent = new Intent(TermList.this, TermDetail.class);
            intent.putExtra(NavMenu.EXTRA_REQUEST_ID, NavMenu.REQUEST_EDIT);
            intent.putExtra(TermDetail.EXTRA_TERM_ID, termEntity.getTermID());
            intent.putExtra(TermDetail.EXTRA_TERM_TITLE, termEntity.getTermTitle());
            intent.putExtra(TermDetail.EXTRA_TERM_START_DATE, termEntity.getTermStartDate());
            intent.putExtra(TermDetail.EXTRA_TERM_END_DATE, termEntity.getTermEndDate());
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // confirms a user is currently logged in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            startActivity(new Intent(TermList.this, MainActivity.class));
        }
    }
}