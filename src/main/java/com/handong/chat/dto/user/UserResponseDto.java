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

    @Schema
    @Getter
    @Setter
    public static class LoginResponseDto {
        @Schema(description = "id", example = "id")
        private Long id;
        @Schema(description = "email", example = "abc123@example.com")
        private String username;
        @Schema(description = "createdAt", example="2024-01-01 00:00:00.000000")
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

    @Schema
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailResponseDto {
        @Schema(description = "E-mail(id)", example="id")
        private String username;
        @Schema(description = "real name", example="Jack")
        private String realname;
        @Schema(description = "job", example="student")
        private String job;
        @Schema(description = "sex", example="male")
        private String sex;
        @Schema(description = "instargram id", example="@dkdjf")
        private String instagramId;
        @Schema(description = "age", example="25")
        private String age;
        @Schema(description = "introduce user self", example="Hello, ...")
        private String comment;

        public DetailResponseDto(User user) {
            this.username = user.getUsername();
            this.realname = user.getRealname();
            this.job = user.getJob();
            this.sex = user.getSex();
            this.instagramId = user.getInstagramId();
            this.age = user.getAge();
            this.comment = user.getComment();
        }
    }

    @Schema
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateResponseDto {
        @Schema(description = "real name", example="Jack")
        private String realname;
        @Schema(description = "job", example="student")
        private String job;
        @Schema(description = "sex", example="male")
        private String sex;
        @Schema(description = "instargram id", example="@dkdjf")
        private String instagramId;
        @Schema(description = "age", example="25")
        private String age;
        @Schema(description = "introduce user self", example="Hello, ...")
        private String comment;

        public UpdateResponseDto(User user) {
            this.realname = user.getRealname();
            this.job = user.getJob();
            this.sex = user.getSex();
            this.instagramId = user.getInstagramId();
            this.age = user.getAge();
            this.comment = user.getComment();
        }
    }
}
