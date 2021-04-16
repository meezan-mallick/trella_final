package com.android.blogapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {
    TextView welcome;
    Button sign_out,nav;

    //firebase object
    private FirebaseAuth mAuth;
    //firebase User
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWidgets();

        //intialising the firebase object
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        welcome.setText(currentUser.getEmail());

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.getInstance().signOut();
                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),NavigationActivity.class);

                startActivity(i);
            }
        });
    }

    private void getWidgets() {
        welcome = findViewById(R.id.welcome);
        sign_out = findViewById(R.id.sign_out);
        nav = findViewById(R.id.nav);
    }
}