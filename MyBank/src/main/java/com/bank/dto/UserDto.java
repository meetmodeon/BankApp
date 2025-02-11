package com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String firstname;
    private String lastname;
    private String username;
    private Date dob;
    private long tel;
    private String password;
    private String gender;
}
