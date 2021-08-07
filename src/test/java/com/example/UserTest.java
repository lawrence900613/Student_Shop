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

import java.util.HashMap;
import java.util.Map;

import com.github.dockerjava.api.model.SearchItem;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.matchers.JUnitMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.ArgumentMatchers.any;

class UserTest {
    static Account a;
    static Account b;

    @BeforeAll
    static void setUp() {
        a = new Account();
        a.setID(1);
        a.setPassword("123");
        a.setRole("seller");
        a.setUsername("seller1");
        
        b = new Account();
        b.setID(2);
        b.setPassword("123");
        b.setRole("customer");
        b.setUsername("customer1");
    }

    @Test
    public void userCheckTest() throws Exception {
        assertEquals(1, a.getID());
        assertEquals("123", a.getPassword());
        assertEquals("seller", a.getRole());
        assertEquals("seller1", a.getUsername());

        assertEquals(2, b.getID());
        assertEquals("123", b.getPassword());
        assertEquals("customer", b.getRole());
        assertEquals("customer1", b.getUsername());
    }
}