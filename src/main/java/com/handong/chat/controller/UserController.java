package com.handong.chat.controller;

import com.handong.chat.dto.ResponseDto;
import com.handong.chat.service.UserService;
import com.handong.chat.dto.user.UserRequestDto.JoinRequestDto;
import com.handong.chat.dto.user.UserResponseDto.JoinResponseDto;
import com.handong.chat.dto.user.UserResponseDto.DetailResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원 정보 등록",
            description = "회원 신규 정보 등록을 위한 컨트롤러 (누구나 접근 가능) <br />"
                    + "@param JoinRequestDto <br />"
                    + "@return HttpStatus.CREATED(201) ResponseEntity\\<JoinResponseDto\\> <br />"
                    + "@exception 중복 <br />")
    @PreAuthorize("permitAll()")
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid JoinRequestDto joinRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(-1, "Validation errors", bindingResult.getAllErrors()));
        }
        JoinResponseDto joinResDto = userService.join(joinRequestDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "Join success", joinResDto), HttpStatus.CREATED);
    }

    @Operation(summary = "회원 정보 조회",
            description = "아이디를 통해 한명의 회원 정보 조회를 위한 컨트롤러 <br />"
                    + "@param id(PathVariable) <br />"
                    + "@return HttpStatus.OK(200) ResponseEntity\\<DetailResponseDto\\> <br />"
                    + "@exception 정보 없음 <br />")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Long id) {
        DetailResponseDto detailResDto = userService.detail(id);
        return new ResponseEntity<>(new ResponseDto<>(1, "User detail", detailResDto), HttpStatus.OK);
    }
}
