package com.bitc.springapp.service;

import com.bitc.springapp.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto SelectUserInfo(String id) throws Exception;

    String userLogin(String id, String pw) throws Exception;

    int UserIdCheck(String id) throws Exception;

    void UserInsert(UserDto user) throws Exception;

    void UserUpdate(UserDto user) throws Exception;

    void UserDelete(String id) throws Exception;
}
