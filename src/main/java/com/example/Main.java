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
import java.sql.Array;
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
// import UserID.java;

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
  public String landingNoSignin( Map<String, Object> model ) {
    UserID idofuser = new UserID();
    idofuser.setUserID(0);
    model.put("UserID", idofuser);
    return "home";  //basic landing of user
  }

  @GetMapping(path = "/About")
  public String getAboutNOID(Map<String, Object> model){
    return "About"; 
  }
  
  @GetMapping( path ="/About/{id}")
  public String about(@PathVariable("id")Integer recieveID, Map<String, Object> model ) {
    System.out.println("recieved id is = " +recieveID);

    if(recieveID != null){
      UserID idofuser = new UserID();
      idofuser.setUserID(recieveID);
      model.put("UserID", idofuser);
    }
    
    System.out.println("about to go to About!!!!!");
    return "About";  
  }

  // @GetMapping( path = "/ShoppingList/{id}")
  // String shoppingList() {
  //   return "shoppingList";  
  // }

  // @GetMapping( path = "/Search/{id}")
  // public String searching(@PathVariable(required = false) Integer recieveID, Map<String, Object> model ) {
  //   UserID idofuser = new UserID();
  //   idofuser.setUserID(recieveID);
  //   model.put("UserID", idofuser);
  //   System.out.println("about to go to searchhtml");
  //   return "search";  
  // }
  // @GetMapping(path = "/Home")
  //   public String getHomeNOID(Map<String, Object> model){
  //     return "home"; 
  //   }

 @GetMapping(path ="/Home/{id}")
  public String landingSpecialized(@PathVariable("id") Integer recieveID, Map<String, Object> model) {
    
    try (Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM Accounts WHERE id=" + recieveID); 
      
      User output = new User();

      while(rs.next()){
        if(recieveID == (rs.getInt("id"))){
          output.setName("" + rs.getObject("Username"));
          output.setRole("" + rs.getObject("Role"));
          output.setID(rs.getInt("id"));
          model.put("ret", output);
        }
      }
      System.out.println("IN THIS GETMAPPING with home/{id}");
      // if(output.getRole() == "seller"){
      //   UserID idofuser = new UserID();
      //   idofuser.setUserID(recieveID);
      //   model.put("UserID", idofuser);
      //   return "HomeSeller/" + output.getID();
      // }

      if(recieveID == 0){

        UserID idofuser = new UserID();
        idofuser.setUserID(0);
        model.put("UserID", idofuser);
        return "home";
      }
    // if(output.getRole() == "customer" || output.getRole() == "seller"){
      else{
        UserID idofuser = new UserID();
        idofuser.setUserID(recieveID);
        model.put("UserID", idofuser);
        return "home";
      }
      
      
    }

    catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }
  

