package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

class simpleUserIDTest {
    static UserID user;

    @BeforeAll
    static void setUp(){
        user = new UserID();
        user.setUserID(12);
    }

    @Test
    public void testUserID(){
        assertEquals(12,user.getUserID());
        user.setUserID(45);
        assertEquals(45,user.getUserID());
    }

}
