package com.sim.tunisierando.Entities;

/**
 * Created by Aymen on 04/12/2017.
 */

public class Comment  {
private int id;
    private String content;
    private User user;
    private Posts posts;
    private String date;

    public Comment(int id, String content, User user, Posts posts, String date) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.posts = posts;
        this.date = date;
    }

    public Comment(String content, User user, Posts posts, String date) {
        this.content = content;
        this.user = user;
        this.posts = posts;
        this.date = date;
    }

    public Comment() {
    }

    public Comment( String content, Posts posts) {

        this.content = content;
        this.posts = posts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Posts getPosts() {
        return posts;
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
