package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.matchers.JUnitMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.ArgumentMatchers.any;


// @SpringBootTest
// @AutoConfigureMockMvc
//@WebMvcTest
class ShoppingListTest{

    // @Autowired
    // private MockMvc mvc;

    // @MockBean
    static Account p1; //seller
    static Account p2; //customer
    static Item i;

    static UserID user;

    @BeforeAll
    static void setUp(){

        p1 = new Account();
        Integer[] test = {1,2};
        p1.setRole("customer");
        p1.setID(1);
        p1.setShoppingList(test);

        p2 = new Account();
        p2.setRole("seller");
        p2.setID(2);
        p2.setShoppingList(test);

        i = new Item();
        i.setName("Apple");
        i.setSellerID(2);

        user = new UserID();
        user.setUserID(12);
    }

    @Test
    public void removeItemfromSL() throws Exception{

        // RequestBuilder request = MockMvcRequestBuilders.get("/ShoppingList/1");
        // MvcResult result = mvc.perform(request).andReturn();
        // assertEquals("bobby",result.getResponse().getCOntentAsString());

        // mvc.perform(MockMvcRequestBuilders.get("/ShoppingList/{id}", 1))
        //         .andDo(print())
        //         .andExpect(status().isOk())
        //         .andExpect(content().string(containsString("Hello, World")));

        //assertEquals([1,2],p1.getShoppingList());
        assertEquals(2,i.getSellerID());
        assertEquals("Apple",i.getName());

        assertEquals(12,user.getUserID());
        user.setUserID(45);
        assertEquals(45,user.getUserID());
        
    }

}
