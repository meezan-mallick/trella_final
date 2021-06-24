package com.android.blogapplication;

public class BlogModel {
    private String blog_content;
    private String time;
    private String blog_title;
    private String blog_image;
    private String user_id;
    private String category;

    public BlogModel() {
    }

    public BlogModel(String blog_content, String time, String blog_title, String blog_image, String user_id,String category) {
        this.blog_content = blog_content;
        this.time = time;
        this.blog_title = blog_title;
        this.blog_image = blog_image;
        this.user_id = user_id;
        this.category = category;
    }

    public String getBlog_content() {
        return blog_content;
    }

    public void setBlog_content(String blog_content) {
        this.blog_content = blog_content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBlog_title() {
        return blog_title;
    }

    public void setBlog_title(String blog_title) {
        this.blog_title = blog_title;
    }

    public String getBlog_image() {
        return blog_image;
    }

    public void setBlog_image(String blog_image) {
        this.blog_image = blog_image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
