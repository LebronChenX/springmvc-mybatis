package com.lebron.springmvc.service;


import java.util.List;

import com.lebron.springmvc.model.User;

public interface UserService {
    
    int addUser(User user);

    int delUser(String id);

    int updateUser(User user);
    
    User getUser(String id);

    List<User> listUser();
}