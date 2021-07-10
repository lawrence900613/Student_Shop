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
  

@GetMapping(path = "/MyPage/{id}")
public String myPage(@PathVariable("id") Integer recieveID, Map<String, Object> model) throws Exception{

  try (Connection connection = dataSource.getConnection()){
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM Accounts WHERE ID =" + recieveID); //change name
    
    Account output = new Account(); // store data

    if(recieveID == (rs.getInt("id"))){
      output.setUsername("" + rs.getObject("Username"));
      output.setRole("" + rs.getObject("Role"));
      output.setID(rs.getInt("id"));
      Array temp = rs.getArray("SellingList");
      if(temp != null){
        Integer[] temp2 = (Integer[]) temp.getArray();
        output.setSellingList(temp2);
      }
      
      model.put("ret", output);
      }

      return "/myPage";
    }


  catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }
}


@GetMapping(path = "/MyPage/add/{id}")
public String addPage(@PathVariable("id") Integer recieveID, Map<String, Object> model) throws Exception{
  return "/add"; // NEED ADDING PAGE -- currently add on home seller
}

@GetMapping(path = "/HomeSeller")
  public String getHomeSellerNOID(Map<String, Object> model){
    Item item = new Item();
    model.put("Item", item);
    return "homeSeller"; 
  }

@PostMapping(path = "/afterSubmitNewItem", 
consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  public String handleNewItem(Map<String, Object> model, Item item) throws Exception{
    //saving the data obtained into databse
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Items (Id serial, Name varchar(80), Description varchar(200), Category varchar(20), Price numeric, Stock Integer)");
      //line below, item.getName etc.. all from parameters
      String sql = "INSERT INTO Items (Name, Category, Description, Price, Stock) VALUES ('" + item.getName() +"','"+item.getCategory() + "','" + item.getDescription() + "','" + item.getPrice() + "','" + item.getStock() + "')";
      stmt.executeUpdate(sql);
      System.out.println(item.getName()+" "+ item.getCategory()+" "+ item.getPrice()+" "+ item.getStock());
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

<<<<<<< HEAD

@GetMapping(path = "/myPage/add/{id}")
public String addPage(@PathVariable("id") Integer recieveID, Map<String, Object> model) throws Exception{
  return "/add";
}

@GetMapping(path ="/homeSeller")
public String test() throws Exception{
  return "homeSeller";
}

@PostMapping(path = "/afterSubmitNewItem", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  public String handleNewItem(Map<String, Object> model, Item item) throws Exception{
    //saving the data obtained into databse
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Items (name varchar(80), category varchar(20), description varchar(200), image)");
      if (!file.isEmpty()) {
        byte[] fileBytes = file.getBytes();
        item.setImage(fileBytes);
      }
      //line below, item.getName etc.. all from parameters
      String sql = "INSERT INTO Items (name, category, description, image) VALUES ('" + item.getName()+"','"+item.getCategory() + "','" + item.getDescription()+ "','" + item.getImage() +"')";
      stmt.executeUpdate(sql);
      System.out.println(item.getName()+" "+ item.getCategory()+" "+ item.getDescription());
      return "redirect:/itemAdd/success";
  }
  catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }
}



@GetMapping(path="/shoppingList")
public String updateShoppingList(Map<String, Object> model) throws Exception{
=======
@GetMapping(path="/ShoppingList")
public String getShoppingListNoID(Map<String, Object> model) throws Exception{
  return "shoppingList";
}

 
@GetMapping(path="/ShoppingList/{id}")
public String getShoppingList(@PathVariable("id") Integer recID, Map<String, Object> model) throws Exception{
>>>>>>> master
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM Accounts WHERE id=" + recID);
    Account outputAcc = new Account();
    Array temp = rs.getArray("ShoppingList");
    outputAcc.setID(rs.getInt("ID"));
    Integer temp2[] = {};
    if(temp != null){
      temp2 = (Integer[]) temp.getArray();
    }
    Integer[] tempArr = temp2;
    ArrayList<Item> storeItem = new ArrayList<Item>();
    for(int i = 0; i < tempArr.length; i++){
      ResultSet rs2 = stmt.executeQuery("SELECT * FROM Items WHERE id=" + tempArr[i]); //if ID has been deleted we need to move on ***
      Item outputItem = new Item();
      outputItem.setName(rs2.getString("Name"));
      outputItem.setDescription(rs2.getString("Description"));
      outputItem.setPrice(rs2.getDouble("Price"));
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
  path = "/DELETEmp/{id}",
  consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
)
public String handleDeleteButtonForMyPage(@PathVariable("id") Integer recID, Map<String, Object> model) throws Exception {
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("DELETE FROM Items WHERE id=" + recID);
    return "redirect:/successDeleteMyPage";
  } catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }
}

  @PostMapping( //create update handle !!!!!!!!!!!
    path = "/UPDATEmp/{id}",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleUpdateButtonforMyPage(@PathVariable("id") Integer recieveID, Map<String, Object> model) throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM Items WHERE ID =" + recieveID);

      Item output = new Item();
      output.setName("" + rs.getObject("Name"));
      output.setDescription("" + rs.getObject("Description"));
      output.setPrice(rs.getDouble("Price"));
      output.setStock(rs.getInt("Stock"));
      output.setID(rs.getInt("id"));

      model.put("ret",output);

      return "/update"; // return to correct return page 
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
        if(role.equals("customer")){
          System.out.println(account.getRole());
          System.out.println("Success");
          return "redirect:/Home";
        }else{
          System.out.println("Success123");
          return "redirect:/HomeSeller";
        }
      }
      return "login";
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
      if(rs.next()){
        return "AccountError";
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

  @RequestMapping("/db")
  String db(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

      ArrayList<String> output = new ArrayList<String>();
      while (rs.next()) {
        output.add("Read from DB: " + rs.getTimestamp("tick"));
      }

      model.put("records", output);
      return "db";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @GetMapping(path = "/Search")
    public String getSearch(Map<String, Object> model){
      Searchname item = new Searchname() ;   //creates a new empty Item object
      model.put("item", item);
      return "search"; 

    }

  @PostMapping(path = "/Searchitem", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  public String searchItem(Map<String, Object> model, Searchname item) throws Exception{
    //saving the data obtained into databse
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS productdatabase (id serial, name varchar(20), description varchar, price numeric, stock real)");
      ResultSet rs = stmt.executeQuery("SELECT * FROM productdatabase");
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
        product.setPrice(rs.getDouble("price"));
        product.setStock(rs.getInt("stock"));
        product.setID(rs.getInt("id"));
        output.add(product);
      }
    }
    model.put("product", output);
    return "redirect:/Search";

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