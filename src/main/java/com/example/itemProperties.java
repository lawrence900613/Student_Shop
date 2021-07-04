package com.example;

public class Item{
    private String name;
    private String category;
    private String description;

    public String getName(){
        return this.name;
    }
    public String getCategory(){
        return this.category;
    }
    public String getDescription(){
        return this.description;
    }

    public void setName(String n){
        this.name = n;
    }

    public void setCategory(String c){
        this.category = c;
    }

    public void setDescription( String d){
        this.description = d;
    }

}