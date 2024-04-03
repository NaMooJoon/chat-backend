package com.handong.chat.service;

import com.handong.chat.domain.user.User;
import com.handong.chat.dto.user.UserRequestDto.JoinRequestDto;
import com.handong.chat.dto.user.UserResponseDto.DetailResponseDto;
import com.handong.chat.dto.user.UserResponseDto.JoinResponseDto;
import com.handong.chat.handler.ex.CustomApiException;
import com.handong.chat.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public JoinResponseDto join(JoinRequestDto requestDto) {

        Optional<User> userOptional = userRepository.findByUsername(requestDto.getUsername());
        if (userOptional.isPresent()) {
            throw new CustomApiException("Duplicated username exists...");
        }
        String rawPassword = requestDto.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);
        requestDto.setPassword(encPassword);

        User userPS = userRepository.save(requestDto.toEntity());
        return new JoinResponseDto(userPS);
    }

    public DetailResponseDto detail(Long id) {
        User user = userRepository.findById(id)
                        .orElseThrow(() -> new CustomApiException("The requested user doesn't exist"));
        return new DetailResponseDto(user);
    }

}
