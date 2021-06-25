package com.android.blogapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Adapter.UserRecyclerAdapter;


public class usersFragment extends Fragment {

    private RecyclerView users_recyclerView;
    private List<UserModel> user_list;
    private FirebaseFirestore fstore;
    private Adapter.UserRecyclerAdapter UserRecyclerAdapter;
    private CollectionReference collectionRef;
    public static final String TAG = "TAG";

    //firebase object
    private FirebaseAuth mAuth;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_users, container, false);

//        Toast.makeText(getContext(), "Users fragment", Toast.LENGTH_SHORT).show();

        try {

            user_list = new ArrayList<>();

            users_recyclerView = v.findViewById(R.id.myRecycler);
            UserRecyclerAdapter = new UserRecyclerAdapter(user_list, getContext());

            users_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            users_recyclerView.setAdapter(UserRecyclerAdapter);

//            UserModel uModel1 = new UserModel("11uqj9y6DGNDqBn6SYXoBkcepjh2","Thirali Patel", "thirali7126@gmail.com","profile image");
//            UserModel uModel2 = new UserModel("XQwYaBkcf5cIef6CdjjRvW06dEf2","meezan-mallick","meezanmalek1234@gmail.com","profile image");
//            UserModel uModel3 = new UserModel("tiFGRpiOEkXatfC6h2RZ55kysqA3","Riya Panchal","riya@vox360.com","profile image");
//            user_list.add(uModel1);
//            user_list.add(uModel2);
//            user_list.add(uModel3);

        }
        catch (Exception e){
            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //intialising the firebase object
        mAuth = FirebaseAuth.getInstance();

        //initialize FirebaseFirestore object
        fstore = FirebaseFirestore.getInstance();

        try {
            fstore.collection("users")
                    .addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

                            if(queryDocumentSnapshots!=null){
                                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        UserModel uModel = doc.getDocument().toObject(UserModel.class);
                                        user_list.add(uModel);
                                        UserRecyclerAdapter.notifyDataSetChanged();
//                                        Toast.makeText(getContext(), ""+uModel.getUserName(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
        }
        catch (Exception e){
            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return v;
    }
}