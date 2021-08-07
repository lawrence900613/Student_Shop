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

class ItemTest {
    static Item i;

    @BeforeAll
    static void setUp() {
        i = new Item();
        i.setName("Mango");
        i.setID(1);
        i.setPrice((float) 3.0);
        i.setDescription("fruit");
        
    }

    @Test
    public void searchItemTest() throws Exception{
        assertEquals("Mango", i.getName());
        assertEquals(1, i.getID());
        assertEquals((float) 3.0, i.getPrice());
        assertEquals("fruit", i.getDescription());
    }    
}