@GetMapping(path = "/MyItem/{UserId}/{ItemId}")
public String myItem(@PathVariable("UserId") String idofuser, @PathVariable("ItemId") Integer ItemId, Map<String, Object> model) throws Exception{

  try (Connection connection = dataSource.getConnection()){
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM Items");
    
    Item output = new Item(); // store data

    Integer UserId = Integer.parseInt(idofuser);

    while (rs.next()) {
      if(rs.getInt("Id") == ItemId){
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
    
    model.put("retItem", output); //to display existing
    UserID Userid = new UserID();
    Userid.setUserID(UserId);
    model.put("retUserId", Userid); //to redirect

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
  public String getHomeSellerWithID(@PathVariable (required = false) String id, Map<String, Object> model){
    try (Connection connection = dataSource.getConnection()) {
      Integer parameterid = Integer.parseInt(id);
      if(id == null){
        Item in = new Item(); 
        model.put("Item", in);
        return "homeSeller";
      }
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Items (Id serial, Name varchar(80), Description varchar(200), Category varchar(20), Price float, image bytea, Stock Integer, SellerId Integer)");

      System.out.println("before query loop in homeseller id= "+parameterid);

      ResultSet rs = stmt.executeQuery("SELECT * FROM Items WHERE SellerId =" + id);
      ArrayList<Item> storeItem = new ArrayList<Item>();
      while(rs.next()){
        Item outputItem = new Item();
        outputItem.setName(rs.getString("Name"));
        outputItem.setCategory(rs.getString("Category"));
        outputItem.setStock(rs.getInt("Stock"));
        outputItem.setID(rs.getInt("ID"));
        storeItem.add(outputItem);
      }
      
      Item in = new Item(); 
      model.put("Item", in);

      UserID temp = new UserID();
      temp.setUserID(parameterid);
      model.put("UserId", temp);

      model.put("records", storeItem);

      return "homeSeller";

    }catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    } 
  }


@PostMapping(path = "/afterSubmitNewItem/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public String handleNewItem(Map<String, Object> model, Item Item, @PathVariable("id") String idofuser )  throws Exception{
    //saving the data obtained into databse
    try (Connection connection = dataSource.getConnection()) {
      Integer SellerId = Integer.parseInt(idofuser);
      System.out.println("1 inside aftersubmitnewitem/id with id = "+SellerId);

      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Items (Id serial, Name varchar(80), Description varchar(200), Category varchar(20), Price float, image bytea, Stock Integer, SellerId Integer)");
      String sql = "INSERT INTO Items (Name, Category, Description, Price, image, Stock, SellerId) VALUES ('" + Item.getName() +"','"+Item.getCategory() + "','" + Item.getDescription() + "','" + Item.getPrice() + "','" + Item.getImage() + "','" +  Item.getStock() + "','" +  SellerId + "')";
      stmt.executeUpdate(sql);
      System.out.println(Item.getName()+" "+ Item.getCategory()+" "+ Item.getPrice()+" "+ Item.getStock());
      

      // if (!file.isEmpty()) {
      //   byte[] fileBytes = file.getBytes();
      //   Item.setImage(fileBytes);
      // }
      
      //line below, item.getName etc.. all from parameters
      return "redirect:/HomeSeller/" + SellerId;
  }
  catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }
}


@PostMapping(
  path = "/DELETEsp/{UserId}/{ItemId}",
  consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
)
public String handleDeleteButtonForShoppingList(@PathVariable("UserId") Integer UserId, @PathVariable("ItemId") Integer ItemId, Map<String, Object> model) throws Exception {
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    ResultSet rsTemp = stmt.executeQuery("SELECT * FROM Accounts WHERE id=" + UserId); //THIS NEEDS TO GO INTO USER ARRAY TO DELETE NUMBER

    ArrayList<Integer> tempStoreItemID = new ArrayList<Integer>();

      if(rsTemp.next()){
          Array temp = rsTemp.getArray("ShoppingList");

          if(temp != null){
            Integer[] temp2 = (Integer[])temp.getArray();

            for(int i = 0; i < temp2.length; i++){
              if(temp2[i] != null)
                tempStoreItemID.add(temp2[i]);
            }
          }

          if(tempStoreItemID.contains(ItemId)){ //remove from array if exist
            tempStoreItemID.remove(ItemId);
          }

          Integer newIDList[] = new Integer[tempStoreItemID.size()];
          newIDList = tempStoreItemID.toArray(newIDList);

          System.out.println("User = " + UserId); //print out to confirm correct
          System.out.println("Item = " + ItemId);
          for (Integer obj : newIDList)
                System.out.println(obj);


          StringBuilder string = new StringBuilder();
          string.append("{");
          for(int i = 0; i < newIDList.length; i++){
             string.append(newIDList[i]);
             if(i != newIDList.length - 1)
              string.append(",");
          }
          string.append("}");
          System.out.println(string);    
          stmt.executeUpdate("UPDATE Accounts SET ShoppingList = '" + string + "' WHERE id = " + UserId);
      }

      UserID temp = new UserID();
      temp.setUserID(UserId);
      model.put("UserId", temp);

      return "successDeleteShoppingList";

  } catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }
}

