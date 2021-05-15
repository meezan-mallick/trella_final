package com.android.blogapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.chinalwb.are.AREditor;

public class CreateBlogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_blog);

        AREditor arEditor = this.findViewById(R.id.areditor);
        arEditor.setExpandMode(AREditor.ExpandMode.FULL);
        arEditor.setHideToolbar(false);
        arEditor.setToolbarAlignment(AREditor.ToolbarAlignment.BOTTOM);

    }
}