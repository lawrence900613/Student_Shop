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

 @GetMapping(path ="/Home/{id}")
  public String landingSpecialized(@PathVariable("id") Integer recieveID, Map<String, Object> model) {
    
    try (Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM DBNAME"); //change name
      
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
        case "Seller":
          return "homeSeller";
        case "Customer":
          return "home";
        case "Guest":
          return "home"; //??
        case "Admin":
          return "homeAdmin"; //??
      }

     throw new Exception("testing"); //!!!!!!!!!!!!!!!!!!!!
    }

    catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }
  

@GetMapping(path = "/myPage/{id}")
public String myPage(@PathVariable("id") Integer recieveID, Map<String, Object> model) throws Exception{

  try (Connection connection = dataSource.getConnection()){
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM DBNAME WHERE ID =" + recieveID); //change name
    
    User output = new User(); // store data

    if(recieveID == (rs.getInt("id"))){
      output.setName("" + rs.getObject("Username"));
      output.setRole("" + rs.getObject("Role"));
      output.setID(rs.getInt("id"));
      //output.setSellingList(rs.getArray("SellingList")); //not sure
      model.put("dblist", rs.getObject("SellingList"));
      model.put("ret", output);
      }

      return "/myPage";
    }


  catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }
}


@GetMapping(path = "/myPage/add/{id}")
public String addPage(@PathVariable("id") Integer recieveID, Map<String, Object> model) throws Exception{
  return "/add";
}

@GetMapping(path ="/homeSeller")
public String test() throws Exception{
  return "homeSeller";
}

@PostMapping(path = "/afterSubmitNewItem", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  public String handleNewItem(Map<String, Object> model, Items item, @RequestParam("file") MultipartFile file) throws Exception{
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
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS recs (id serial, Name varchar(50), Description varchar(100), Price real)");
    ResultSet rs = stmt.executeQuery("SELECT * FROM db");
    ArrayList<Item> output2 = new ArrayList<Item>();
    while(rs.next()){
      Item output = new Item();
      output.setName("" + rs.getObject("Name"));
      output.setDescription("" + rs.getObject("Description"));
      output.setPrice(rs.getDouble("Price"));
      output.setID(rs.getInt("id"));

      output2.add(output);
    }  

    model.put("records", output2);
    return "View";
  }catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }
}

@PostMapping(
  path = "/DELETE/{id}",
  consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
)
public String handleDeleteButton(@PathVariable("id") Integer recID, Map<String, Object> model) throws Exception {
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("DELETE FROM DBNAME WHERE id=" + recID + ";"); //chnage dbname
    return "redirect:/rectangle/successD"; // sure u wanna redirect?
  } catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }

}

  @PostMapping( //create update handle !!!!!!!!!!!
    path = "/UPDATE/{id}",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleUpdateButton(@PathVariable("id") Integer recieveID, Map<String, Object> model) throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM DBNAME WHERE ID =" + recieveID); //chnage dbname

      Item output = new Item();
      output.setName("" + rs.getObject("Name"));
      output.setDescription("" + rs.getObject("Description"));
      output.setPrice(rs.getDouble("Price"));
      output.setStock(rs.getInt("Stock"));
      output.setID(rs.getInt("id"));

      model.put("ret",output);

      return "/update"; // sure u wanna redirect?
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }

  }
  

@GetMapping(path = "/myPage/{id}")
public String myPage(@PathVariable("id") Integer recieveID, Map<String, Object> model) throws Exception{

  try (Connection connection = dataSource.getConnection()){
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM DBNAME WHERE ID =" + recieveID); //change name
    
    User output = new User(); // store data

    if(recieveID == (rs.getInt("id"))){
      output.setName("" + rs.getObject("Username"));
      output.setRole("" + rs.getObject("Role"));
      output.setID(rs.getInt("id"));
      //output.setSellingList(rs.getArray("SellingList")); //not sure
      model.put("dblist", rs.getObject("SellingList"));
      model.put("ret", output);
      }

      return "/myPage";
    }


  catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }
}


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
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS recs (id serial, Name varchar(50), Description varchar(100), Price real)");
    ResultSet rs = stmt.executeQuery("SELECT * FROM items");
    ArrayList<Item> output2 = new ArrayList<Item>();
    while(rs.next()){
      Item output = new Item();
      output.setName("" + rs.getObject("Name"));
      output.setDescription("" + rs.getObject("Description"));
      output.setPrice(rs.getDouble("Price"));
      output.setID(rs.getInt("id"));

      output2.add(output);
    }  

    model.put("records", output2);
    return "View";
  }catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }
}

