package com.lebron.springmvc.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lebron.springmvc.model.User;
import com.lebron.springmvc.service.UserService;

@Service
public class UserServiceImpl extends BaseService<User> implements UserService{

    @Override
    public int addUser(User user) {
        user.setId(getUUID());
        return super.insert(user);
    }

    @Override
    public int delUser(String id) {
        return super.deleteById(id);
    }

    @Override
    public int updateUser(User user) {
        return super.updateSelective(user);
    }

    @Override
    public User getUser(String id) {
        return super.selectByPrimaryKey(id);
    }

    @Override
    public List<User> listUser() {
        User record = new User();
        record.setDeleteFlag(0);
        return super.selectList(record);
    }


}
