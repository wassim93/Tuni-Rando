package com.sim.tunisierando.Entities;



import java.io.Serializable;


public class User implements Serializable {

    public int id;

    public String username;

    public String email;

    public String FisrtName;
    public String LastName;
    public String Address;
    public String ProfilePicUrl;
    public String backgroundPicUrl;
    public passwords plainPassword;

    public User(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;

    }



    public User(int id, String username, String profilePicUrl,String fisrtName, String lastName) {
        this.id = id;
        this.username = username;
        FisrtName = fisrtName;
        LastName = lastName;
        ProfilePicUrl = profilePicUrl;
    }

    public User(String username, String profilePicUrl ) {

        this.username = username;
        ProfilePicUrl = profilePicUrl;
    }

    public User(String username, String email, passwords plainPassword) {
        this.username = username;
        this.email = email;
        this.plainPassword = plainPassword;
    }

    public User(int id, String username, String email, String fisrtName, String lastName, String address, String profilePicUrl, passwords plainPassword) {
        this.id = id;
        this.username = username;
        this.email = email;
        FisrtName = fisrtName;
        LastName = lastName;
        Address = address;
        ProfilePicUrl = profilePicUrl;
        this.plainPassword = plainPassword;
    }

    public User(String fisrtName, String lastName, String Profilepic) {
        FisrtName = fisrtName;
        LastName = lastName;
        ProfilePicUrl = Profilepic;
    }

    public User(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public passwords getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(passwords plainPassword) {
        this.plainPassword = plainPassword;
    }

    public String getFisrtName() {
        return FisrtName;
    }

    public void setFisrtName(String fisrtName) {
        FisrtName = fisrtName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getProfilePicUrl() {
        return ProfilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        ProfilePicUrl = profilePicUrl;
    }

    public String getBackgroundPicUrl() {
        return backgroundPicUrl;
    }

    public void setBackgroundPicUrl(String backgroundPicUrl) {
        this.backgroundPicUrl = backgroundPicUrl;
    }

    public static class passwords{
        public  String first;
        public  String second ;

        public passwords() {
        }

    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", FisrtName='" + FisrtName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", Address='" + Address + '\'' +
                ", ProfilePicUrl='" + ProfilePicUrl + '\'' +
                ", backgroundPicUrl='" + backgroundPicUrl + '\'' +
                ", plainPassword=" + plainPassword +
                '}';
    }
}
