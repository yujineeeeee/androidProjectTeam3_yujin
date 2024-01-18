package com.bitc.springapp.service;

import com.bitc.springapp.dto.UserDto;

public interface UserService {
    UserDto SelectUserInfo(String id) throws Exception;

    String userLogin(String id, String pw) throws Exception;

    int UserIdCheck(String id) throws Exception;
}
