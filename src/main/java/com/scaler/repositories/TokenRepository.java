package com.scaler.repositories;

import com.scaler.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Time;
import java.util.Date;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token save(Token token);
    Optional<Token> findByValueAndDeletedEquals(String value, boolean isDeleted);
    Optional<Token> deleteByValue(String value);
    Optional<Token> findByValueAndDeletedEqualsAndExpiryAtGreaterThan(String value, boolean isDeleted, Date expiryGreaterThan);

}
