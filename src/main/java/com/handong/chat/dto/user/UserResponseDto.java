package com.handong.chat.dto.user;

import com.handong.chat.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserResponseDto {
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
