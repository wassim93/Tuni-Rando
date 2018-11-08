package com.sim.tunisierando.Entities;

/**
 * Created by Aymen on 02/12/2017.
 */

public class Posts {
    private int id ;
    private String content;
    private String date ;
    private User user ;
    private int like;
    private int comment;
    private String imagePath;

    public Posts(String content, String date, User user, int like, int comment, String imagePath) {
        this.content = content;
        this.date = date;
        this.user = user;
        this.like = like;
        this.comment = comment;
        this.imagePath = imagePath;
    }

    public Posts(int id, String content, String date, User user, int like, int comment, String imagePath) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.user = user;
        this.like = like;
        this.comment = comment;
        this.imagePath = imagePath;
    }
    public Posts(int id, String content , String imagePath) {
        this.id = id;
        this.content = content;

        this.imagePath = imagePath;
    }
    public Posts() {
    }

    public Posts(String content, String imagePath) {
        this.content = content;
        this.imagePath = imagePath;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
