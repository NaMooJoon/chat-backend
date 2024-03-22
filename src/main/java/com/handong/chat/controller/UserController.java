package com.handong.chat.controller;

import com.handong.chat.dto.ResponseDto;
import com.handong.chat.service.UserService;
import com.handong.chat.dto.user.UserRequestDto.JoinRequestDto;
import com.handong.chat.dto.user.UserResponseDto.JoinResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestParam @Valid JoinRequestDto joinRequestDto, BindingResult bindingResult) {
        JoinResponseDto joinResDto = userService.join(joinRequestDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "Join success", joinResDto), HttpStatus.CREATED);
    }
}
