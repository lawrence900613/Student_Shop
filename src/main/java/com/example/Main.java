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
  String index() {
    return "index";
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
      Statement stmt = connection.createStatement();
      String sql = "SELECT * FROM Accounts WHERE username ='"+account.getUser()+"'AND password ='"+account.getPassword()+"' ";
      ResultSet rs = stmt.executeQuery(sql);
      if(rs.next()){
        System.out.println("Success");
        return "redirect:/login/success";
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
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Accounts (username varchar(20), password varchar(16))");
      String sql = "INSERT INTO Accounts (username, password) VALUES ('" + account.getUser() + "','" + account.getPassword() + "')";
      stmt.executeUpdate(sql);
      System.out.println(account.getUser());
      return "redirect:/login/success";
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
      Item newItem = new Item();    //creates a new empty Item object
      model.put("item", newItem);
      return "homeSeller"; 
    }

  @PostMapping(path = "/afterSubmitNewItem", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  public String handleNewItem(Map<String, Object> model, Item item) throws Exception{
    //saving the data obtained into databse
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Items (name varchar(80), category varchar(20), description varchar(200), url varchar(200))");
      //line below, item.getName etc.. all from parameters
      String sql = "INSERT INTO Items (name, category, description, url) VALUES ('" + item.getName()+"','"+item.getCategory() + "','" + item.getDescription()+ "','"+item.getUrl()+ "')";
      stmt.executeUpdate(sql);
      System.out.println(item.getName()+" "+ item.getCategory()+" "+ item.getDescription());
      return "redirect:/itemAdd/success";
  }
  catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }
}


  public void getImageFromLaptop(java.awt.event.ActionEvent evt){
    JFileChooser chooser = new JFileChooser();
    chooser.showOpenDialog(null);
    File f = chooser.getSelectedFile();
    String filename = f.getAbsolutePath();
    // System.out.println(filename);
    // chooser.setText(filename);
    // Image getAbsolutePath = null;
    // ImageIcon icon = new ImageIcon(filename);
    // Image image = icon.getImage().getScaledInstance(lbl_image.getWidth(), lbl_image.getHeight(), Image.SCALE_SMOOTH);
    // lbl_image.setIcon(icon);
    // System.out.println("Successfull completed import image");

  } 


  @GetMapping("/itemAdd/success")
  public String itemAddedSuccess(){
    return "success";
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