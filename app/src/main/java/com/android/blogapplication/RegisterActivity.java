package com.android.blogapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

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

    //progress dialog
    ProgressDialog pd;

    //FireStore Object
    private FirebaseFirestore fstore;
    //Storage for storing images
    private StorageReference mStorageRef;
    //Text fields
    private EditText username,emailED,passwordED,CpasswordED;
    private ImageView image_profile;
    String userID,usernameString,emailString,passwordString ;
    private Uri img_uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //init progress bar
        pd =  new ProgressDialog( this);
        pd.setMessage("Loggin In");

        //intialising the firebase object
        mAuth = FirebaseAuth.getInstance();

        //intialising the firebaseFireStore object
        fstore = FirebaseFirestore.getInstance();

        //intialising the StorageRefrence object
        mStorageRef = FirebaseStorage.getInstance().getReference();

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
        //set profile image
        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,1000);
            }
        });
        //click on Register button
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usernameString = username.getText().toString().trim();
                Toast.makeText(RegisterActivity.this, usernameString, Toast.LENGTH_SHORT).show();
                emailString = emailED.getText().toString().trim();
                passwordString = passwordED.getText().toString().trim();
                String CpasswordString = CpasswordED.getText().toString().trim();
                if(TextUtils.isEmpty(usernameString)){
                    username.setError("Enter Username");
                    username.setFocusable(true);

                }
                else if(TextUtils.isEmpty(emailString)){
                    //set error message
                    emailED.setError("Enter email id");
                    emailED.setFocusable(true);
                }

                else if(passwordString.length()<=7 ){
                    passwordED.setError("Password length should be 8 or ore than 8 characters");
                    passwordED.setFocusable(true);
                }
                else if(TextUtils.isEmpty(CpasswordString)){
                    CpasswordED.setError("Enter Confirm Password");
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

        //show progress dialog
        pd.show();
        mAuth.createUserWithEmailAndPassword(emailStr,passwordStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    //dismiss progress dialog
                    pd.dismiss();

                    //user data insert into firestore database
                    userID = mAuth.getCurrentUser().getUid();
                    DocumentReference dr = fstore.collection("users").document(userID);
                    Map<String,Object> userData = new HashMap<>();
                    userData.put("userName",usernameString);
                    userData.put("email",emailString);
                    userData.put("userID",userID);
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
                    if(img_uri!=null) {
                        uploadImagetoFireBase(img_uri);
                    }
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(RegisterActivity.this, "User registered successfully "+user.getEmail(), Toast.LENGTH_SHORT).show();

                    Intent i=new Intent(getApplicationContext(),CategoryActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
//                    Intent i=new Intent(new Intent(getApplicationContext(),NavigationActivity.class));
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(i);

                }else{
                    //dismiss progress dialog
                    pd.dismiss();

                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //dismiss progress dialog
                pd.dismiss();

                Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode == Activity.RESULT_OK){
                img_uri = data.getData();
                image_profile.setImageURI(img_uri);

            }
        }
    }

    private void uploadImagetoFireBase(final Uri img_uri) {
        //upload img to firebase storage
        final StorageReference fileRef = mStorageRef.child("profiles/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(img_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(img_uri).into(image_profile);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void getwidgets() {
        image_profile=findViewById(R.id.profile_img);
        username=findViewById(R.id.username);
        login_btn=findViewById(R.id.login_btn);
        registrationButton=findViewById(R.id.registration_btn);
        emailED= findViewById(R.id.email);
        passwordED= findViewById(R.id.password);
        CpasswordED = findViewById(R.id.confirm_password);
        info_text=findViewById(R.id.info_text);
        info_text.setText("Already have an account?");

    }

}
