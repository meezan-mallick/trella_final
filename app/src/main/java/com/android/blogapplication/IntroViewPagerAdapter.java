package com.android.blogapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IntroViewPagerAdapter extends RecyclerView.Adapter<IntroViewPagerAdapter.OnboardingViewHolder>  {
    private List<SplashItem> splashItems;

    public IntroViewPagerAdapter(List<SplashItem> splashItems) {
        this.splashItems = splashItems;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboardingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.screen,parent,false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        holder.setOnboadringdData(splashItems.get(position));

    }

    @Override
    public int getItemCount() {
        return splashItems.size();
    }

    class OnboardingViewHolder extends RecyclerView.ViewHolder {
        private TextView splash_infoText;
        private ImageView splash_infoImg;


        OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            splash_infoText = itemView.findViewById(R.id.splash_infotext);
            splash_infoImg = itemView.findViewById(R.id.splash_infoimg);
        }

        void setOnboadringdData(SplashItem splashitem) {
            splash_infoText.setText(splashitem.getText());
            splash_infoImg.setImageResource(splashitem.getImage());
        }
    }
}
