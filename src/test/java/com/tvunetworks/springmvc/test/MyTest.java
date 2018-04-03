package com.tvunetworks.springmvc.test;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.lebron.springmvc.util.ThreadPoolUtil;

public class MyTest {
    
    public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
        ThreadPoolUtil.excute(new MyTask());
    }
}

class MyTask implements Runnable {

    public void run() {
        System.out.println("1");
    }
}
