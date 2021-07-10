package com.example;
import java.util.ArrayList;

public class User {
    private String Username;
    private String Password;
    private String Role; //Seller , Customer , Admin, Guest?
    private Integer ID;
    private Integer testvar;
    //private Item[] SellingList;
    //private Item[] ShoppingList; // something else

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
    /*

    public Item[] getShoppingList() {
        return this.ShoppingList;
    }

    public Item[] getSellingList() {
        return this.SellingList;
    }

    

    public void addToShopList(Item in){
        this.ShoppingList.add(in); // must chnage method!!!!!!!!!!!!!
    }

    public void removeFromShopList(Item out){
        this.ShoppingList.remove(out);
    }

    public void addToSellList(Item in){
        this.SellingList.add(in);
    }

    public void removeFromSellList(Item out){
        this.SellingList.remove(out);
    }
    */

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

/*
    public void setShoppingList( Item[] in) { //shallow copy ?????????????
        this.ShoppingList = in;
    }

    public void getSellingList( Item[] in) {
        this.SellingList = in;
    }
    */
}
