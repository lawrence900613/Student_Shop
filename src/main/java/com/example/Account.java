package com.example;

public class Account {
    private String username;
    private String password;
    private String role = "";
    private Integer ID;
    private Integer[] SellingList;
    private Integer[] ShoppingList; 
    private Integer[] MessagingList;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setRole(String role){
        this.role = role;
    }

    public String getRole() {
        return this.role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

     public Integer getID() {
        return this.ID;
    }

    public Integer[] getShoppingList() {
        return this.ShoppingList;
    }

    public Integer[] getSellingList() {
        return this.SellingList;
    }

     public void setID(Integer i) {
        this.ID = i;
    }

    public void setShoppingList( Integer[] in) { //shallow copy ?????????????
        this.ShoppingList = in;
    }

    public void setSellingList( Integer[] in) {
        this.SellingList = in;
    }

    public void setMessagingList(Integer[] messagingList){
        this.MessagingList = messagingList;
    }

    public Integer[] getMessagingListNames() {
        return this.MessagingList;
    }

}
