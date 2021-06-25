package com.android.blogapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class CategoryActivity extends AppCompatActivity implements CategoryListener{
    TextView textView2;
    RecyclerView category_recycler;
    FirebaseFirestore fstore;
    CollectionReference collectionRef;
    ArrayList<String> cat_name=new ArrayList<>();
    ArrayList<String> cat_img=new ArrayList<>();
    CategoryAdapter adapter;
    ImageView category_choose;
    private List<CategoryModel> cat_list;

    CategoryListener ct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getSupportActionBar().hide();
        getWidgets();

        cat_list = new ArrayList<>();
        fstore = FirebaseFirestore.getInstance();


        fstore.collection("categories")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    CategoryModel catModel = doc.getDocument().toObject(CategoryModel.class);
                                    cat_list.add(catModel);
                                }

                            }
//                            adapter = new CategoryAdapter(getApplicationContext(),cat_list,ct);
                            adapter = new CategoryAdapter(getApplicationContext(),cat_list,ct);
                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false);
                            category_recycler.setLayoutManager(gridLayoutManager);
                            category_recycler.setAdapter(adapter);

                        }
                    }
                });
//        CategoryModel catModel1 = new CategoryModel("abc","axsdxs");
//        cat_list.add(catModel1);
//        CategoryModel catModel2 = new CategoryModel("Fashion","axsdxs");
//        cat_list.add(catModel2);
//        adapter = new CategoryAdapter(getApplicationContext(),cat_list,this);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false);
//        category_recycler.setLayoutManager(gridLayoutManager);
//        category_recycler.setAdapter(adapter);

//
        category_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                List<CategoryModel> selectedCategory = adapter.getSelectedCategory();
//                Toast.makeText(CategoryActivity.this, selectedCategory.size()+" ", Toast.LENGTH_SHORT).show();
//                StringBuilder categories = new StringBuilder();
//                for(int i=0; i<selectedCategory.size(); i++){
//                    if(i==0){
//                        categories.append(selectedCategory.get(i).getName());
//                    }else {
//                        categories.append("\n").append(selectedCategory.get(i).getName());;
//                    }
//                }
//                Toast.makeText(CategoryActivity.this, categories.toString(), Toast.LENGTH_SHORT).show();

                Intent i=new Intent(getApplicationContext(),NavigationActivity.class);
                startActivity(i);
            }
        });

    }

    private void getWidgets() {
        category_recycler = findViewById(R.id.category_recycler);
        category_choose = findViewById(R.id.category_choose);
    }

    @Override
    public void OnCategorySelect(Boolean isSelected) {

    }
}