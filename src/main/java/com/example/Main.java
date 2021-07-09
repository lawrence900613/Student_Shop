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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

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

  @GetMapping(path = "/search")
    public String getSearch(Map<String, Object> model){
      Searchname item = new Searchname() ;   //creates a new empty Item object
      model.put("item", item);
      return "search"; 
    }

  @PostMapping(path = "/searchitem", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  public String handleNewItem(Map<String, Object> model, Searchname item) throws Exception{
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


    @GetMapping(path = "/sellerHome")
    public String getNewItem(Map<String, Object> model){
      Item newItem = new Item();    //creates a new empty Item object
      model.put("item", newItem);
      return "homeSeller"; 
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