@PostMapping(
  path = "/DELETE/{id}",
  consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
)
public String handleDeleteButton(@PathVariable("id") Integer recID, Map<String, Object> model) throws Exception {
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("DELETE FROM DBNAME WHERE id=" + recID + ";"); //chnage dbname
    return "redirect:/rectangle/successD"; // sure u wanna redirect?
  } catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }

}

  @PostMapping( //create update handle !!!!!!!!!!!
    path = "/UPDATE/{id}",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleUpdateButton(@PathVariable("id") Integer recieveID, Map<String, Object> model) throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM DBNAME WHERE ID =" + recieveID); //chnage dbname

      Item output = new Item();
      output.setName("" + rs.getObject("Name"));
      output.setDescription("" + rs.getObject("Description"));
      output.setPrice(rs.getDouble("Price"));
      output.setStock(rs.getInt("Stock"));
      output.setID(rs.getInt("id"));

      model.put("ret",output);

      return "/update"; // sure u wanna redirect?
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }

  }



  @GetMapping(
    path = "/login"
  )
  public String getLoginForm(Map<String, Object> model) {
    Account account = new Account();
    model.put("account", account);
    return "login";
  }

  @PostMapping(
    path = "/login",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleUserLogin(Map<String, Object> model, Account account) throws Exception {
    try(Connection connection = dataSource.getConnection()) {
      System.out.println(account.getRole());
      Statement stmt = connection.createStatement();
      String sql = "SELECT * FROM Account WHERE username ='"+account.getUsername()+"'AND password ='"+account.getPassword() + "' ";
      ResultSet rs = stmt.executeQuery(sql);
      if(rs.next()){
        String role = rs.getString("role");
        if(role.equals("customer")){
          System.out.println(account.getRole());
          System.out.println("Success");
          return "redirect:/home";
        }else{
          System.out.println("Success123");
          return "redirect:/homeSeller";
        }
      }
      return "login";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @GetMapping(
    path = "/create"
  )
   String getNewAcc(Map<String, Object> model) {
     Account account = new Account();
     model.put("account", account);
     return "create";
   }

  @PostMapping(
    path = "/create",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleCreate(Map<String, Object> model, Account account) throws Exception {
    System.out.println(account.getUsername());
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      String sql = "SELECT * FROM Account WHERE username ='"+account.getUsername()+"'AND password ='"+account.getPassword() + "' ";
      ResultSet rs = stmt.executeQuery(sql);
      if(rs.next()){
        return "accounterror";
      }else{
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Account (username varchar(20), password varchar(16), role varchar(16))");
        sql = "INSERT INTO Account (username, password,role) VALUES ('" + account.getUsername() + "','" + account.getPassword() + "','" + account.getRole() + "')";
        stmt.executeUpdate(sql);
      }
      
      return "success2";
    }
    catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

@GetMapping(
  path = "/login/success"
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


  @GetMapping(path = "/sellerHome")
    public String getNewItem(Map<String, Object> model){
      Items newItem = new Items();    //creates a new empty Item object
      model.put("item", newItem);
      return "homeSeller";
    }
  @GetMapping(path = "/search")
    public String getSearch(Map<String, Object> model){
      Searchname item = new Searchname() ;   //creates a new empty Item object
      model.put("item", item);
      return "search"; 

    }

  @PostMapping(path = "/searchitem", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
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
    return "redirect:/search";

  }
  catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }

}

@GetMapping(path = "/success/search")
public String getsearchagain(Map<String, Object> model){
  Searchname item = new Searchname() ;   //creates a new empty Item object
  model.put("item", item);
  return "search"; 
}

@GetMapping(path = "/success/search")
public String getsearchagain(Map<String, Object> model){
  Searchname item = new Searchname() ;   //creates a new empty Item object
  model.put("item", item);
  return "search"; 
}


  @GetMapping("/itemAdd/success")
  public String itemAddedSuccess(){
    return "success";
  }




  @GetMapping(path = "/home")
    public String getmain(Map<String, Object> model){
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