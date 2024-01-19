package com.bitc.springapp.dto;

import lombok.Data;
@Data
public class UserDto {
    private String id;
    private String pw;
    private String name;
    private String email;
    private String phone;
    private String createDate;
}
