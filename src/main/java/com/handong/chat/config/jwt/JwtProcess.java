package com.handong.chat.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.handong.chat.config.auth.LoginUser;
import com.handong.chat.domain.user.User;
import com.handong.chat.domain.user.UserEnum;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtProcess {

    private final ExternalProperties properties;

    public Algorithm getTokenAlgorithm() {
        return Algorithm.HMAC512(properties.getSecretKey());
    }

    public String createAccessToken(LoginUser loginUser) {
        String jwtToken = JWT.create()
                .withSubject("accessToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + properties.getAccessTokenExpirationTime()))
                .withClaim("id", loginUser.getUser().getId())
                .withClaim("role", loginUser.getUser().getRole() + "")
                .withClaim("username", loginUser.getUsername())
                .sign(getTokenAlgorithm());
        return properties.getTokenPrefix() + jwtToken;
    }

    public LoginUser verifyAccessToken(String accessToken) {
        DecodedJWT decodedJWT = JWT.require(getTokenAlgorithm())
                .build().verify(accessToken);
        Long id = decodedJWT.getClaim("id").asLong();
        String role = decodedJWT.getClaim("role").asString();
        String username = decodedJWT.getClaim("username").asString();
        User user = User.builder()
                .id(id)
                .role(UserEnum.valueOf(role))
                .username(username)
                .build();
        return new LoginUser(user);
    }
}
