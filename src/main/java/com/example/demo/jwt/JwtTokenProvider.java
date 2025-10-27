package com.example.demo.jwt;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.demo.enums.TokenType;
import com.example.demo.model.Token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secret;

    public Token generateAccessToken(
        Map<String, Object> extraClaims,
        Long duration,
        TemporalUnit durationType,
        UserDetails user
    )
    {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plus(duration, durationType);
        String token = Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(user.getUsername())
        .setIssuedAt(toDate(now))
        .setExpiration(toDate(expiryDate))
        .signWith(decodeSecretKey(secret), SignatureAlgorithm.HS256)
        .compact();
        return new Token(TokenType.ACCESS, token, expiryDate, false, null);
    }

    public Token generateRefreshToken(
        Long duration,
        TemporalUnit durationType,
        UserDetails user
    )
    {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plus(duration, durationType);
        String token = Jwts.builder()
        .setSubject(user.getUsername())
        .setIssuedAt(toDate(now))
        .setExpiration(toDate(expiryDate))
        .signWith(decodeSecretKey(secret), SignatureAlgorithm.HS256)
        .compact();
        return new Token(TokenType.REFRESH, token, expiryDate, false, null);
    }

    private <T>T extractClaim(String token, Function<Claims,T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(decodeSecretKey(secret)).build().parseClaimsJwt(token).getBody();
    }

    public LocalDateTime getExpiryDate(String token){
        return toLocalDateTime(extractClaim(token,Claims::getExpiration));
    }

    public LocalDateTime toLocalDateTime(Date date){
        return date.toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime();
    }

    public String getUsername(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    public boolean validateToken(String token) {
        if (token == null) { return false; }
        try{
            Jwts.parserBuilder().setSigningKey(decodeSecretKey(token)).build().parseClaimsJwt(token);
            return true;
        }
        catch(JwtException e) {
            return false;
        }
    }

    private Date toDate(LocalDateTime time){
        return Date.from(time.toInstant(ZoneOffset.UTC));
    }

    private Key decodeSecretKey(String secret){
        byte[] decodeKey = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(decodeKey);
    }
}
