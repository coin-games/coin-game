package com.cgs.backend.security;


import com.cgs.backend.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;
    private SecretKey key;

    @Value("${jwt.access-expiration}")
    private long accessTokenExpiration;

    @Getter
    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpiration;


    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(User user) {
        return createToken(user.getId(), accessTokenExpiration);
    }

    public String createAccessToken(String userId) {
        return createToken(userId, accessTokenExpiration);
    }

    public String createRefreshToken(User user) {
        return createToken(user.getId(), refreshTokenExpiration);
    }

    //Jwt 생성
    private String createToken(String userId, long expiration) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //Jwt에서 userId 추출
    public String getUserId(String token) {
        return parseClaims(token).getBody().getSubject();
    }

    //Jwt 유효성 검증
    public TokenValidationResult validateToken(String token) {
        try {
            parseClaims(token);
            return TokenValidationResult.VALID;
        } catch (ExpiredJwtException e) {   //유효한 토큰 만료
            return TokenValidationResult.EXPIRED;
        } catch (SignatureException e) {    //서명 올바르지 않음 (변조 의심)
            return TokenValidationResult.INVALID_SIGNATURE;
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {  //토큰 형식 잘못
            return TokenValidationResult.MALFORMED;
        }
    }

    private Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}
