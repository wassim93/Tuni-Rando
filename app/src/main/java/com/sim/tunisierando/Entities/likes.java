package com.sim.tunisierando.Entities;

/**
 * Created by Aymen on 26/12/2017.
 */

public class likes  {
    String Username;
    String imagePath;

    public likes() {
    }

    public likes(String username, String imagePath) {
        Username = username;
        this.imagePath = imagePath;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
