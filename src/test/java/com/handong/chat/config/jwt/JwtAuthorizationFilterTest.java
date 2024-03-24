package com.handong.chat.config.jwt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.handong.chat.config.auth.LoginUser;
import com.handong.chat.domain.user.User;
import com.handong.chat.domain.user.UserEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
class JwtAuthorizationFilterTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JwtProcess jwtProcess;
    @Autowired
    private ExternalProperties properties;

    @Test
    void authorization_success_test() throws Exception {
        // given
        LoginUser loginUser = new LoginUser(User.builder()
                .id(1L)
                .username("mamoo")
                .role(UserEnum.USER)
                .build());
        String token = jwtProcess.createAccessToken(loginUser);
        // when
        ResultActions resultActions = mvc.perform(get("/api/user/hello").header(properties.getAccessKey(), token));
        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void authorization_fail_test() throws Exception {
        // given
        LoginUser loginUser = new LoginUser(User.builder()
                .id(1L)
                .username("mamoo")
                .role(UserEnum.USER)
                .build());
        String token = jwtProcess.createAccessToken(loginUser);
        // when
        ResultActions resultActions = mvc.perform(get("/api/admin/hello").header(properties.getAccessKey(), token));
        // then
        resultActions.andExpect(status().isForbidden());
    }
}