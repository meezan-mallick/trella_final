package com.android.blogapplication;

public class BlogPost {
    private String user_id,time,blog_image,publish,blog_content,blog_title,category;
    int currentCount = 0;

    public BlogPost() {
    }

    public BlogPost(String user_id, String time, String blog_image, String publish, String blog_content, String blog_title, String category) {
        this.user_id = user_id;
        this.time = time;
        this.blog_image = blog_image;
        this.publish = publish;
        this.blog_content = blog_content;
        this.blog_title = blog_title;
        this.category = category;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBlog_image() {
        return blog_image;
    }

    public void setBlog_image(String blog_image) {
        this.blog_image = blog_image;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public String getBlog_content() {
        return blog_content;
    }

    public void setBlog_content(String blog_content) {
        this.blog_content = blog_content;
    }

    public String getBlog_title() {
        return blog_title;
    }

    public void setBlog_title(String blog_title) {
        this.blog_title = blog_title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }
}
