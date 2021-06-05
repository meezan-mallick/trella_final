package com.android.blogapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.chinalwb.are.AREditor;

public class CreateBlogActivity extends AppCompatActivity {

    private  Spinner s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_blog);

        //Action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add New Blog Post");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        s =  findViewById(R.id.spinner);
        AREditor arEditor = this.findViewById(R.id.areditor);

        //spinner data
        String[] arraySpinner = new String[] {"Current Affairs", "Entertainment", "Fashion", "Food", "Health", "Technology", "Travel","Spiritual","Sports"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        arEditor.setExpandMode(AREditor.ExpandMode.FULL);
        arEditor.setHideToolbar(false);
        arEditor.setToolbarAlignment(AREditor.ToolbarAlignment.BOTTOM);

    }

    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}