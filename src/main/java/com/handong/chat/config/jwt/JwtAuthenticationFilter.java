package com.handong.chat.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.handong.chat.config.auth.LoginUser;
import com.handong.chat.dto.user.UserRequestDto.LoginRequestDto;
import com.handong.chat.dto.user.UserResponseDto.LoginResponseDto;
import com.handong.chat.util.CustomResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ExternalProperties properties;
    private final JwtProcess jwtProcess;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, ExternalProperties properties,
                                   JwtProcess jwtProcess) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.properties = properties;
        this.jwtProcess = jwtProcess;
        setFilterProcessesUrl("/api/login");
    }


    // POST: /api/login
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        log.debug("/api/login => start the Authentication");
        try {
            ObjectMapper om = new ObjectMapper();
            LoginRequestDto loginReqDto = om.readValue(request.getInputStream(), LoginRequestDto.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginReqDto.getUsername(),
                    loginReqDto.getPassword()
            );

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return authentication;
        } catch (Exception e) {
            log.debug("DEBUG : /api/login => attemptAuthentication is called");
            log.debug("DEBUG : InternalAuthenticationServiceException");
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        LoginUser loginUser = (LoginUser) authResult.getPrincipal();
        String accessToken = jwtProcess.createAccessToken(loginUser);
        response.addHeader(properties.getAccessKey(), accessToken);

        LoginResponseDto loginResDto = new LoginResponseDto(loginUser.getUser());
        CustomResponseUtil.success(response, "Login success", loginResDto);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        CustomResponseUtil.fail(response, "Login failed...", HttpStatus.UNAUTHORIZED);
    }
}
