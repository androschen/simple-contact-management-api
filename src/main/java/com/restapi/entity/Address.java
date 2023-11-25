package com.restapi.entity;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
public class Address extends BaseEntity{
    @Id
    private String id;
    private String street;
    private String city;
    private String province;
    private String country;
    @Column(name = "postal_code")
    private String postalCode;
    @ManyToOne
    @JoinColumn(name = "contact_id", referencedColumnName = "id")
    private Contact contact;
}
