package com.bitc.springapp.mapper;

import com.bitc.springapp.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    UserDto SelectUserInfo(String id) throws Exception;

    String UserLogin(String id, String pw) throws Exception;

    int UserIdCheck(String id) throws Exception;
}
