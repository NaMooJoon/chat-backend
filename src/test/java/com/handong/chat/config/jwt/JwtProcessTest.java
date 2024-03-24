package com.handong.chat.config.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import com.handong.chat.config.auth.LoginUser;
import com.handong.chat.config.dummy.DummyObject;
import com.handong.chat.domain.user.User;
import com.handong.chat.domain.user.UserEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtProcessTest extends DummyObject {

    @Autowired
    private JwtProcess jwtProcess;
    @Autowired
    private ExternalProperties properties;

    @Test
    void createAccessToken_test() {
        // given
        User user = newMockUser(1L, "mamoo", "마무", UserEnum.USER);
        LoginUser loginUser = new LoginUser(user);
        // when
        String accessToken = jwtProcess.createAccessToken(loginUser);
        System.out.println("accessToken = " + accessToken);
        // then
        assertThat(accessToken).contains(properties.getTokenPrefix());
    }

    @Test
    void verifyAccessToken_test() {
        // given
        User user = newMockUser(1L, "mamoo", "마무", UserEnum.USER);
        LoginUser accessUser = new LoginUser(user);
        String accessToken = jwtProcess.createAccessToken(accessUser);
        // when
        accessToken = accessToken.replace(properties.getTokenPrefix(), "");
        LoginUser loginUser = jwtProcess.verifyAccessToken(accessToken);
        // then
        assertThat(loginUser.getUsername()).isEqualTo(accessUser.getUsername());
        assertThat(loginUser.getUser().getId()).isEqualTo(accessUser.getUser().getId());
    }
}