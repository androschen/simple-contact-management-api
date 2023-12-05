package com.restapi.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity{
    private static final String TABLE_NAME = "users";
    private static final String COLUMN_TOKEN_EXPIRED_AT = "TOKEN_EXPIRED_AT";
    @Id
    private String username;
    private String password;
    private String name;
    private String token;
    @Column(name = User.COLUMN_TOKEN_EXPIRED_AT)
    private Long tokenExpiredAt;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Contact> contacts;
}
