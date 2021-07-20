/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Array; //this fix 
import java.util.ArrayList;
import java.util.Map;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import java.io.Console;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Controller
@SpringBootApplication
public class Main {

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Autowired
  private DataSource dataSource;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }

  @RequestMapping("/")
  String landing() {
    return "home";  //basic landing of user
  }

  @GetMapping( path = "/About")
  String about() {
    return "About";  
  }

  

 @GetMapping(path ="/Home/{id}")
  public String landingSpecialized(@PathVariable("id") Integer recieveID, Map<String, Object> model) {
    
    try (Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM Accounts WHERE id=" + recieveID); 
      
      User output = new User(); // store data

      while(rs.next()){
        if(recieveID == (rs.getInt("id"))){
          output.setName("" + rs.getObject("Username"));
          output.setRole("" + rs.getObject("Role"));
          output.setID(rs.getInt("id"));
          model.put("ret", output);
        }
      }
      System.out.println("IN THIS GETMAPPING with home/{id}");
      switch(output.getRole()){
        case "seller":
          return "homeSeller";
        case "customer":
          return "home";
        case "Guest":
          return "home"; //?? Guest ID SETUP
      }

     throw new Exception("testing"); //!!!!!!!!!!!!!!!!!!!!
    }

    catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }
  

@GetMapping(path = "/MyItem/{id}")
public String myItem(@PathVariable("id") Integer receiveID, Map<String, Object> model) throws Exception{

  try (Connection connection = dataSource.getConnection()){
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM Items");
    
    Item output = new Item(); // store data

    while (rs.next()) {
      if(rs.getInt("Id") == receiveID){
        break;
      }
    }

    output.setName("" + rs.getObject("Name"));
    output.setDescription("" + rs.getObject("Description"));
    output.setCategory("" + rs.getObject("Category"));
    output.setPrice(rs.getFloat("Price"));
    output.setStock(rs.getInt("Stock"));
    output.setID(rs.getInt("Id"));
    output.setImage(rs.getBytes("image")); 
    
    model.put("ret", output); //to display existing 

    Item in = new Item(); //to update
    model.put("Item", in);

    return "myItem";
  } 
    
  catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }
}

@GetMapping(path = {"/HomeSeller/{id}", "/HomeSeller"})
  public String getHomeSellerWithID(@PathVariable (required = false) Integer id, Map<String, Object> model){
    try (Connection connection = dataSource.getConnection()) {
      if(id == null){
        Item in = new Item(); 
        model.put("Item", in);
        return "homeSeller";
      }
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT SellingList FROM Accounts WHERE ID =" + id);
      ArrayList<Item> storeItem = new ArrayList<Item>();
      if(rs.next()){
      Array temp = rs.getArray("SellingList");
      Integer temp2[] = {};
      if(temp != null){
        temp2 = (Integer[]) temp.getArray();
      }
      Integer[] tempArr = temp2;
      for(int i = 0; i < tempArr.length; i++){
        ResultSet rs2 = stmt.executeQuery("SELECT * FROM Items WHERE id=" + tempArr[i]); //implement move on when empty ID
        Item outputItem = new Item();
        outputItem.setName(rs2.getString("Name"));
        outputItem.setCategory(rs2.getString("Category"));
        outputItem.setStock(rs2.getInt("Stock"));
        outputItem.setID(rs2.getInt("ID"));
        storeItem.add(outputItem);
      }
    }
      Item in = new Item(); 
      model.put("Item", in);

      model.put("records", storeItem);
      return "homeSeller";
    }catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    } 
  }


@PostMapping(path = "/afterSubmitNewItem", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public String handleNewItem(Map<String, Object> model, Item Item, @RequestParam("file") MultipartFile file)  throws Exception{
    //saving the data obtained into databse
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();

// trying to add sellerid into items------------------------------------------------------------------------------------

      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Items (Id serial, Name varchar(80), Description varchar(200), Category varchar(20), Price float, image bytea, Stock Integer)");
      if (!file.isEmpty()) {
        byte[] fileBytes = file.getBytes();
        Item.setImage(fileBytes);
      }
      
      //line below, item.getName etc.. all from parameters
      String sql = "INSERT INTO Items (Name, Category, Description, Price, image, Stock) VALUES ('" + Item.getName() +"','"+Item.getCategory() + "','" + Item.getDescription() + "','" + Item.getPrice() + "','" + Item.getImage() + "','" +  Item.getStock() + "')";
      stmt.executeUpdate(sql);
      System.out.println(Item.getName()+" "+ Item.getCategory()+" "+ Item.getPrice()+" "+ Item.getStock());
      return "redirect:/HomeSeller"; //return "redirect:/itemAdd/success"
  }
  catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }
}


