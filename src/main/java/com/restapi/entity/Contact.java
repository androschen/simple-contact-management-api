package com.restapi.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contact")
public class Contact extends BaseEntity{
    private static final String TABLE_NAME = "contact";
    private static final String COLUMN_FIRST_NAME = "FIRST_NAME";
    private static final String COLUMN_LAST_NAME = "LAST_NAME";

    @Id
    private String id;
    @Column(name = Contact.COLUMN_FIRST_NAME)
    private String firstName;
    @Column(name = Contact.COLUMN_LAST_NAME)
    private String lastName;
    private String phone;
    private String email;
    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;
    @OneToMany(mappedBy = "contact")
    private List<Address> addresses;
}
