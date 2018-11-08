package com.sim.tunisierando.Entities;

import java.io.Serializable;

/**
 * Created by wassim on 23/12/2017.
 */

public class GallerieImg implements Serializable{

    private int id ;
    private String nom;
    private String url;


    public GallerieImg() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Images{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
