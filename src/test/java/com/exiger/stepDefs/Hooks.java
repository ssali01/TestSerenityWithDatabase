package com.exiger.stepDefs;

import com.exiger.utilities.DB_Util;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {

//    @Before("@api")
//    public static void setup(){
//        baseURI  = "http://library2.cydeo.com";
//    }

    @Before("@db")
    public void dbHook() {
        System.out.println("---> HOOKS --->creating database connection");
        DB_Util.createConnection();
    }

    @After("@db")
    public void afterDbHook() {
        System.out.println("---> HOOKS ---> closing database connection");
        DB_Util.destroy();
    }
  }

