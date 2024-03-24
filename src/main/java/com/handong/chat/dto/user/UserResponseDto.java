package com.handong.chat.dto.user;

import com.handong.chat.domain.user.User;
import com.handong.chat.util.CustomDateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserResponseDto {

    @Getter
    @Setter
    public static class LoginResponseDto {
        private Long id;
        private String username;
        private String createdAt;

        public LoginResponseDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.createdAt = CustomDateUtil.toStringFormat(user.getCreatedAt());
        }
    }

    @Schema
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinResponseDto {
        @Schema(description = "email", example = "abc123@example.com")
        private String username;

        public JoinResponseDto(User user) {
            this.username = user.getUsername();
        }
    }
}
