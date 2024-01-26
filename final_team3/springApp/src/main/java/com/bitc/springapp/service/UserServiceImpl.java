package com.bitc.springapp.service;

import com.bitc.springapp.dto.UserDto;
import com.bitc.springapp.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDto SelectUserInfo(String id) throws Exception {
        return userMapper.SelectUserInfo(id);
    }

    @Override
    public String userLogin(String id, String pw) throws Exception {
        return userMapper.UserLogin(id, pw);
    }

    @Override
    public int UserIdCheck(String id) throws Exception {
        return userMapper.UserIdCheck(id);
    }

    @Override
    public void UserInsert(UserDto user) throws Exception {
        userMapper.UserInsert(user);
    }

    @Override
    public void UserUpdate(UserDto user) throws Exception {
        userMapper.UserUpdate(user);
    }

    @Override
    public void UserDelete(String id) throws Exception {
        userMapper.UserDelete(id);
    }

}



