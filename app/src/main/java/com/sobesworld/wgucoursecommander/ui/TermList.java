package com.sobesworld.wgucoursecommander.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sobesworld.wgucoursecommander.R;
import com.sobesworld.wgucoursecommander.database.TermViewModel;
import com.sobesworld.wgucoursecommander.database.adapters.TermAdapter;
import com.sobesworld.wgucoursecommander.database.entity.TermEntity;

import java.util.List;

public class TermList extends AppCompatActivity {
    public static final String TAG = "TermList";

    private TermViewModel termViewModel;
    private ActivityResultLauncher<Intent> activityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        FloatingActionButton addTermFab = findViewById(R.id.add_term_fab);
        addTermFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TermList.this, TermDetail.class);
                intent.putExtra(MainActivity.EXTRA_REQUEST_ID, MainActivity.REQUEST_ADD);
                activityLauncher.launch(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.terms_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TermAdapter adapter = new TermAdapter();
        recyclerView.setAdapter(adapter);

        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);
        termViewModel.getAllTerms().observe(this, new Observer<List<TermEntity>>() {
            @Override
            public void onChanged(List<TermEntity> termEntities) {
                adapter.submitList(termEntities);
            }
        });

        adapter.setOnItemClickListener(new TermAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TermEntity termEntity) {
                Intent intent = new Intent(TermList.this, TermDetail.class);
                intent.putExtra(MainActivity.EXTRA_REQUEST_ID, MainActivity.REQUEST_EDIT);
                intent.putExtra(TermDetail.EXTRA_TERM_ID, termEntity.getTermID());
                intent.putExtra(TermDetail.EXTRA_TERM_TITLE, termEntity.getTermTitle());
                intent.putExtra(TermDetail.EXTRA_TERM_START_DATE, termEntity.getTermStartDate());
                intent.putExtra(TermDetail.EXTRA_TERM_END_DATE, termEntity.getTermEndDate());
                activityLauncher.launch(intent);
            }
        });

        activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent intent = result.getData();
                        int resultCode = result.getResultCode();
                        if (intent != null) {
                            int termID = intent.getIntExtra(TermDetail.EXTRA_TERM_ID, -1);
                            String termTitle = intent.getStringExtra(TermDetail.EXTRA_TERM_TITLE);
                            String termStartDate = intent.getStringExtra(TermDetail.EXTRA_TERM_START_DATE);
                            String termEndDate = intent.getStringExtra(TermDetail.EXTRA_TERM_END_DATE);
                            if (resultCode == RESULT_OK) {
                                TermEntity termEntity = new TermEntity(termTitle, termStartDate, termEndDate);
                                if (termID == -1) {
                                    termViewModel.insert(termEntity);
                                    Toast.makeText(TermList.this, "Term added.", Toast.LENGTH_SHORT).show();
                                } else {
                                    termEntity.setTermID(termID);
                                    termViewModel.update(termEntity);
                                    Toast.makeText(TermList.this, "Term updated.", Toast.LENGTH_SHORT).show();
                                }
                            } else if (resultCode == MainActivity.RESULT_DELETE) {
                                if (termID == -1) {
                                    Toast.makeText(TermList.this, "Term does not exist.", Toast.LENGTH_SHORT).show();
                                } else {
                                    termViewModel.deleteUsingTermID(termID);
                                    Toast.makeText(getApplicationContext(), "Term permanently deleted.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
        );
    }
}