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
