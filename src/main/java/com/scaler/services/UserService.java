package com.scaler.services;

import com.scaler.models.User;

public interface UserService {

    User Login(String email, String password);
    User SignUp(String name, String email, String password);
    Boolean Logout(String token);
}
