package com.scaler.controllers;

import com.scaler.DTOs.LoginRequestDTO;
import com.scaler.DTOs.LogoutRequestDTO;
import com.scaler.DTOs.SignUpRequestDTO;
import com.scaler.Exceptions.TokenNotExistsOrAlreadyExpiredException;
import com.scaler.models.Token;
import com.scaler.models.User;
import com.scaler.services.UserService;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService= userService;

    }

    @PostMapping("/users/login")
    public Token login(@RequestBody LoginRequestDTO loginRequestDTO) {
        // check if email and password in db
        // if yes return user
        // else throw some error
        String email=loginRequestDTO.getEmail();
        String password=loginRequestDTO.getPassword();

        return userService.Login(email,password);
    }

    @PostMapping("/users/signup")
    public User signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        // no need to hash password for now
        // just store user as is in the db
        // for now no need to have email verification either
        String email=signUpRequestDTO.getEmail();
        String password= signUpRequestDTO.getPassword();
        String name=signUpRequestDTO.getName();
        return userService.SignUp(name, email, password);
    }

    @PostMapping("/users/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDTO logoutRequestDTO) throws TokenNotExistsOrAlreadyExpiredException {
        // delete token if exists -> 200
        // if doesn't exist give a 404

        userService.Logout(logoutRequestDTO.getToken());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/validate/{token}")
    public User ValidateToken(@PathVariable("token") @NonNull String token) throws TokenNotExistsOrAlreadyExpiredException{
        return userService.ValidateToken(token);
    }
}
