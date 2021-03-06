package com.android.blogapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {
    private IntroViewPagerAdapter introViewPagerAdapter;
    private LinearLayout indicator_layout;
    Button login_btn,register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getSupportActionBar().hide();
        setUpOnboardingItems();
        getWidgets();
        ViewPager2 onboardingViewPager=findViewById(R.id.viewPager);
        indicator_layout=findViewById(R.id.indicator);

        onboardingViewPager.setAdapter(introViewPagerAdapter);

        setUpOnboardingIndicators();
        setCurrentIndicator(0);
        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),RegisterActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });

    }


    private void setUpOnboardingItems(){
        List<SplashItem> splashItems=new ArrayList<>();
        SplashItem item_list1=new SplashItem("Share your thoughts, \n with like minded peoples",R.drawable.splash_1);
        SplashItem item_list2=new SplashItem("Share your thoughts, \n with like minded peoples",R.drawable.splash_2);
        SplashItem item_list3=new SplashItem("Share your thoughts, \n with like minded peoples",R.drawable.splash_3);
        splashItems.add(item_list1);
        splashItems.add(item_list2);
        splashItems.add(item_list3);
        introViewPagerAdapter= new IntroViewPagerAdapter(splashItems);
    }

    private void setUpOnboardingIndicators(){
        ImageView[] indicators=new ImageView[introViewPagerAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);
        for(int i=0;i<indicators.length;i++){
            indicators[i]=new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),R.drawable.indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            indicator_layout.addView(indicators[i]);
        }
    }

    private void setCurrentIndicator(int index){
        int childCount=indicator_layout.getChildCount();
        for(int i=0;i<childCount;i++){
            ImageView imageView=(ImageView)indicator_layout.getChildAt(i);
            if(i==index){
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.indicator_active));
            }else{
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.indicator_inactive));
            }

        }
    }
    private void getWidgets() {
        login_btn=(Button) findViewById(R.id.login);
        register_btn=findViewById(R.id.register);
    }
}