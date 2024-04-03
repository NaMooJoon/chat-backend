package com.handong.chat.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.handong.chat.config.dummy.DummyObject;
import com.handong.chat.dto.user.UserRequestDto.JoinRequestDto;
import com.handong.chat.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ActiveProfiles({"test", "external"})
@Sql("classpath:db/teardown.sql")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
class UserControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        userRepository.save(newUser("test@example.com", "테스트"));
        em.clear();
    }

    @Test
    void join_success_test() throws Exception {
        // given
        JoinRequestDto joinRequestDto = JoinRequestDto.builder()
                .username("love@example.com") // not email format (bad request)
                .password("1234")
                .realname("러브")
                .build();

        String requestBody = om.writeValueAsString(joinRequestDto);
        // when
        ResultActions resultActions = mvc.perform(
                post("/api/user/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);
        // then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    void join_bad_request_test() throws Exception {
        // given
        JoinRequestDto joinRequestDto = JoinRequestDto.builder()
                .username("love") // not email format (bad request)
                .password("1234")
                .realname("러브")
                .build();

        String requestBody = om.writeValueAsString(joinRequestDto);
        // when
        ResultActions resultActions = mvc.perform(
                post("/api/user/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void join_duplicated_fail_test() throws Exception {
        // given
        JoinRequestDto joinRequestDto = JoinRequestDto.builder()
                .username("test@example.com") // duplicated
                .password("1234")
                .realname("러브")
                .build();

        String requestBody = om.writeValueAsString(joinRequestDto);
        // when
        ResultActions resultActions = mvc.perform(
                post("/api/user/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);
        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @WithUserDetails(value = "test@example.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void detail_forbidden_test() throws Exception {
        // when
        ResultActions resultActions = mvc.perform(get("/api/user/detail/1"));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);
        // then
        resultActions.andExpect(jsonPath("$.code").value(1L));
        resultActions.andExpect(jsonPath("$.data.username").value("test@example.com"));
    }
}