package com.example.pixelstore_an.models;

public class Product {
    private int id;
    private String name;
    private float price;
    private int image;
    private String category;

    public Product(int id, String name, float price, int image, String category){
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.category = category;
    }

    public Product(String name, String category, float price, int image) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.image = image;
    }


    // Se realiza los getters y los Setters

    public int getId() {
        return id;
    }

    public float getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }
}