@PostMapping(
  path = "/DELETEsp/{id}",
  consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
)
public String handleDeleteButtonForShoppingList(@PathVariable("id") Integer recID, Map<String, Object> model) throws Exception {
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("DELETE FROM DBNAME WHERE id=" + recID + ";"); //THIS NEEDS TO GO INTO USER ARRAY TO DELETE NUMBER
    return "redirect:/successDeleteShoppingList";
  } catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }
}

@GetMapping(path="/ShoppingList")
public String getShoppingListNoID(Map<String, Object> model) throws Exception{
  return "shoppingList";
}

 
@GetMapping(path="/ShoppingList/{id}")
public String getShoppingList(@PathVariable("id") Integer recID, Map<String, Object> model) throws Exception{
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM Accounts");
    while (rs.next()) {
      if(rs.getInt("Id") == recID){
        break;
      }
    }
    Account outputAcc = new Account();
    Array temp = rs.getArray("ShoppingList");
    outputAcc.setID(rs.getInt("ID"));
    Integer temp2[] = {};
    if(temp != null){
      temp2 = (Integer[]) temp.getArray();
    }
    Integer[] tempArr = temp2;
    Item outputItem = new Item();
    ArrayList<Item> storeItem = new ArrayList<Item>();
    for(int i = 0; i < tempArr.length; i++){
      ResultSet rs2 = stmt.executeQuery("SELECT * FROM Items WHERE id=" + tempArr[i]); 
      outputItem.setName(rs2.getString("Name"));
      outputItem.setDescription(rs2.getString("Description"));
      outputItem.setPrice(rs2.getFloat("Price"));
      outputItem.setID(rs2.getInt("ID"));
      storeItem.add(outputItem);
      
    }
    model.put("records", storeItem); //iterate all shopping list item and link them to respective page ***
    return "shoppingList";
  }catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }
}

