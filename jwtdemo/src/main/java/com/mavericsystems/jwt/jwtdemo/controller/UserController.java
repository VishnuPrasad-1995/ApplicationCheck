package com.mavericsystems.jwt.jwtdemo.controller;


import com.mavericsystems.jwt.jwtdemo.model.JWTRequest;
import com.mavericsystems.jwt.jwtdemo.model.JWTResponse;
import com.mavericsystems.jwt.jwtdemo.service.UserService;
import com.mavericsystems.jwt.jwtdemo.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public String home() {
        return "Welcome to spring config";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTResponse> authenticate(@RequestBody JWTRequest jwtRequest) throws Exception {
        try {
             authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
        } catch (BadCredentialsException ex) {
            throw new Exception("Invalid credentials", ex);
        }

        final UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());

        final String token = jwtUtility.generateToken(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(new JWTResponse(token));
    }
}
