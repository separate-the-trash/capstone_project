package com.example.demo.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.config.auth.UserAccount;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.example.demo.config.jwt.JwtProperties.*;

@Component
public class JwtProcessor {

    //토큰 생성
    //userAccount를 받아서 JwtToken을 생성하고 반환.
    public String createJwtToken(UserAccount userAccount){
        Date now = new Date();
        return JWT.create()
                .withSubject(userAccount.getUsername()) // 토큰 제목 ( username ) 으로 설정
                .withIssuedAt(now)  // 현재 시간
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 만료 시간
                .withClaim("id", userAccount.getUser().getId())
                .withClaim("username", userAccount.getUser().getUsername())
                .sign(Algorithm.HMAC512(SECRET));
    }

    // JwtToken을 받으면 secretKey를 사용해서 지정된 claim을 반환한다.
    public String decodeJwtToken(String jwtToken, String secretKey, String claim) {
        return JWT.require(Algorithm.HMAC512(secretKey)).build()
                .verify(jwtToken)
                .getClaim(claim)
                .asString();
    }

    // Bearer "를 제거하고 뒤의 순수한 Jwt Token만을 추출한다.
    public String extractBearer(String jwtHeader) {
        int pos = jwtHeader.lastIndexOf(" ");
        return jwtHeader.substring(pos + 1);
    }
}
