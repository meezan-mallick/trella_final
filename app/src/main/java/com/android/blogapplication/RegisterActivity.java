package com.android.blogapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    private TextView info_text;
    private Button login_btn;
    //registration button
    private Button registrationButton;
    //firebase object
    private FirebaseAuth mAuth;
    //FireStore Object
    private FirebaseFirestore fstore;
    //Text fields
    private EditText username,emailED,passwordED,CpasswordED;
    private ImageView image_profile;
    String userID,usernameString,emailString ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //intialising the firebase object
        mAuth = FirebaseAuth.getInstance();

        //intialising the firebaseFireStore object
        fstore = FirebaseFirestore.getInstance();

        // Action bar hide
        getSupportActionBar().hide();

        //intialise all the elements
        getwidgets();

        //click on Login
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

        //click on Register button
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameString = username.getText().toString().trim();
                emailString = emailED.getText().toString().trim();
                String passwordString = passwordED.getText().toString().trim();
                String CpasswordString = CpasswordED.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()){
                    //set error message
                    emailED.setError("Invalid Email");
                    emailED.setFocusable(true);
                }
                else if(passwordString.length()<6){
                    passwordED.setError("Password must have 6 characters");
                    passwordED.setFocusable(true);
                }
                else if(!CpasswordString.equals(passwordString)){
                    CpasswordED.setError("Password and confirm password should be same");
                    CpasswordED.setFocusable(true);
                }
                else{
                    //register the user
                    registerUSer(emailString,passwordString);
                }
            }
        });
    }



    private void registerUSer(String emailStr, String passwordStr) {

        mAuth.createUserWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //user data insert into firestore database
                    userID = mAuth.getCurrentUser().getUid();
                    DocumentReference dr = fstore.collection("users").document(userID);
                    Map<String,Object> userData = new HashMap<>();
                    userData.put("userName",usernameString);
                    userData.put("email",emailString);
                    userData.put("profile_image","profile image");
                    dr.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG,"Onsuccess : user profile is created for "+userID);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG,"OnFailure : "+e.toString());
                        }
                    });
                    //Email Verification
                    FirebaseUser fUser = mAuth.getCurrentUser();
                    fUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(RegisterActivity.this, "Verification email has been sent.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "Verification email failure.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(RegisterActivity.this, "User registered successfully "+user.getEmail(), Toast.LENGTH_SHORT).show();

                    Intent i=new Intent(new Intent(getApplicationContext(),NavigationActivity.class));
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);

                }else{
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getwidgets() {
        username=findViewById(R.id.username);
        login_btn=findViewById(R.id.login_btn);
        info_text=findViewById(R.id.info_text);
        registrationButton=findViewById(R.id.registration_btn);
        emailED= findViewById(R.id.email);
        passwordED= findViewById(R.id.password);
        CpasswordED = findViewById(R.id.confirm_password);
        info_text.setText("Already have an account?");

    }

}