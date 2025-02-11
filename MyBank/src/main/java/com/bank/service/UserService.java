package com.bank.service;

import com.bank.dto.UserDto;
import com.bank.entity.User;
import com.bank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    public User registerUser(UserDto userDto){
        User user= mapToUser(userDto);
        return userRepository.save(user);

    }

    private User mapToUser(UserDto userDto){
        return User.builder()
                .lastName(userDto.getLastname())
                .firstName(userDto.getFirstname())
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .dob(userDto.getDob())
                .roles(List.of("USER"))
                .tag("io_"+ userDto.getUsername())
                .build();
    }

    public Map<String, Object> authenticateUser(UserDto userDto){
        Map<String, Object> authObject = new HashMap<>();

        User user= (User) userDetailsService.loadUserByUsername(userDto.getUsername());
        if(user == null){
            throw new UsernameNotFoundException("User not Found");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(),userDto.getPassword()));
        authObject.put("token","Bearer ".concat(jwtService.generateToken(userDto.getUsername())));
        authObject.put("user",user);
        return authObject;
    }
}
