package com.lebron.springmvc.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lebron.springmvc.model.User;
import com.lebron.springmvc.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping()
    public int addUser(User user) {
        return userService.addUser(user);
    }

    @PutMapping()
    public int updateUser(User user, @RequestParam(required = true) String id) {
        return userService.updateUser(user);
    }

    @DeleteMapping()
    //消息需要设置为POST类型，添加参数 _method = DELETE
    public int deleteUser(@RequestParam(required = true) String id) {
        return userService.delUser(id);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {
        return userService.getUser(id);
    }

    @GetMapping()
    public List<User> listUser() {
        return userService.listUser();
    }
}
