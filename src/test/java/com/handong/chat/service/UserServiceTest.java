package com.handong.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.handong.chat.config.dummy.DummyObject;
import com.handong.chat.domain.user.User;
import com.handong.chat.domain.user.UserEnum;
import com.handong.chat.dto.user.UserRequestDto.JoinRequestDto;
import com.handong.chat.dto.user.UserResponseDto.JoinResponseDto;
import com.handong.chat.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 가짜 환경에서는
 * Spring 관련 Bean들이 존재하지 않는다.
 * 따라서, UserService안에 작동하는 Bean들을 등록을 해줘야한다.
 * @Mock: 가짜로 메모리 상에 객체를 생성.
 * @Spy: 얘는 진짜 객체를 들고 오는 것.
 * @InjectMocks: @Mock과 @Spy로 만들어진 객체를 주입하는 것.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest extends DummyObject {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void join_test() throws Exception {
        // given
        String username = "abc123@example.com";
        String realname = "abc";
        JoinRequestDto joinReqDto = JoinRequestDto.builder()
                .username(username)
                .password("1234")
                .realname(realname)
                .build();

        // stub 1
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        // stub 2
        User testUser = newMockUser(1L, username, realname, UserEnum.USER);
        when(userRepository.save(any())).thenReturn(testUser);

        // when
        JoinResponseDto joinResDto = userService.join(joinReqDto);
        System.out.println("joinResDto = " + joinResDto.getUsername());

        // then
        assertThat(joinResDto.getUsername()).isEqualTo(username);
    }
}