package com.github.aks8m.it;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MyIT {

    @Test
    public void testFail(){
        fail("This is my IT");
    }

    @Test
    public void testPass(){
        assertTrue(true);
    }

}