// @GetMapping(path="/ShoppingList")
// public String getShoppingListNoID(Map<String, Object> model) throws Exception{
//   return "shoppingList";
// }

 
@GetMapping(path="/ShoppingList/{id}")
public String getShoppingList(@PathVariable("id") String recievedID, Map<String, Object> model) throws Exception{
  try (Connection connection = dataSource.getConnection()) {
    System.out.println("Currently in shoppinglist");

    Integer recID = Integer.parseInt(recievedID);         //converting recievedID into integer!!
    Statement stmt = connection.createStatement();

    if(recID == 0){
      Account account = new Account();
      model.put("account", account);

      UserID idofuser = new UserID();
      idofuser.setUserID(0);
      model.put("UserID", idofuser);
      return "guestlogin";
    }
    ResultSet rs = stmt.executeQuery("SELECT * FROM Accounts WHERE id =" + recID);

    if(rs.next()){
      Array temp = rs.getArray("ShoppingList");
  
      ArrayList<Integer> tempStoreItemID = new ArrayList<Integer>();

      if(temp != null){ //if array not empty
        Integer[] temp2 = (Integer[])temp.getArray();

        for(int i = 0; i < temp2.length; i++){
          if(temp2[i] != null)
            tempStoreItemID.add(temp2[i]); //copy to arraylist
        }
      }

      ArrayList<Item> storeItems = new ArrayList<Item>(); //store all the items

      for(int i = 0; i < tempStoreItemID.size(); i++){
        ResultSet rsTemp = stmt.executeQuery("SELECT * FROM Items WHERE id =" + tempStoreItemID.get(i));
        if(rsTemp.next()){
          Item outputItem = new Item();
          outputItem.setName(rsTemp.getString("Name"));
          outputItem.setDescription(rsTemp.getString("Description"));
          outputItem.setPrice(rsTemp.getFloat("Price"));
          outputItem.setID(rsTemp.getInt("ID"));
          storeItems.add(outputItem);
        }
      }
    

      UserID tempId = new UserID();
      tempId.setUserID(recID);
      model.put("UserID", tempId);

      model.put("records", storeItems); //iterate all shopping list item and link them to respective page
    }
    
    return "shoppingList";

  }
  catch (Exception e){
    model.put("message", e.getMessage());
    return "error";
  }
}

