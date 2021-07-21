package com.example;

public class Item {
    private String Name;
    private String Description;
    private String Category;
    private float Price;
    private byte[] Image;
    private Integer ID;
    private Integer Stock;
    private Integer SellerID;

    public String getName() {
        return this.Name;
    }

    public String getDescription() {
        return this.Description;
    }

    public String getCategory() {
        return this.Category;
    }

    public Float getPrice() {
        return this.Price;
    }

    public byte[] getImage(){
        return this.Image;
    }

    public Integer getID() {
        return this.ID;
    }

    public Integer getStock() {
        return this.Stock;
    }

    public Integer getSellerID() {
        return this.SellerID;
    }

    public void setName(String n) {
        this.Name = n;
    }

    public void setDescription(String d) {
        this.Description = d;
    }

    public void setCategory(String c) {
        this.Category = c;
    }

    public void setPrice(Float p) {
        this.Price = p;
    }

    public void setImage(byte[] image){
        this.Image = image;
    }

    public void setID(Integer i) {
        this.ID = i;
    }

    public void setStock(Integer s) {
        this.Stock = s;
    }

    public void setSellerID(Integer in) {
        this.SellerID = in;
    }
}