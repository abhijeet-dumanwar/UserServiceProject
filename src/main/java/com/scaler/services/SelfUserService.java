package com.scaler.services;

import com.scaler.models.Token;
import com.scaler.models.User;
import com.scaler.repositories.TokenRepository;
import com.scaler.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("SelfUserService")
public class SelfUserService implements UserService {

    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    public SelfUserService(UserRepository userRepository, TokenRepository tokenRepository){
        this.userRepository=userRepository;
        this.tokenRepository=tokenRepository;
    }
    @Override
    public User Login(String email, String password) {
        Optional<User> user= userRepository.findByEmail(email);
        if(user.isEmpty()){
            return null;
        }
        Token tkn=new Token();
        tkn.setValue(user.get().getName()+"'s token");
        tokenRepository.save(tkn);
        return user.get();
    }

    @Override
    public User SignUp(String name, String email, String password) {
    User u =new User();
    u.setName(name);
    u.setEmail(email);
    u.setHashedPassword(password);
        return userRepository.save(u);
    }

    @Override
    public Boolean Logout(String token) {
        Optional<Token> optionalToken=tokenRepository.findByValueAndDeletedEquals(token, false);
        if(optionalToken.isEmpty()) return false;
        Token tkn= optionalToken.get();
        Optional<Token> optionalToken1 = tokenRepository.deleteByValue(tkn.getValue());
        return true;
    }
}
