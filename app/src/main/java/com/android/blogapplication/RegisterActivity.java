package com.android.blogapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {
    private TextView info_text;
    private Button login_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        getwidgets();
        info_text.setText("Already have an account?");
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });
    }
    private void getwidgets() {
        login_btn=findViewById(R.id.login_btn);
        info_text=findViewById(R.id.info_text);
    }
}