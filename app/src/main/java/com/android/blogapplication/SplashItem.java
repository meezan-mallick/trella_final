package com.android.blogapplication;

public class SplashItem {
    private String text;
    private int image;

    public SplashItem(String text, int image) {
        this.text = text;
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
