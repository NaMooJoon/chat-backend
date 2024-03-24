package com.handong.chat.config.jwt;

import com.handong.chat.config.auth.LoginUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final ExternalProperties properties;
    private final JwtProcess jwtProcess;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, ExternalProperties properties,
                                  JwtProcess jwtProcess) {
        super(authenticationManager);
        this.properties = properties;
        this.jwtProcess = jwtProcess;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (isTokenValidated(request)) {
            String accessToken = request.getHeader(properties.getAccessKey())
                    .replace(properties.getTokenPrefix(), "");
            LoginUser loginUser = jwtProcess.verifyAccessToken(accessToken);

            Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser, null,
                    loginUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    private boolean isTokenValidated(HttpServletRequest request) {
        String token = request.getHeader(properties.getAccessKey());
        return (token != null && token.startsWith(properties.getTokenPrefix()));
    }
}
