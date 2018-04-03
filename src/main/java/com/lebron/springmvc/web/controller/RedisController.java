package com.lebron.springmvc.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lebron.springmvc.util.redis.JedisClient;

@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private JedisClient jedisClient;
    
    @GetMapping("/{key}")
    public Object get(@PathVariable String key){
        return jedisClient.get(key);
    }

    @PostMapping("")
    public void get(@RequestParam(required=true)String key, @RequestParam(required=true)String value, Integer seconds){
        seconds = seconds == null ? 10 : seconds;
        jedisClient.set(key, value, seconds);
    }
    
    
}
