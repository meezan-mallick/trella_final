package com.android.blogapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    RecyclerView category_recycler;
    //FireStore Object
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private FirebaseFirestore fstore = FirebaseFirestore.getInstance();
    //Storage for storing images
    private StorageReference mStorageRef;
    List<Integer> cat_img = new ArrayList<>();
    List<String> cat_title = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getWidgets();
        Toast.makeText(this, "getwidets", Toast.LENGTH_SHORT).show();

        category_recycler.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        category_recycler.setLayoutManager(gridLayoutManager);

       firebaseDatabase = FirebaseDatabase.getInstance();
       reference = firebaseDatabase.getReference("categories");

//        System.out.println("category_recycler");
//        cat_img.add(R.drawable.userprofile);
//        cat_img.add(R.drawable.userprofile);
//        cat_img.add(R.drawable.userprofile);
//        cat_img.add(R.drawable.userprofile);
//        cat_title.add("category 1");
//        cat_title.add("category 2");
//        cat_title.add("category 3");
//        cat_title.add("category 4");
//        System.out.println("clist added");
//        category_recycler.setAdapter(new CategoryAdapter(cat_img,cat_title));
//        System.out.println("adapter set");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Query query = fstore.collection("categories");
        FirestoreRecyclerOptions<SingleCategory> options = new FirestoreRecyclerOptions.Builder<SingleCategory>()
                .setQuery(query, SingleCategory.class)
                .build();
        CategoryAdapter categoryAdapter = new CategoryAdapter(options);
    }

    private void getWidgets() {
        category_recycler = findViewById(R.id.category_recycler);
    }
}