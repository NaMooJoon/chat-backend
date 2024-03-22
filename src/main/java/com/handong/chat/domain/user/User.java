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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Builder
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

}
