package com.handong.chat.dto.user;

import com.handong.chat.domain.user.User;
import com.handong.chat.domain.user.UserEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserRequestDto {

    @Getter
    @Setter
    public static class LoginRequestDto {
        private String username;
        private String password;
    }

    @Schema
    @Setter
    @Getter
    @Builder
    @AllArgsConstructor
    public static class JoinRequestDto {

        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}@[a-zA-Z0-9]{2,20}\\.[a-zA-Z]{2,6}$", message = "Please check if ID is email format")
        @Schema(description = "email", example = "abc123@example.com")
        private String username;

        @NotEmpty
        @Schema(description = "min 8, 1 number", example = "password12")
        private String password;

        @NotEmpty
        @Size(max = 50)
        @Pattern(regexp = "^[가-힣]{1,20}$", message = "Please input name using only Korean")
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

        public User toEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .role(UserEnum.USER)
                    .realname(realname)
                    .job(job)
                    .sex(sex)
                    .instagramId(instagramId)
                    .age(age)
                    .comment(comment)
                    .deleted("N")
                    .build();
        }
    }

    @Getter
    @Builder
    public static class UpdateRequestDto {
        @Schema(description = "user id", example = "id")
        private Long id;
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
        @Schema(description = "삭제 여부", example="Y")
        private String deleted;
    }
}
