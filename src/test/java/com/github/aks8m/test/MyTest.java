package com.github.aks8m.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MyTest {

    @Test
    public void testFail(){
        fail("This is my test");
    }

    @Test
    public void testPass(){
        assertTrue(true);
    }

}
