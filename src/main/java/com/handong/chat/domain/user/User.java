package com.handong.chat.domain.user;

import com.handong.chat.domain.AuditingFields;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor // When JPA creates new entity, it makes the entity from no args constructor.
@AllArgsConstructor // for '@Builder' annotation.
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_tb")
@Entity
public class User extends AuditingFields {

    @Column(unique = true, nullable = false, length = 30)
    private String username;

    @Column(nullable = false, length = 60) // Consider encoded(by BCrypt) password length
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserEnum role; // ADMIN, USER

    @Column(nullable = false, length = 50)
    private String realname;

    @Column(length = 50)
    private String job;

    @Column(length = 20)
    private String sex;

    @Column(length = 50)
    private String instagramId;

    @Column(length = 5)
    private String age;

    @Column(length = 200)
    private String comment;

    @Builder
    public User(Long id, String deleted, String username, String password, UserEnum role, String realname, String job,
                String sex, String instagramId, String age, String comment) {
        super(id, deleted);
        this.username = username;
        this.password = password;
        this.role = role;
        this.realname = realname;
        this.job = job;
        this.sex = sex;
        this.instagramId = instagramId;
        this.age = age;
        this.comment = comment;
    }
}
