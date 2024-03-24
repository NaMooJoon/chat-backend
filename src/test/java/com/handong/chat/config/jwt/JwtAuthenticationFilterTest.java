package com.handong.chat.config.jwt;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.handong.chat.config.dummy.DummyObject;
import com.handong.chat.dto.user.UserRequestDto.LoginRequestDto;
import com.handong.chat.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@Sql("classpath:db/teardown.sql")
@ActiveProfiles({"test", "external"})
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK) // 가짜 환경으로 Spring 에 있는 component 들을 스캔 할 수 있음.
class JwtAuthenticationFilterTest extends DummyObject {

    @Autowired
    private ObjectMapper om;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ExternalProperties properties;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.save(newUser("mamoo", "마무"));
    }

    @Test
    void successfulAuthentication_test() throws Exception {
        // given
        LoginRequestDto loginReqDto = new LoginRequestDto();
        loginReqDto.setUsername("mamoo");
        loginReqDto.setPassword("1234");

        String requestBody = om.writeValueAsString(loginReqDto);
        // when
        ResultActions resultActions = mvc.perform(
                post("/api/login").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String accessToken = resultActions.andReturn().getResponse().getHeader(properties.getAccessKey());
        System.out.println("accessToken = " + accessToken);
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isOk());
        assertNotNull(accessToken);
        assertTrue(accessToken.startsWith(properties.getTokenPrefix()));
        resultActions.andExpect(jsonPath("$.data.username").value("mamoo"));
    }

    @Test
    void unsuccessfulAuthentication_test() throws Exception {
        // given
        LoginRequestDto loginReqDto = new LoginRequestDto();
        loginReqDto.setUsername("mamoo");
        loginReqDto.setPassword("12345");

        String requestBody = om.writeValueAsString(loginReqDto);
        // when
        ResultActions resultActions = mvc.perform(
                post("/api/login").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isUnauthorized());
    }
}