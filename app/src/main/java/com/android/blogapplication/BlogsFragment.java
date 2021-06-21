package com.android.blogapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class BlogsFragment extends Fragment {

    TabLayout blog_tabLayout;
    FrameLayout blog_frameLayout;
    FloatingActionButton floating_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_blogs,container,false);
        // Action bar hide
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        blog_tabLayout = v.findViewById(R.id.blog_tabLayout);
        blog_frameLayout = v.findViewById(R.id.blog_frameLayout);
        floating_btn = v.findViewById(R.id.floating_btn);



        Fragment fragment = null;
        fragment = new MyBlogsFragment();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.blog_frameLayout,fragment);
        transaction.commit();
        blog_tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                fragment = new MyBlogsFragment();
                switch (tab.getPosition()){
                    case 0:
                        fragment = new MyBlogsFragment();
                        break;
                    case 1:
                        fragment = new DraftsFragment();
                        break;
                }
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.blog_frameLayout,fragment);
                transaction.commit();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        floating_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getActivity().getApplicationContext(),CreateBlogActivity.class);
                startActivity(i);
            }
        });
        return v;
    }

}