@PostMapping(
  path = "/DELETEmi/{id}",
  consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
)
public String handleDeleteButtonForMyItem(@PathVariable("id") Integer recID, Map<String, Object> model) throws Exception {
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("DELETE FROM Items WHERE id=" + recID);
    return "redirect:/successDeleteMyItem";
  } catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }
}

  @PostMapping( 
    path = "/UPDATEmi/{id}",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleUpdateButtonforMyItem(@PathVariable("id") Integer receiveID, Map<String, Object> model, Item item) throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      System.out.println(item.getName());
      stmt.executeUpdate("UPDATE Items SET Name ='" + item.getName() + "', Category = '" + item.getCategory() + "', Description = '" + item.getDescription() + "', Price = '" + item.getPrice() + "', Stock = '" + item.getStock() + "' WHERE id =" + receiveID + ";");
      return "successUpdate"; 
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }

  }

  @GetMapping(
    path = "/Login"
  )
  public String getLoginForm(Map<String, Object> model) {
    Account account = new Account();
    model.put("account", account);
    return "login";
  }

  @PostMapping(
    path = "/Login",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleUserLogin(Map<String, Object> model, Account account) throws Exception {
    try(Connection connection = dataSource.getConnection()) {
      System.out.println(account.getRole());
      Statement stmt = connection.createStatement();
        String sql = "SELECT * FROM Accounts WHERE Username ='"+account.getUsername()+"'AND Password ='"+account.getPassword() + "' ";
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next()){
          String role = rs.getString("Role");
          Integer id = rs.getInt("Id");
          if(role.equals("customer")){
            System.out.println(account.getRole());
            System.out.println("Success");
            System.out.println(id);
            UserID userid = new UserID();
            userid.setUserID(id);
            model.put("userID", userid);
            return "redirect:/Home/"+id;
          }else{
            System.out.println("Success123");
            System.out.println(id);
            UserID userid = new UserID();
            userid.setUserID(id);
            model.put("userID", userid);
            return "redirect:/HomeSeller/"+id;
          }
      }
      return "badlogin";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @GetMapping(
    path = "/Create"
  )
   String getNewAcc(Map<String, Object> model) {
     Account account = new Account();
     model.put("account", account);
     return "create";
   }

  @PostMapping(
    path = "/Create",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleCreate(Map<String, Object> model, Account account) throws Exception {
    System.out.println(account.getUsername());
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Accounts (Id serial, Username varchar(20), Password varchar(16), Role varchar(16),Shoppinglist Integer[],Sellinglist Integer[])");
      String sql = "SELECT * FROM Accounts WHERE Username ='"+account.getUsername()+ "' ";
      ResultSet rs = stmt.executeQuery(sql);
      if(rs.next() == true){
        return "accounterror";
      }else{
        sql = "INSERT INTO Accounts (Username, Password,Role) VALUES ('" + account.getUsername() + "','" + account.getPassword() + "','" + account.getRole() + "')";
        stmt.executeUpdate(sql);
      }

      return "SuccessCreate";
    }
    catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

@GetMapping(
  path = "/Login/success"
)
String getLoginSuccess() {
  return "success";
}


  @GetMapping(path = {"/Search/{id}","/Search"})
    public String getSearch(@PathVariable (required = false) Integer id, Map<String, Object> model){
      try (Connection connection = dataSource.getConnection()) {
        if(id == null){
          Searchname item = new Searchname() ;
          ItemID ID = new ItemID();
          UserID userID = new UserID();
          userID.setUserID(0);
          System.out.println(userID.getUserID());

          model.put("userID",userID);    //creates a new empty Item object
          model.put("item", item);
          model.put("ID", ID);
          return "search"; 
        }else{
          Statement stmt = connection.createStatement();
          String sql = "SELECT * FROM Accounts WHERE ID ='"+ id + "' ";
          ResultSet rs = stmt.executeQuery(sql);
          if(rs.next()){
            UserID userID = new UserID();
            Searchname item = new Searchname() ;
            ItemID ID = new ItemID();
            userID.setUserID(rs.getInt("Id"));
            System.out.println(userID.getUserID());
            model.put("userID", userID);
            model.put("item", item);
            model.put("ID", ID);
            return "search";
          }else{
            System.out.println("Success123");
            return "redirect:/HomeSeller";
          }
        }
      }catch (Exception e) {
        model.put("message", e.getMessage());
        return "error";
      }
    }

  @PostMapping(path = {"/Searchitem/{id}","/Searchitem"}, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  public String searchItem(Map<String, Object> model, @PathVariable (required = false) Integer id, Searchname item) throws Exception{
    //saving the data obtained into databse
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Items (Id serial, Name varchar(80), Description varchar(200), Category varchar(20), Price float, image bytea, Stock Integer)");
      ResultSet rs = stmt.executeQuery("SELECT * FROM Items");
      ArrayList<Item> output = new ArrayList<Item>();
      String searchname = item.getName();
      searchname = searchname.toLowerCase();
      System.out.println(searchname);
      while (rs.next()) {
        String productname = rs.getString("name");
        productname = productname.toLowerCase();
        if(productname.contains(searchname)){
        Item product = new Item();
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getFloat("price"));
        product.setStock(rs.getInt("stock"));
        product.setID(rs.getInt("id"));
        output.add(product);
      }
    }
    Searchname item2 = new Searchname() ;
    UserID userID = new UserID();
    ItemID ID = new ItemID();
    userID.setUserID(id);
    model.put("userID", userID);
    model.put("item", item2);
    model.put("product", output);
    model.put("ID", ID);
    return "search";

  }
  catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }

}







@GetMapping(path = "/Success/search")
public String getsearchagain(Map<String, Object> model){
  Searchname item = new Searchname() ;   //creates a new empty Item object
  model.put("item", item);
  return "search"; 
}


  @GetMapping("/ItemAdd/success")
  public String itemAddedSuccess(){
    return "success";
  }




  @GetMapping(path = "/Home")
    public String getHomeNOID(Map<String, Object> model){
      return "home"; 
    }

   


  @Bean
  public DataSource dataSource() throws SQLException {
    if (dbUrl == null || dbUrl.isEmpty()) {
      return new HikariDataSource();
    } else {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);
      return new HikariDataSource(config);
    }
  }

}