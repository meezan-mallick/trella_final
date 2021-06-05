package com.android.blogapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chinalwb.are.AREditor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CreateBlogFragment extends Fragment {
    Spinner cat_spinner;
    DatabaseReference dbref;

    ValueEventListener listener;
    ArrayList<String> cat_list;
    ArrayAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_createblog,container,false);

        cat_spinner = v.findViewById(R.id.cat_spinner);
        dbref = FirebaseDatabase.getInstance().getReference("categories");

        cat_list = new ArrayList<String>();
        adapter = new ArrayAdapter(getActivity().getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,cat_list);
        cat_spinner.setAdapter(adapter);
        fetchdata();

        AREditor arEditor = v.findViewById(R.id.areditor);
        arEditor.setExpandMode(AREditor.ExpandMode.FULL);
        arEditor.setHideToolbar(false);
        arEditor.setToolbarAlignment(AREditor.ToolbarAlignment.BOTTOM);
        return v;
    }
    public void fetchdata(){
        listener = dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot mydata : snapshot.getChildren()){
                    cat_list.add(mydata.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
