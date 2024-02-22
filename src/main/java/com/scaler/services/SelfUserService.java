package com.scaler.services;

import com.scaler.Exceptions.TokenNotExistsOrAlreadyExpiredException;
import com.scaler.models.Token;
import com.scaler.models.User;
import com.scaler.repositories.TokenRepository;
import com.scaler.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service("SelfUserService")
public class SelfUserService implements UserService {

    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public SelfUserService(UserRepository userRepository, TokenRepository tokenRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
        this.userRepository=userRepository;
        this.tokenRepository=tokenRepository;
    }
    @Override
    public Token Login(String email, String password) {
        Optional<User> user= userRepository.findByEmail(email);
        if(user.isEmpty()){
            return null;
        }
        if(!bCryptPasswordEncoder.matches(password, user.get().getHashedPassword())){
            return null;
        }

        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysLater = today.plus(30, ChronoUnit.DAYS);

        // Convert LocalDate to Date
        Date expiryDate = Date.from(thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Token tkn=new Token();
        tkn.setUser(user.get());
        tkn.setExpiryAt(expiryDate);
        tkn.setValue(RandomStringUtils.randomAlphabetic(128));
        Token savedToken = tokenRepository.save(tkn);
        return savedToken;
    }

    @Override
    public User SignUp(String name, String email, String password) {
    User u =new User();
    u.setName(name);
    u.setEmail(email);
    u.setHashedPassword(bCryptPasswordEncoder.encode(password));
        return userRepository.save(u);
    }

    @Override
    public void Logout(String token) throws TokenNotExistsOrAlreadyExpiredException {
        Optional<Token> optionalToken=tokenRepository.findByValueAndDeletedEquals(token, false);
        if(optionalToken.isEmpty())
            throw new TokenNotExistsOrAlreadyExpiredException("Token does not exist in storage.");
        Token tkn= optionalToken.get();
        tkn.setDeleted(true);
        tokenRepository.save(tkn);
        return;
    }

    @Override
    public User ValidateToken(String token) throws TokenNotExistsOrAlreadyExpiredException {
        Optional<Token> optionalToken=tokenRepository.findByValueAndDeletedEqualsAndExpiryAtGreaterThan(token, false, new Date());
        if(optionalToken.isEmpty()){
            throw new TokenNotExistsOrAlreadyExpiredException("Token does not exist in storage.");
        }
        return optionalToken.get().getUser();
    }
}
