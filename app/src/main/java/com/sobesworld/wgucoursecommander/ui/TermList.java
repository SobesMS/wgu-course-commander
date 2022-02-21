package com.sobesworld.wgucoursecommander.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    public static final String EXTRA_REQUEST_ID = "com.sobesworld.wgucoursecommander.EXTRA_REQUEST_ID";
    public static final int REQUEST_ADD_TERM = 1;
    public static final int REQUEST_EDIT_TERM = 2;

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
                intent.putExtra(EXTRA_REQUEST_ID, REQUEST_ADD_TERM);
                activityLauncher.launch(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.terms_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        TermAdapter adapter = new TermAdapter();
        recyclerView.setAdapter(adapter);

        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);
        termViewModel.getAllTerms().observe(this, new Observer<List<TermEntity>>() {
            @Override
            public void onChanged(List<TermEntity> termEntities) {
                adapter.submitList(termEntities);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                termViewModel.delete(adapter.getTermEntityAt(viewHolder.getAdapterPosition()));
                Toast.makeText(TermList.this, "Term deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new TermAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TermEntity termEntity) {
                Intent intent = new Intent(TermList.this, TermDetail.class);
                intent.putExtra(EXTRA_REQUEST_ID, REQUEST_EDIT_TERM);
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
                        Log.d(TAG, "onActivityResult: ");

                        if (result.getResultCode() == RESULT_OK) {
                            Intent intent = result.getData();

                            if (intent != null) {
                                // extract data
                            }
                        } else if (result.getResultCode() == TermDetail.RESULT_TERM_DELETE) {
                            Intent intent = result.getData();
                            termViewModel.delete(adapter.getTermEntityAt(intent.getIntExtra())); // TODO: finish this logic
                            Toast.makeText(getApplicationContext(), "Term record permanently deleted.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }



    /*public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh_menu) {
            fillRecyclerView();
            Toast.makeText(getApplicationContext(), "Term list refreshed.", Toast.LENGTH_LONG).show();
        }
        if (item.getItemId() == R.id.list_home_button) {
            Intent homeButton = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(homeButton);
        }
        return super.onOptionsItemSelected(item);
    }*/
}