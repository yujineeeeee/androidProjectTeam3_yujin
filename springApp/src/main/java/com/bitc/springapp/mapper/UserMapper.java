package com.bitc.springapp.mapper;

import com.bitc.springapp.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    UserDto SelectUserInfo(String id) throws Exception;

    String UserLogin(String id, String pw) throws Exception;

    int UserIdCheck(String id) throws Exception;

    void UserInsert(UserDto user) throws Exception;

    void UserUpdate(UserDto user) throws Exception;

    void UserDelete(String id) throws Exception;
}
