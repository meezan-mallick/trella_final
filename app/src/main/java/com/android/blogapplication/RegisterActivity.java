package com.android.blogapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private TextView info_text;
    private Button login_btn;

    //registration button
    private Button registrationButton;

    //firebase object
    private FirebaseAuth mAuth;

    //Text fields
    private EditText emailED,passwordED;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //intialising the firebase object
        mAuth = FirebaseAuth.getInstance();

        // Action bar hide
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

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailString = emailED.getText().toString().trim();
                String passwordString = passwordED.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()){
                    //set error message
                    emailED.setError("Invalid Email");
                    emailED.setFocusable(true);
                }
                else if(passwordString.length()<6){
                    passwordED.setError("Password must have 6 characters");
                    passwordED.setFocusable(true);
                }
                else{
                    //register the user
                    registerUSer(emailString,passwordString);
                }
            }
        });
    }

    private void registerUSer(String emailString, String passwordString) {

        mAuth.signInWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(RegisterActivity.this, "User registered successfully"+user.getEmail(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegisterActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getwidgets() {
        login_btn=findViewById(R.id.login_btn);
        info_text=findViewById(R.id.info_text);
        registrationButton=findViewById(R.id.registration_btn);
        emailED= findViewById(R.id.email);
        passwordED= findViewById(R.id.password);

    }
}