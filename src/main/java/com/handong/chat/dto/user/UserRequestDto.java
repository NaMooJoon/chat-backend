package com.handong.chat.dto.user;

import com.handong.chat.domain.user.User;
import com.handong.chat.domain.user.UserEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserRequestDto {
    @Schema
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinRequestDto {

        @NotNull
        @NotEmpty
        @Schema(description = "email", example = "abc123@example.com")
        private String username;

        @NotNull
        @NotEmpty
        @Schema(description = "min 8, 1 number", example = "password12")
        private String password;

        @NotNull
        @NotEmpty
        @Size(max = 50)
        @Schema(description = "real name", example = "Brown")
        private String realname;

        @Size(max = 50)
        @Schema(description = "job", example = "software developer")
        private String job;

        @Size(max = 20)
        @Schema(description = "sex", example = "male")
        private String sex;

        @Size(max = 50)
        @Schema(description = "Instargram ID", example = "aaa123")
        private String instagramId;

        @Size(max = 5)
        @Schema(description = "age", example = "25")
        private String age;

        @Size(max = 200)
        @Schema(description = "comment", example = "Hey, there!")
        private String comment;

        public User toEntity(BCryptPasswordEncoder passwordEncoder) {
            return User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .role(UserEnum.USER)
                    .realname(realname)
                    .job(job)
                    .sex(sex)
                    .instagramId(instagramId)
                    .age(age)
                    .comment(comment)
                    .build();
        }
    }
}
