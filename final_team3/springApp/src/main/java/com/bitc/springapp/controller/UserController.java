package com.bitc.springapp.controller;

import com.bitc.springapp.dto.UserDto;
import com.bitc.springapp.dto.UserLoginDto;
import com.bitc.springapp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/userInfo")
    public UserDto userInfo(HttpServletRequest request) throws Exception{

        String id = request.getParameter("id");

        UserDto user = userService.SelectUserInfo(id);

        return user;
    }

    @PostMapping("/userLogin")
    @ResponseBody
    public int userLogin(@RequestBody UserLoginDto user) throws Exception{

        int result = Integer.parseInt(userService.userLogin(user.getId(), user.getPw()));

        return result;
    }

    @GetMapping("/userIdCheck")
    public int userIdCheck(HttpServletRequest request) throws Exception {

        String id = request.getParameter("id");

        int result = userService.UserIdCheck(id);

        return result;
    }

    @PostMapping("/userInsert")
    @ResponseBody
    public int userInsert(@RequestBody UserDto user) throws Exception{
        userService.UserInsert(user);

        return 1;
    }

    @PostMapping("/userUpdate")
    @ResponseBody
    public int userUpdate(@RequestBody UserDto user) throws Exception{
        userService.UserUpdate(user);

        return 1;
    }

    @GetMapping("/userDelete")
    public int userDelete(HttpServletRequest request) throws Exception{
        String id = request.getParameter("id");

        userService.UserDelete(id);

        return 1;
    }

}