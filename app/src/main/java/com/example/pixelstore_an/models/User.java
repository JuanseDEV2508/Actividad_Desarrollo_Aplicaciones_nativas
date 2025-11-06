package com.example.pixelstore_an.models;

import java.io.Serializable;

public class User implements Serializable {

    private String name;
    private String email;
    private String phone;
    private int image;
    private int age;

    public User(String name, String email, String phone, int image, int age ){
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.image = image;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public int getImage() {
        return image;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone_number) {
        this.phone = phone_number;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
