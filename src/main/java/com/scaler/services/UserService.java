package com.scaler.services;

import com.scaler.Exceptions.TokenNotExistsOrAlreadyExpiredException;
import com.scaler.models.Token;
import com.scaler.models.User;

public interface UserService {

    Token Login(String email, String password);
    User SignUp(String name, String email, String password);
    void Logout(String token) throws TokenNotExistsOrAlreadyExpiredException;
    User ValidateToken(String token) throws TokenNotExistsOrAlreadyExpiredException;
}
