package com.sim.tunisierando.Entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wassim on 13/11/2017.
 */

public class Events implements Serializable{

    private int id,nbrPlace;
    private float prix;
    private String title,description,contact,date,depart,arrive,type,niveau;
    private User user ;
    ArrayList<String> images;


    public Events() {
    }

    public Events(String title, String date) {
        this.title = title;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNbrPlace() {
        return nbrPlace;
    }

    public void setNbrPlace(int nbrPlace) {
        this.nbrPlace = nbrPlace;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getArrive() {
        return arrive;
    }

    public void setArrive(String arrive) {
        this.arrive = arrive;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Events{" +
                "id=" + id +
                ", nbrPlace=" + nbrPlace +
                ", prix=" + prix +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", contact='" + contact + '\'' +
                ", date='" + date + '\'' +
                ", depart='" + depart + '\'' +
                ", arrive='" + arrive + '\'' +
                ", type='" + type + '\'' +
                ", niveau='" + niveau + '\'' +
                ", user=" + user +
                ", images=" + images +
                '}';
    }
}
