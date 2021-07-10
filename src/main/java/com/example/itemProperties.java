package com.example;

 class sellerItem{
    private String name;
    private String category;
    private String description;
    private byte[] image;



    public String getName(){
        return this.name;
    }
    public String getCategory(){
        return this.category;
    }
    public String getDescription(){
        return this.description;
    }
    public byte[] getImage(){
        return this.image;
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
    public void setImage(byte[] image){
        this.image = image;
    }


}