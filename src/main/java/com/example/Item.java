package com.example;

public class Item {
    private String Name;
    private String Description;
    private Double Price;
    private Integer ID;
    private Integer Stock;

    public String getName() {
        return this.Name;
    }

    public String getDescription() {
        return this.Description;
    }

    public Double getPrice() {
        return this.Price;
    }

    public Integer getID() {
        return this.ID;
    }

    public Integer getStock() {
        return this.Stock;
    }

    public void setName(String n) {
        this.Name = n;
    }

    public void setDescription(String d) {
        this.Description = d;
    }

    public void setPrice(Double p) {
        this.Price = p;
    }

    public void setID(Integer i) {
        this.ID = i;
    }

    public void setStock(Integer s) {
        this.Stock = s;
    }
}