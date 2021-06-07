package com.android.blogapplication;

public class BlogModel {
    private String blog_content;
    private String time;
    private String blog_title;

    public BlogModel() {
    }

    public BlogModel(String blog_content, String time, String blog_title) {
        this.blog_content = blog_content;
        this.time = time;
        this.blog_title = blog_title;
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
}