@PostMapping(
  path = "/DELETEmi/{uid}/{id}",
  consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
)
public String handleDeleteButtonForMyItem(@PathVariable("uid") Integer UserId, @PathVariable("id") Integer ItemId, Map<String, Object> model) throws Exception {
  try (Connection connection = dataSource.getConnection()) {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("DELETE FROM Items WHERE id=" + ItemId);

    UserID temp = new UserID();
    temp.setUserID(UserId);
    model.put("UserId", temp);

    return "SuccessDeleteMyItem";

  } catch (Exception e) {
    model.put("message", e.getMessage());
    return "error";
  }
}

  @PostMapping( 
    path = "/UPDATEmi/{uid}/{id}",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleUpdateButtonforMyItem(@PathVariable("uid") Integer UserId, @PathVariable("id") Integer receiveID, Map<String, Object> model, Item item) throws Exception {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      System.out.println(item.getName());
      stmt.executeUpdate("UPDATE Items SET Name ='" + item.getName() + "', Category = '" + item.getCategory() + "', Description = '" + item.getDescription() + "', Price = '" + item.getPrice() + "', Stock = '" + item.getStock() + "' WHERE id =" + receiveID + ";");
      
      UserID temp = new UserID();
      temp.setUserID(UserId);
      model.put("UserId", temp);

      return "successUpdate"; 
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }

  }

  @GetMapping(
    path = "/Login/{id}"
  )
  public String getLogin(@PathVariable("id") String recievedID, Map<String, Object> model) throws Exception{
    Account account = new Account();
    model.put("account", account);
    Integer recID = Integer.parseInt(recievedID);


    UserID idofuser = new UserID();
    idofuser.setUserID(recID);
    model.put("UserID", idofuser);
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
          model.put("UserID", userid);
          return "redirect:/Home/"+id;
        }else{
          System.out.println("Success123");
          System.out.println(id);
          UserID userid = new UserID();
          ItemID itemid = new ItemID();

          userid.setUserID(id);
          model.put("userID", userid);
          model.put("Item", itemid);
          System.out.println("inside the login post!!!! going to sellerhome id= "+id);
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
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Accounts (Id serial, Username varchar(20), Password varchar(16), Role varchar(16),Shoppinglist Integer[1], Sellinglist Integer[1])");
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
    public String getSearch(@PathVariable ("id") Integer id, Map<String, Object> model){
      try (Connection connection = dataSource.getConnection()) {
        if(id == 0){
          Searchname item = new Searchname() ;
          ItemID ID = new ItemID();
          UserID userID = new UserID();
          userID.setUserID(id);
          System.out.println(userID.getUserID());

          model.put("UserID",userID);    //creates a new empty Item object
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
            model.put("UserID", userID);
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


@PostMapping(path = {"/Searchcategory/{id}","/Searchcategory"}, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  public String searchcategory(Map<String, Object> model, @PathVariable (required = false) Integer id, Searchname item) throws Exception{
    //saving the data obtained into databse
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Items (Id serial, Name varchar(80), Description varchar(200), Category varchar(20), Price float, image bytea, Stock Integer)");
      ResultSet rs = stmt.executeQuery("SELECT * FROM Items WHERE Category = "+ item);
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




@PostMapping(path = {"/AddToShoppingListMyItem/{Userid}/{Itemid}","/AddToShoppingListMyItem"}, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
  public String AddItemShoppingList(Map<String, Object> model, @PathVariable ("Userid") Integer UserId, @PathVariable ("Itemid") Integer ItemId, Searchname item) throws Exception{
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      System.out.println(UserId);
      ResultSet rsTemp = stmt.executeQuery("SELECT * FROM Items WHERE id =" + ItemId);
      ArrayList<Integer> tempStoreItemID = new ArrayList<Integer>();

      if(rsTemp.next()) {

        Integer storeItemId = rsTemp.getInt("Id"); // get Item ID

        ResultSet rs = stmt.executeQuery("SELECT * FROM Accounts WHERE id =" + UserId);
        if(rs.next()){
          Array temp = rs.getArray("ShoppingList");

          if(temp != null){
            Integer[] temp2 = (Integer[])temp.getArray();

            for(int i = 0; i < temp2.length; i++){
              if(temp2[i] != null)
                tempStoreItemID.add(temp2[i]);
            }
          }

          if(!(tempStoreItemID.contains(storeItemId))){    //find a way to copy array / make sure no removed items in existing array
            tempStoreItemID.add(storeItemId);
          }

          Integer newIDList[] = new Integer[tempStoreItemID.size()];
          newIDList = tempStoreItemID.toArray(newIDList);

          System.out.println("User = " + UserId); //print out to confirm correct
          System.out.println("Item = " + storeItemId);
          for (Integer obj : newIDList)
                System.out.println(obj);


          StringBuilder string = new StringBuilder();
          string.append("{");
          for(int i = 0; i < newIDList.length; i++){
             string.append(newIDList[i]);
             if(i != newIDList.length - 1)
              string.append(",");
          }
          string.append("}");
          System.out.println(string);    
          stmt.executeUpdate("UPDATE Accounts SET ShoppingList = '" + string + "' WHERE id = " + UserId);
        }
      }
    
      // if (!file.isEmpty()) {
      //   byte[] fileBytes = file.getBytes();
      //   Item.setImage(fileBytes);
      // }
      return "redirect:MyItem/" + UserId + "/" + ItemId;
  }
  catch (Exception e) {
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