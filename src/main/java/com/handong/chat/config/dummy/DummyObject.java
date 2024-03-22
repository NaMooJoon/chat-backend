package com.handong.chat.config.dummy;

import com.handong.chat.domain.user.User;
import com.handong.chat.domain.user.UserEnum;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class DummyObject {

    protected User newUser(String username, String realname) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");

        return User.builder()
                .username(username)
                .password(encPassword)
                .role(UserEnum.USER)
                .realname(realname)
                .build();
    }

    protected User newMockUser(String username, String realname) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");

        return User.builder()
                .username(username)
                .password(encPassword)
                .role(UserEnum.USER)
                .realname(realname)
                .build();
    }
}
