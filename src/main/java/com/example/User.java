package com.example;
import java.util.ArrayList;

public class User {
    private String Username;
    private String Password;
    private String Role; //Seller , Customer
    private Integer ID;
    private Item[] SellingList;
    private Item[] ShoppingList; 

    public String getUsername() {
        return this.Username;
    }

    public String getRole() {
        return this.Role;
    }

    public String getPassword() { //should we have this?
        return this.Password;
    }

    public Integer getID() {
        return this.ID;
    }
    

    public Item[] getShoppingList() {
        return this.ShoppingList;
    }

    public Item[] getSellingList() {
        return this.SellingList;
    }

    public void setName(String n) {
        this.Username = n;
    }

    public void setPassword(String p) {
        this.Password = p;
    }

    public void setRole(String r) {
        this.Role = r;
    }

    public void setID(Integer i) {
        this.ID = i;
    }

    public void setShoppingList( Item[] in) { //shallow copy ?????????????
        this.ShoppingList = in;
    }

    public void getSellingList( Item[] in) {
        this.SellingList = in;
    }
}
