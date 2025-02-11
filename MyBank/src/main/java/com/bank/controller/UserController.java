package com.bank.controller;

import com.bank.dto.UserDto;
import com.bank.entity.User;
import com.bank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.registerUser(userDto));
    }

    @PostMapping("/auth")
    public ResponseEntity<?> authenticateUser(@RequestBody UserDto userDto){
        var authObject = userService. authenticateUser(userDto);
        ResponseEntity<?> response = ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION,(String) authObject.get("token")).body(authObject.get("user"));
        return response;
    }
}
