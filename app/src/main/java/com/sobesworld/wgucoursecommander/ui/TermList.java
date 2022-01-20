package com.sobesworld.wgucoursecommander.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sobesworld.wgucoursecommander.R;

public class TermList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
    }

    public void goToAddTerm(View view) {
        Intent intent= new Intent(TermList.this, AddTerm.class);
        startActivity(intent);
    }

    // TODO: DELETE THIS CODE (Test Term Detail)
    public void testTermDetail(View view) {
        Intent intent=new Intent(TermList.this, TermDetail.class);
        startActivity(intent);
    }
}