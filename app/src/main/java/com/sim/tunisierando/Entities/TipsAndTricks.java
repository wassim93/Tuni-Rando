package com.sim.tunisierando.Entities;

import java.io.Serializable;

/**
 * Created by Aymen on 20/11/2017.
 */

public class TipsAndTricks implements Serializable {

    private int id ;
    private String Title ;
    private String Content ;
    private String ImagePath ;
    private User user ;

    public TipsAndTricks(int id, String title, String content, String imagePath) {
        this.id = id;
        Title = title;
        Content = content;
        ImagePath = imagePath;

    }
    public TipsAndTricks(int id, String title, String content, String imagePath, User user) {
        this.id = id;
        Title = title;
        Content = content;
        ImagePath = imagePath;
        this.user = user;
    }
    public TipsAndTricks(String title, String content, String imagePath, User user) {
        Title = title;
        Content = content;
        ImagePath = imagePath;
        this.user = user;
    }

    public TipsAndTricks() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "TipsAndTricks{" +
                "id=" + id +
                ", Title='" + Title + '\'' +
                ", Content='" + Content + '\'' +
                ", ImagePath='" + ImagePath + '\'' +
                '}';
    }
}
